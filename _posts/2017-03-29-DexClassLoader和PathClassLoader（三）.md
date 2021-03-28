---
layout: post
title:  "DexClassLoader和PathClassLoader（三）"
date:   2017-03-29 18:00:00 +520
categories: Android_Security
---

## 0x00 前言
在DexClassLoader和PathClassLoader(2)中，将加载Dex文件前期的准备工作详细的分析了一遍，包括几个很关键的结构体

篇幅问题，留了几个方法，从命名来看，明显的看出来这是关键的几个方法

- dvmOpenCachedDexFile(fileName, cachedName, modTime, adler32, isBootstrap, &newFile, /*createIfMissing=*/true)
- copyFileToFile(optFd, dexFd, fileSize)
- dvmOptimizeDexFile(optFd, dexOffset, fileSize, fileName, modTime, adler32, isBootstrap)
- dvmDexFileOpenFromFd(optFd, &pDvmDex)

## 0x01 四个关键
dvmOpenCachedDexFile()方法参数蛮多的
```
fileName
cachedName
modTime
adler32
isBootstrap
&newFile
/createIfMissing=/true
```

主要作用就是判断是否存在已优化的ODex文件
- 如果不存在，先创建一个，然后写一下ODex Header，返回文件描述符
- 如果存在，则各种判断，再返回文件描述符(该分支我们后面分析)

```
int dvmOpenCachedDexFile(const char* fileName, const char* cacheFileName,
    u4 modWhen, u4 crc, bool isBootstrap, bool* pNewFile, bool createIfMissing)
{
    int fd, cc;
    struct stat fdStat, fileStat;
    bool readOnly = false;

    *pNewFile = false;

retry:
    //传进来的createIfMissing是True，所以这里会创建文件
    //第一个参数: 文件路径
    //第二个参数O_CREAT: 文件不存在则创建
    //第二个参数O_RDWR: 可读可写打开文件
    //第三个参数0664: 权限的数字表示，用于描述前面创建的文件权限: rw-rw-rw-
    fd = createIfMissing ? open(cacheFileName, O_CREAT|O_RDWR, 0644) : -1;

    //出问题未获得ODex文件的文件描述符
    if (fd < 0) {
        //只读模式打开
        fd = open(cacheFileName, O_RDONLY, 0);
        if (fd < 0) {
            //只读打开依旧失败，那就只能失败了，返回小于0的文件描述符fd
            if (createIfMissing) {
                const std::string errnoString(strerror(errno));
                if (directoryIsValid(cacheFileName)) {
                    ALOGE("Can't open dex cache file '%s': %s", cacheFileName, errnoString.c_str());
                }
            }
            return fd;
        }
        //readOnly变量对于后续的逻辑判断很重要，不过总归是已经创建了ODex文件
        readOnly = true;
    } else {
        fchmod(fd, 0644);
    }

    //这里给新建的ODex文件描述符fd上了个锁，并且将线程状态设置为VMWAIT状态
    //注释里的说法是一定要上个锁，就算这个时候有其它线程在操作这个文件，也要等它操作完了
    //再给它上锁，说是后面有大用处，说是在等待某外部资源，其实应该是在等待Dex文件的数据写进来:)
    ALOGV("DexOpt: locking cache file %s (fd=%d, boot=%d)",
        cacheFileName, fd, isBootstrap);
    ThreadStatus oldStatus = dvmChangeStatus(NULL, THREAD_VMWAIT);
    cc = flock(fd, LOCK_EX | LOCK_NB);
    if (cc != 0) {
        ALOGD("DexOpt: sleeping on flock(%s)", cacheFileName);
        cc = flock(fd, LOCK_EX);
    }
    dvmChangeStatus(NULL, oldStatus);
    if (cc != 0) {
        ALOGE("Can't lock dex cache '%s': %d", cacheFileName, cc);
        close(fd);
        return -1;
    }
    ALOGV("DexOpt:  locked cache file");

    //进行两重校验，比如创建文件出错，打开出错的问题
    cc = fstat(fd, &fdStat);
    if (cc != 0) {
        ALOGE("Can't stat open file '%s'", cacheFileName);
        LOGVV("DexOpt: unlocking cache file %s", cacheFileName);
        goto close_fail;
    }
    cc = stat(cacheFileName, &fileStat);
    if (cc != 0 ||
        fdStat.st_dev != fileStat.st_dev || fdStat.st_ino != fileStat.st_ino)
    {
        ALOGD("DexOpt: our open cache file is stale; sleeping and retrying");
        LOGVV("DexOpt: unlocking cache file %s", cacheFileName);
        flock(fd, LOCK_UN);
        close(fd);
        usleep(250 * 1000);     /* if something is hosed, don't peg machine */
        goto retry;
    }

    //如果文件大小为0，说明是我们刚创建的
    //注意readOnly变量，我刚才说这个变量非常重要，这里有所体现
    if (fdStat.st_size == 0) {
        //ODex文件的头部是ODex Header，如果是只读打开，那么并不符合这里的操作需求
        //所以如果是只读，跳到失败分支
        if (readOnly) {
            ALOGW("DexOpt: file has zero length and isn't writable");
            goto close_fail;
        }
        //创建一个空的ODex Header
        cc = dexOptCreateEmptyHeader(fd);
        if (cc != 0)
            goto close_fail;
        //这个变量会在上一层函数中作为是否是新建文件的标志决定是否执行优化Dex的操作
        *pNewFile = true;
        ALOGV("DexOpt: successfully initialized new cache file");
    } else {
        //这个分支其实是以前加载该Dex过后，在目标路径存在对应的ODex文件
        //那么就会直接使用该ODex文件，而且会使用NewFile变量表示不是新创建的文件
        bool expectVerify, expectOpt;

        if (gDvm.classVerifyMode == VERIFY_MODE_NONE) {
            expectVerify = false;
        } else if (gDvm.classVerifyMode == VERIFY_MODE_REMOTE) {
            expectVerify = !isBootstrap;
        } else /*if (gDvm.classVerifyMode == VERIFY_MODE_ALL)*/ {
            expectVerify = true;
        }

        if (gDvm.dexOptMode == OPTIMIZE_MODE_NONE) {
            expectOpt = false;
        } else if (gDvm.dexOptMode == OPTIMIZE_MODE_VERIFIED ||
                   gDvm.dexOptMode == OPTIMIZE_MODE_FULL) {
            expectOpt = expectVerify;
        } else /*if (gDvm.dexOptMode == OPTIMIZE_MODE_ALL)*/ {
            expectOpt = true;
        }

        ALOGV("checking deps, expecting vfy=%d opt=%d",
            expectVerify, expectOpt);

        if (!dvmCheckOptHeaderAndDependencies(fd, true, modWhen, crc,
                expectVerify, expectOpt))
        {
            if (readOnly) {
                if (createIfMissing) {
                    ALOGW("Cached DEX '%s' (%s) is stale and not writable",
                        fileName, cacheFileName);
                }
                goto close_fail;
            }

            ALOGD("ODEX file is stale or bad; removing and retrying (%s)",
                cacheFileName);
            if (ftruncate(fd, 0) != 0) {
                ALOGW("Warning: unable to truncate cache file '%s': %s",
                    cacheFileName, strerror(errno));
                /* keep going */
            }
            if (unlink(cacheFileName) != 0) {
                ALOGW("Warning: unable to remove cache file '%s': %d %s",
                    cacheFileName, errno, strerror(errno));
                /* keep going; permission failure should probably be fatal */
            }
            LOGVV("DexOpt: unlocking cache file %s", cacheFileName);
            flock(fd, LOCK_UN);
            close(fd);
            goto retry;
        } else {
            ALOGV("DexOpt: good deps in cache file");
        }
    }

    assert(fd >= 0);
    return fd;

close_fail:
    flock(fd, LOCK_UN);
    close(fd);
    return -1;
}
```

当打开了目标ODex文件，但是并不知道该ODex文件的状态，到底是新创建的还是已经优化后的？

所以判断了ODex文件的Size，如果是0说明该文件刚创建，会调用`dexOptCreateEmptyHeader()`方法生成ODex Header
```
int dexOptCreateEmptyHeader(int fd)
{
    DexOptHeader optHdr;
    ssize_t actual;

    //保证文件指针指向首字节
    assert(lseek(fd, 0, SEEK_CUR) == 0);

    assert((sizeof(optHdr) & 0x07) == 0);

    //初始化整个ODex Header
    memset(&optHdr, 0xff, sizeof(optHdr));

    //ODex Header的后面就是Dex文件
    //此处给成员dexOffset赋值
    optHdr.dexOffset = sizeof(optHdr);

    //将只初始化了一个成员变量值的optHdr数据写进ODex文件
    actual = write(fd, &optHdr, sizeof(optHdr));
    if (actual != sizeof(optHdr)) {
        int err = errno ? errno : -1;
        ALOGE("opt header write failed: %s", strerror(errno));
        return errno;
    }

    return 0;
}
```

DexOptHeader结构体
```
struct DexOptHeader {
    u1  magic[8];           /* includes version number */

    u4  dexOffset;          /* file offset of DEX header */
    u4  dexLength;
    u4  depsOffset;         /* offset of optimized DEX dependency table */
    u4  depsLength;
    u4  optOffset;          /* file offset of optimized data tables */
    u4  optLength;

    u4  flags;              /* some info flags */
    u4  checksum;           /* adler32 checksum covering deps/opt */

    /* pad for 64-bit alignment if necessary */
};
```

这隔着蛮远，重新来看`dvmRawDexFileOpe()`接下来的代码，newFile变量我在前面强调过很多次，用于标记ODex是否是新创建

如果是新创建的ODex，说明未优化，也就需要进入接下来的分支里，如果不是新创建的，说明已经优化过了，那么接下来这个分支里的优化代码没有必要执行，此处我们假设是第一次加载
```
if (newFile) {
    u8 startWhen, copyWhen, endWhen;
    bool result;
    off_t dexOffset;

    //获取当前文件指针
    //由于dvmOpenCachedDexFile()里创建的ODex文件写了ODex Header进去，所以文件指针一定大于0
    dexOffset = lseek(optFd, 0, SEEK_CUR);
    result = (dexOffset > 0);

    if (result) {
        startWhen = dvmGetRelativeTimeUsec();
        //
        result = copyFileToFile(optFd, dexFd, fileSize) == 0;
        copyWhen = dvmGetRelativeTimeUsec();
    }

    if (result) {
        //继续处理生成ODex文件
        result = dvmOptimizeDexFile(optFd, dexOffset, fileSize,
            fileName, modTime, adler32, isBootstrap);
    }

    if (!result) {
        ALOGE("Unable to extract+optimize DEX from '%s'", fileName);
        goto bail;
    }

    endWhen = dvmGetRelativeTimeUsec();
    ALOGD("DEX prep '%s': copy in %dms, rewrite %dms",
        fileName,
        (int) (copyWhen - startWhen) / 1000,
        (int) (endWhen - copyWhen) / 1000);
}
```

`copyFileToFile()`用于将Dex文件拷贝到ODex文件中，执行完后ODex文件已经有了ODex Header和Dex两个成员
```
static int copyFileToFile(int destFd, int srcFd, size_t size)
{
    //将文件指针指向Dex文件的首字节
    if (lseek(srcFd, 0, SEEK_SET) != 0) {
        ALOGE("lseek failure: %s", strerror(errno));
        return -1;
    }

    //拷贝
    return sysCopyFileToFile(destFd, srcFd, size);
}

int sysCopyFileToFile(int outFd, int inFd, size_t count)
{
    const size_t kBufSize = 32768;
    unsigned char buf[kBufSize];

    while (count != 0) {
        size_t getSize = (count > kBufSize) ? kBufSize : count;

        ssize_t actual = TEMP_FAILURE_RETRY(read(inFd, buf, getSize));
        if (actual != (ssize_t) getSize) {
            ALOGW("sysCopyFileToFile: copy read failed (%d vs %zd)",
                (int) actual, getSize);
            return -1;
        }

        if (sysWriteFully(outFd, buf, getSize, "sysCopyFileToFile") != 0)
            return -1;

        count -= getSize;
    }

    return 0;
}
```

在介绍`dvmOptimizeDexFile()`之前，有一个重要的变量`gDvm`，前面也有出现过几次，但是都不是很惹眼

随意看几个成员就好了，实在是太长了，`gDvm`是虚拟机的一个全局变量，存储着各种数据，在虚拟机初始化的时候被创建
```
struct DvmGlobals {
    /*
     * Some options from the command line or environment.
     */
    char*       bootClassPathStr;
    char*       classPathStr;

    size_t      heapStartingSize;
    size_t      heapMaximumSize;
    size_t      heapGrowthLimit;
    bool        lowMemoryMode;
    double      heapTargetUtilization;
    size_t      heapMinFree;
    size_t      heapMaxFree;
    size_t      stackSize;
    size_t      mainThreadStackSize;
    
    ...
```

`dvmOptimizeDexFile()`调用了`dexopt`命令来优化Dex，在这里只是做了参数的选择，然后调用了优化命令
```
bool dvmOptimizeDexFile(int fd, off_t dexOffset, long dexLength,
    const char* fileName, u4 modWhen, u4 crc, bool isBootstrap)
{
    const char* lastPart = strrchr(fileName, '/');
    if (lastPart != NULL)
        lastPart++;
    else
        lastPart = fileName;

    ALOGD("DexOpt: --- BEGIN '%s' (bootstrap=%d) ---", lastPart, isBootstrap);

    pid_t pid;

    /*
     * This could happen if something in our bootclasspath, which we thought
     * was all optimized, got rejected.
     */
    if (gDvm.optimizing) {
        ALOGW("Rejecting recursive optimization attempt on '%s'", fileName);
        return false;
    }

    //fork一个子进程，在子进程中完成优化过程
    //fork()有两个返回值，等于和不等于0
    //返回0的说明在子进程中
    //返回大于0说明在父进程中，小于0说明fork失败了
    pid = fork();
    if (pid == 0) {
        static const int kUseValgrind = 0;
        static const char* kDexOptBin = "/bin/dexopt";
        static const char* kValgrinder = "/usr/bin/valgrind";
        static const int kFixedArgCount = 10;
        static const int kValgrindArgCount = 5;
        static const int kMaxIntLen = 12;   // '-'+10dig+'\0' -OR- 0x+8dig
        int bcpSize = dvmGetBootPathSize();
        int argc = kFixedArgCount + bcpSize
            + (kValgrindArgCount * kUseValgrind);
        const char* argv[argc+1];             // last entry is NULL
        char values[argc][kMaxIntLen];
        char* execFile;
        const char* androidRoot;
        int flags;

        /* change process groups, so we don't clash with ProcessManager */
        setpgid(0, 0);

        //dexopt工具的完整路径
        //类似：/system/bin/dexopt
        androidRoot = getenv("ANDROID_ROOT");
        if (androidRoot == NULL) {
            ALOGW("ANDROID_ROOT not set, defaulting to /system");
            androidRoot = "/system";
        }
        execFile = (char*)alloca(strlen(androidRoot) + strlen(kDexOptBin) + 1);
        strcpy(execFile, androidRoot);
        strcat(execFile, kDexOptBin);

        //构造命令参数
        int curArg = 0;

        //这个if里的条件不成立
        //kUseValgrind初始化为0
        if (kUseValgrind) {
            /* probably shouldn't ship the hard-coded path */
            argv[curArg++] = (char*)kValgrinder;
            argv[curArg++] = "--tool=memcheck";
            argv[curArg++] = "--leak-check=yes";        // check for leaks too
            argv[curArg++] = "--leak-resolution=med";   // increase from 2 to 4
            argv[curArg++] = "--num-callers=16";        // default is 12
            assert(curArg == kValgrindArgCount);
        }
        argv[curArg++] = execFile;

        argv[curArg++] = "--dex";

        //#define DALVIK_VM_BUILD         27
        sprintf(values[2], "%d", DALVIK_VM_BUILD);
        argv[curArg++] = values[2];

        //ODex文件的文件描述符
        sprintf(values[3], "%d", fd);
        argv[curArg++] = values[3];

        //Dex文件在ODex文件中的偏移，就在ODex Header后面
        sprintf(values[4], "%d", (int) dexOffset);
        argv[curArg++] = values[4];

        //Dex文件的长度
        sprintf(values[5], "%d", (int) dexLength);
        argv[curArg++] = values[5];

        //Dex文件名
        argv[curArg++] = (char*)fileName;

        //Dex文件修改时间
        sprintf(values[7], "%d", (int) modWhen);
        argv[curArg++] = values[7];

        //checksum
        sprintf(values[8], "%d", (int) crc);
        argv[curArg++] = values[8];

        //这一堆还有待研究
        flags = 0;
        if (gDvm.dexOptMode != OPTIMIZE_MODE_NONE) {
            flags |= DEXOPT_OPT_ENABLED;
            if (gDvm.dexOptMode == OPTIMIZE_MODE_ALL)
                flags |= DEXOPT_OPT_ALL;
        }
        if (gDvm.classVerifyMode != VERIFY_MODE_NONE) {
            flags |= DEXOPT_VERIFY_ENABLED;
            if (gDvm.classVerifyMode == VERIFY_MODE_ALL)
                flags |= DEXOPT_VERIFY_ALL;
        }
        if (isBootstrap)
            flags |= DEXOPT_IS_BOOTSTRAP;
        if (gDvm.generateRegisterMaps)
            flags |= DEXOPT_GEN_REGISTER_MAPS;
        sprintf(values[9], "%d", flags);
        argv[curArg++] = values[9];

        //第一个括号的条件会满足
        assert(((!kUseValgrind && curArg == kFixedArgCount) ||
               ((kUseValgrind && curArg == kFixedArgCount+kValgrindArgCount))));

        //前面获取了ClassPath的数量，已经加在了argv的长度中
        ClassPathEntry* cpe;
        for (cpe = gDvm.bootClassPath; cpe->ptr != NULL; cpe++) {
            argv[curArg++] = cpe->fileName;
        }
        assert(curArg == argc);

        argv[curArg] = NULL;

        //kUseValgrind一直是0，虽然不排除其它地方会对它修改
        //但是目前来看，会执行else里的代码
        if (kUseValgrind)
            execv(kValgrinder, const_cast<char**>(argv));
        else
            execv(execFile, const_cast<char**>(argv));

        //execv()会停掉本进程，去执行progName
        //所以正常情况下不会到这里，如果这里执行说明上面出了问题
        ALOGE("execv '%s'%s failed: %s", execFile,
            kUseValgrind ? " [valgrind]" : "", strerror(errno));
        exit(1);
    } else {
        ALOGV("DexOpt: waiting for verify+opt, pid=%d", (int) pid);
        int status;
        pid_t gotPid;

        //等等等等等等等等等等等等等等等等等等等等
        ThreadStatus oldStatus = dvmChangeStatus(NULL, THREAD_VMWAIT);
        while (true) {
            gotPid = waitpid(pid, &status, 0);
            if (gotPid == -1 && errno == EINTR) {
                ALOGD("waitpid interrupted, retrying");
            } else {
                break;
            }
        }

        //等完继续执行
        dvmChangeStatus(NULL, oldStatus);
        if (gotPid != pid) {
            ALOGE("waitpid failed: wanted %d, got %d: %s",
                (int) pid, (int) gotPid, strerror(errno));
            return false;
        }

        if (WIFEXITED(status) && WEXITSTATUS(status) == 0) {
            ALOGD("DexOpt: --- END '%s' (success) ---", lastPart);
            return true;
        } else {
            ALOGW("DexOpt: --- END '%s' --- status=0x%04x, process failed",
                lastPart, status);
            return false;
        }
    }
}
```

上面说到的dexopt源码在/dalvik/dexopt/OptMain.cpp

就是一个可执行的ELF文件，找到`main()`函数入口
```
int main(int argc, char* const argv[])
{
    //设置进程名
    set_process_name("dexopt");

    setvbuf(stdout, NULL, _IONBF, 0);

    //通过第二个参数来决定调用的方法
    //隐约记得命令是"/system/bin/dexopt --dex ......"
    if (argc > 1) {
        if (strcmp(argv[1], "--zip") == 0)
            return fromZip(argc, argv);
        else if (strcmp(argv[1], "--dex") == 0)
            return fromDex(argc, argv);
        else if (strcmp(argv[1], "--preopt") == 0)
            return preopt(argc, argv);
    }

    fprintf(stderr,
        "Usage:\n\n"
        "Short version: Don't use this.\n\n"
        "Slightly longer version: This system-internal tool is used to\n"
        "produce optimized dex files. See the source code for details.\n");

    return 1;
}
```

`fromDex()`获取了传进来的参数，并且在最后调用了两个函数
```
static int fromDex(int argc, char* const argv[])
{
    int result = -1;
    bool vmStarted = false;
    char* bootClassPath = NULL;
    int fd, flags, vmBuildVersion;
    long offset, length;
    const char* debugFileName;
    u4 crc, modWhen;
    char* endp;
    bool onlyOptVerifiedDex = false;
    DexClassVerifyMode verifyMode;
    DexOptimizerMode dexOptMode;

    //参数不够
    if (argc < 10) {
        ALOGE("Not enough arguments for --dex (found %d)", argc);
        goto bail;
    }

    //argv先自加1，跳过"--dex"，同时argc自减1
    argc--;
    argv++;

    //接下来这一段都是获取参数
    //一共获取了8个，在上一层中，这里是10个，去掉了"/dexopt --dex"这两个
    GET_ARG(vmBuildVersion, strtol, "bad vm build");
    if (vmBuildVersion != DALVIK_VM_BUILD) {
        ALOGE("DexOpt: build rev does not match VM: %d vs %d",
            vmBuildVersion, DALVIK_VM_BUILD);
        goto bail;
    }
    GET_ARG(fd, strtol, "bad fd");
    GET_ARG(offset, strtol, "bad offset");
    GET_ARG(length, strtol, "bad length");
    debugFileName = *++argv;

    //此处argc自减1，去掉上面的debugFileName
    --argc;
    GET_ARG(modWhen, strtoul, "bad modWhen");
    GET_ARG(crc, strtoul, "bad crc");
    GET_ARG(flags, strtol, "bad flags");

    ALOGV("Args: fd=%d off=%ld len=%ld name='%s' mod=%#x crc=%#x flg=%d (argc=%d)",
        fd, offset, length, debugFileName, modWhen, crc, flags, argc);
    assert(argc > 0);

    //此处判断"bootClassPath"的数量，可为0
    //后续的参数都是"bootClassPath"，数量不为0则使用":"进行拼接
    if (--argc == 0) {
        bootClassPath = strdup("");
    } else {
        int i, bcpLen;
        char* const* argp;
        char* cp;

        bcpLen = 0;
        for (i = 0, argp = argv; i < argc; i++) {
            ++argp;
            ALOGV("DEP: '%s'", *argp);
            bcpLen += strlen(*argp) + 1;
        }

        cp = bootClassPath = (char*) malloc(bcpLen +1);
        for (i = 0, argp = argv; i < argc; i++) {
            int strLen;

            ++argp;
            strLen = strlen(*argp);
            if (i != 0)
                *cp++ = ':';
            memcpy(cp, *argp, strLen);
            cp += strLen;
        }
        *cp = '\0';

        assert((int) strlen(bootClassPath) == bcpLen-1);
    }
    ALOGV("  bootclasspath is '%s'", bootClassPath);

    //这一段依旧有待研究
    if ((flags & DEXOPT_VERIFY_ENABLED) != 0) {
        if ((flags & DEXOPT_VERIFY_ALL) != 0)
            verifyMode = VERIFY_MODE_ALL;
        else
            verifyMode = VERIFY_MODE_REMOTE;
    } else {
        verifyMode = VERIFY_MODE_NONE;
    }
    if ((flags & DEXOPT_OPT_ENABLED) != 0) {
        if ((flags & DEXOPT_OPT_ALL) != 0)
            dexOptMode = OPTIMIZE_MODE_ALL;
        else
            dexOptMode = OPTIMIZE_MODE_VERIFIED;
    } else {
        dexOptMode = OPTIMIZE_MODE_NONE;
    }

    //从命名来看，是优化前的初始化工作
    if (dvmPrepForDexOpt(bootClassPath, dexOptMode, verifyMode, flags) != 0) {
        ALOGE("VM init failed");
        goto bail;
    }

    vmStarted = true;

    //这里应该就是真正的优化操作
    if (!dvmContinueOptimization(fd, offset, length, debugFileName,
            modWhen, crc, (flags & DEXOPT_IS_BOOTSTRAP) != 0))
    {
        ALOGE("Optimization failed");
        goto bail;
    }

    result = 0;

bail:

#if 0
    if (vmStarted) {
        ALOGI("DexOpt shutting down, result=%d", result);
        dvmShutdown();
    }
#endif

    free(bootClassPath);
    ALOGV("DexOpt command complete (result=%d)", result);
    return result;
}
```

最后调用的两个函数，从注释以及命名来看，第一个是准备工作，第二个是真的进行优化
```
dvmPrepForDexOpt(bootClassPath, dexOptMode, verifyMode, flags)
dvmContinueOptimization(fd, offset, length, debugFileName, modWhen, crc, (flags & DEXOPT_IS_BOOTSTRAP)
dvmPrepForDexOpt()看起来好严肃的样子，都是一些跟Dalvik虚拟机有关的设置

int dvmPrepForDexOpt(const char* bootClassPath, DexOptimizerMode dexOptMode,
    DexClassVerifyMode verifyMode, int dexoptFlags)
{
    gDvm.initializing = true;
    gDvm.optimizing = true;

    blockSignals();

    setCommandLineDefaults();
    free(gDvm.bootClassPathStr);
    gDvm.bootClassPathStr = strdup(bootClassPath);

    gDvm.dexOptMode = dexOptMode;
    gDvm.classVerifyMode = verifyMode;
    gDvm.generateRegisterMaps = (dexoptFlags & DEXOPT_GEN_REGISTER_MAPS) != 0;
    if (dexoptFlags & DEXOPT_SMP) {
        assert((dexoptFlags & DEXOPT_UNIPROCESSOR) == 0);
        gDvm.dexOptForSmp = true;
    } else if (dexoptFlags & DEXOPT_UNIPROCESSOR) {
        gDvm.dexOptForSmp = false;
    } else {
        gDvm.dexOptForSmp = (ANDROID_SMP != 0);
    }

    if (!dvmGcStartup())
        goto fail;
    if (!dvmThreadStartup())
        goto fail;
    if (!dvmInlineNativeStartup())
        goto fail;
    if (!dvmRegisterMapStartup())
        goto fail;
    if (!dvmInstanceofStartup())
        goto fail;
    if (!dvmClassStartup())
        goto fail;

    return 0;

fail:
    dvmShutdown();
    return 1;
}
```

`dvmContinueOptimization()`函数看着长，但是我们记住，它操作的永远是那个ODex文件，无论是映射还是其它什么的，最终都会改变ODex文件，而改变的途径就是修改ODex文件描述符`fd`
```
bool dvmContinueOptimization(int fd, off_t dexOffset, long dexLength,
    const char* fileName, u4 modWhen, u4 crc, bool isBootstrap)
{
    DexClassLookup* pClassLookup = NULL;
    RegisterMapBuilder* pRegMapBuilder = NULL;

    assert(gDvm.optimizing);

    ALOGV("Continuing optimization (%s, isb=%d)", fileName, isBootstrap);

    assert(dexOffset >= 0);

    //对Dex长度的一个小校验
    if (dexLength < (int) sizeof(DexHeader)) {
        ALOGE("too small to be DEX");
        return false;
    }

    //对ODex Header长度的一个小校验
    if (dexOffset < (int) sizeof(DexOptHeader)) {
        ALOGE("not enough room for opt header");
        return false;
    }

    bool result = false;

    gDvm.optimizingBootstrapClass = isBootstrap;

    {
        bool success;
        void* mapAddr;

        //将ODex文件映射到内存中，映射的数据长度为ODex Header + Dex
        mapAddr = mmap(NULL, dexOffset + dexLength, PROT_READ|PROT_WRITE,
                    MAP_SHARED, fd, 0);
        if (mapAddr == MAP_FAILED) {
            ALOGE("unable to mmap DEX cache: %s", strerror(errno));
            goto bail;
        }

        bool doVerify, doOpt;
        if (gDvm.classVerifyMode == VERIFY_MODE_NONE) {
            doVerify = false;
        } else if (gDvm.classVerifyMode == VERIFY_MODE_REMOTE) {
            doVerify = !gDvm.optimizingBootstrapClass;
        } else /*if (gDvm.classVerifyMode == VERIFY_MODE_ALL)*/ {
            doVerify = true;
        }

        if (gDvm.dexOptMode == OPTIMIZE_MODE_NONE) {
            doOpt = false;
        } else if (gDvm.dexOptMode == OPTIMIZE_MODE_VERIFIED ||
                   gDvm.dexOptMode == OPTIMIZE_MODE_FULL) {
            doOpt = doVerify;
        } else /*if (gDvm.dexOptMode == OPTIMIZE_MODE_ALL)*/ {
            doOpt = true;
        }

        //rewriteDex()负责重写Dex文件，比如4字节对齐，结构重排，字节码优化等
        success = rewriteDex(((u1*) mapAddr) + dexOffset, dexLength,
                    doVerify, doOpt, &pClassLookup, NULL);

        if (success) {
            DvmDex* pDvmDex = NULL;

            //Dex文件在内存中真实的地址
            u1* dexAddr = ((u1*) mapAddr) + dexOffset;

            //整体dump的脱壳点
            //早期的部分壳可以，比如爱加密，过一下TracerPid反调试就可以整体dump了
            //那是多么幸福的脱壳年代，没有类抽取，没有VMP
            if (dvmDexFileOpenPartial(dexAddr, dexLength, &pDvmDex) != 0) {
                ALOGE("Unable to create DexFile");
                success = false;
            } else {
                if (gDvm.generateRegisterMaps) {
                    //这个结构体用于Precise GC(精确垃圾回收)
                    //少有文章讲这个，能搜到的文章都是简单的提了一句，有必要跟着代码分析一遍
                    //太长不看版：RegisterMap以Method为单位，模拟指令执行，判断是否指向对象
                    //Dalvik虚拟机在执行GC的时候，就可以确定哪些是可以回收的，哪些是不能回收的
                    pRegMapBuilder = dvmGenerateRegisterMaps(pDvmDex);
                    if (pRegMapBuilder == NULL) {
                        ALOGE("Failed generating register maps");
                        success = false;
                    }
                }

                //更新dexAddr指向的Dex文件的checksum
                DexHeader* pHeader = (DexHeader*)pDvmDex->pHeader;
                updateChecksum(dexAddr, dexLength, pHeader);

                //这一段对于ODex文件的操作完成，释放pDvmDex
                dvmDexFileFree(pDvmDex);
            }
        }

        //强行将映射区的数据写回文件，原先这片区域是SHARED，也会进行同步
        if (msync(mapAddr, dexOffset + dexLength, MS_SYNC) != 0) {
            ALOGW("msync failed: %s", strerror(errno));
        }
#if 1
        //释放掉映射的内存空间
        if (munmap(mapAddr, dexOffset + dexLength) != 0) {
            ALOGE("munmap failed: %s", strerror(errno));
            goto bail;
        }
#endif

        if (!success)
            goto bail;
    }

    off_t depsOffset, optOffset, endOffset, adjOffset;
    int depsLength, optLength;
    u4 optChecksum;

    //ODex文件的Dex段后面是依赖段
    depsOffset = lseek(fd, 0, SEEK_END);
    if (depsOffset < 0) {
        ALOGE("lseek to EOF failed: %s", strerror(errno));
        goto bail;
    }

    //调整依赖段的起始偏移，八字节对齐
    adjOffset = (depsOffset + 7) & ~(0x07);
    if (adjOffset != depsOffset) {
        ALOGV("Adjusting deps start from %d to %d",
            (int) depsOffset, (int) adjOffset);
        depsOffset = adjOffset;
        lseek(fd, depsOffset, SEEK_SET);
    }

    //把依赖段的数据写进去
    if (writeDependencies(fd, modWhen, crc) != 0) {
        ALOGW("Failed writing dependencies");
        goto bail;
    }

    //计算依赖段的长度，然后再设置opt段八字节对齐
    optOffset = lseek(fd, 0, SEEK_END);
    depsLength = optOffset - depsOffset;

    adjOffset = (optOffset + 7) & ~(0x07);
    if (adjOffset != optOffset) {
        ALOGV("Adjusting opt start from %d to %d",
            (int) optOffset, (int) adjOffset);
        optOffset = adjOffset;
        lseek(fd, optOffset, SEEK_SET);
    }

    //写优化段的数据
    if (!writeOptData(fd, pClassLookup, pRegMapBuilder)) {
        ALOGW("Failed writing opt data");
        goto bail;
    }

    endOffset = lseek(fd, 0, SEEK_END);
    optLength = endOffset - optOffset;

    //计算checksum
    if (!computeFileChecksum(fd, depsOffset,
            (optOffset+optLength) - depsOffset, &optChecksum))
    {
        goto bail;
    }

    //填充ODex Header
    DexOptHeader optHdr;
    memset(&optHdr, 0xff, sizeof(optHdr));
    memcpy(optHdr.magic, DEX_OPT_MAGIC, 4);
    memcpy(optHdr.magic+4, DEX_OPT_MAGIC_VERS, 4);
    optHdr.dexOffset = (u4) dexOffset;
    optHdr.dexLength = (u4) dexLength;
    optHdr.depsOffset = (u4) depsOffset;
    optHdr.depsLength = (u4) depsLength;
    optHdr.optOffset = (u4) optOffset;
    optHdr.optLength = (u4) optLength;
#if __BYTE_ORDER != __LITTLE_ENDIAN
    optHdr.flags = DEX_OPT_FLAG_BIG;
#else
    optHdr.flags = 0;
#endif
    optHdr.checksum = optChecksum;

    //同步数据
    fsync(fd);      

    //把刚才的ODex Header结构体的数据写进ODex文件
    lseek(fd, 0, SEEK_SET);
    if (sysWriteFully(fd, &optHdr, sizeof(optHdr), "DexOpt opt header") != 0)
        goto bail;

    ALOGV("Successfully wrote DEX header");
    result = true;

    //dvmRegisterMapDumpStats();

bail:
    dvmFreeRegisterMapBuilder(pRegMapBuilder);
    free(pClassLookup);
    return result;
}
```

这个函数执行完，ODex文件也就优化的差不多了

其中有几个函数需要进一步跟进

- rewriteDex(((u1*) mapAddr) + dexOffset, dexLength, doVerify, doOpt, &pClassLookup, NULL)
- dvmDexFileOpenPartial(dexAddr, dexLength, &pDvmDex)
- dvmGenerateRegisterMaps(pDvmDex)
- writeDependencies(fd, modWhen, crc)
- writeOptData(fd, pClassLookup, pRegMapBuilder)