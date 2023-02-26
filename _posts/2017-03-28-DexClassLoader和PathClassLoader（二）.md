---
layout: post
title:  "DexClassLoader和PathClassLoader（二）"
date:   2017-03-28 18:00:00 +520
categories: Android_Security
---

## 0x00 前言

在DexClassLoader和PathClassLoader（一）中，我用了几个小例子介绍了一下DexClassLoader的用法，并给出了完整的代码，有兴趣的同学可以跟着玩一下，也可以根据代码进行扩展

在介绍完使用方法后，简单的介绍了下整个加载流程，篇幅问题只是将大概的过程给梳理了一下，留了许多问题，虽然这些问题都没有明说，DexFile结构，DvmDex结构，DexOrJar结构...

那么这一篇开始，就会详细的讲解这其中的各种结构体，各种关键点

## 0x01 从DexClassLoader的构造函数说起

我们从DexClassLoader的构造函数说起，这里是整个加载过程的入口，也是出口

```java
public DexClassLoader(String dexPath, String optimizedDirectory,
        String libraryPath, ClassLoader parent) {
    super(dexPath, new File(optimizedDirectory), libraryPath, parent);
}
```

DexClassLoader继承BaseClassLoader，BaseClassLoader的构造函数

```java
public BaseDexClassLoader(String dexPath, File optimizedDirectory,
        String libraryPath, ClassLoader parent) {
    super(parent);
    this.pathList = new DexPathList(this, dexPath, libraryPath, optimizedDirectory);
}
```

DexPathList的构造函数

```java
public DexPathList(ClassLoader definingContext, String dexPath,
        String libraryPath, File optimizedDirectory) {

    //判断BaseClassLoader是否为空
    if (definingContext == null) {
        throw new NullPointerException("definingContext == null");
    }

    //待加载的Dex文件路径是否为空
    if (dexPath == null) {
        throw new NullPointerException("dexPath == null");
    }

    //判断优化后的ODex文件目录是否为空
    if (optimizedDirectory != null) {
        if (!optimizedDirectory.exists())  {
            throw new IllegalArgumentException(
                    "optimizedDirectory doesn't exist: "
                    + optimizedDirectory);
        }
        //如果优化后的ODex文件目录不为空，则判断是否可读可写
        if (!(optimizedDirectory.canRead()
                        && optimizedDirectory.canWrite())) {
            throw new IllegalArgumentException(
                    "optimizedDirectory not readable/writable: "
                    + optimizedDirectory);
        }
    }

    this.definingContext = definingContext;
    ArrayList<IOException> suppressedExceptions = new ArrayList<IOException>();

    //关键的一个函数
    this.dexElements = makeDexElements(splitDexPath(dexPath), optimizedDirectory,
                                       suppressedExceptions);
    if (suppressedExceptions.size() > 0) {
        this.dexElementsSuppressedExceptions =
            suppressedExceptions.toArray(new IOException[suppressedExceptions.size()]);
    } else {
        dexElementsSuppressedExceptions = null;
    }
    this.nativeLibraryDirectories = splitLibraryPath(libraryPath);
}
```

关键的一个函数调用

```java
this.dexElements = makeDexElements(splitDexPath(dexPath), optimizedDirectory,
                                           suppressedExceptions);
```

首先看dexElements的定义，这是一个Dex文件的集合

```java
/**
 * List of dex/resource (class path) elements.
 * Should be called pathElements, but the Facebook app uses reflection
 * to modify 'dexElements' (http://b/7726934).
 */
private final Element[] dexElements;
```

Element类有一个关键的变量dexFile

```java
/**
 * Element of the dex/resource file path
 */
/*package*/ static class Element {
    private final File file;
    private final boolean isDirectory;
    private final File zip;
    private final DexFile dexFile;

    private ZipFile zipFile;
    private boolean initialized;

    public Element(File file, boolean isDirectory, File zip, DexFile dexFile) {
        this.file = file;
        this.isDirectory = isDirectory;
        this.zip = zip;
        this.dexFile = dexFile;
    }
    //......
}
```

再看`makeDexElements()`方法

```java
private static Element[] makeDexElements(ArrayList<File> files, File optimizedDirectory,
                                         ArrayList<IOException> suppressedExceptions) {
    //定义一个Element ArrayList用于存储返回数据
    //因为可能有多个Dex路径，所以这里用了ArrayList
    ArrayList<Element> elements = new ArrayList<Element>();

    //遍历files，这里在上一篇有详细讲过，传入的Dex文件Path会有多个，用":"隔开
    //在调用本函数的时候会先进行待加载Dex文件路径的解析

    for (File file : files) {
        //定义两个变量，都先置为空，然后根据后缀进行赋值
        File zip = null;
        DexFile dex = null;
        String name = file.getName();

        if (name.endsWith(DEX_SUFFIX)) {
            try {
                //如果是Dex，将加载后返回的DexFile数据赋值给dex
                dex = loadDexFile(file, optimizedDirectory);
            } catch (IOException ex) {
                System.logE("Unable to load dex file: " + file, ex);
            }
        } else if (name.endsWith(APK_SUFFIX) || name.endsWith(JAR_SUFFIX)
                || name.endsWith(ZIP_SUFFIX)) {
            //非Dex文件的分支，也就是加载的是Jar，Zip，APK
            zip = file;

            try {
                //本质上还是调用loadDexFile()
                dex = loadDexFile(file, optimizedDirectory);
            } catch (IOException suppressed) {
                suppressedExceptions.add(suppressed);
            }
        } else if (file.isDirectory()) {
            elements.add(new Element(file, true, null, null));
        } else {
            System.logW("Unknown file type for: " + file);
        }

        //将加载后的文件加到Element ArrayList中
        if ((zip != null) || (dex != null)) {
            elements.add(new Element(file, false, zip, dex));
        }
    }

    return elements.toArray(new Element[elements.size()]);
}
```

`loadDexFile()`返回DexFile类型的数据

```java
private static DexFile loadDexFile(File file, File optimizedDirectory)
        throws IOException {
    //判断是否指定ODex的存储路径
    if (optimizedDirectory == null) {
        //未指定ODex存储路径则直接new一个DexFile对象返回
        return new DexFile(file);
    } else {
        //如果有指定ODex的存储路径
        //先处理ODex的存储路径，最后加上".dex"
        String optimizedPath = optimizedPathFor(file, optimizedDirectory);

        //调用DexFile.loadDex()
        return DexFile.loadDex(file.getPath(), optimizedPath, 0);
    }
}
```

处理ODex文件的存储路径后缀，如果有.dex就不处理，如果没有就加上

```java
private static String optimizedPathFor(File path,
        File optimizedDirectory) {
    String fileName = path.getName();
    if (!fileName.endsWith(DEX_SUFFIX)) {
        int lastDot = fileName.lastIndexOf(".");
        if (lastDot < 0) {
            fileName += DEX_SUFFIX;
        } else {
            StringBuilder sb = new StringBuilder(lastDot + 4);
            sb.append(fileName, 0, lastDot);
            sb.append(DEX_SUFFIX);
            fileName = sb.toString();
        }
    }

    File result = new File(optimizedDirectory, fileName);
    return result.getPath();
}
```

`loadDex()`方法，注意和未指定ODex文件存储路径的区别，那个只有一个参数，这里有三个参数

```java
static public DexFile loadDex(String sourcePathName, String outputPathName,
    int flags) throws IOException {
    //new一个DexFile对象然后返回
    return new DexFile(sourcePathName, outputPathName, flags);
}
```

查看对应的构造函数

```java
private DexFile(String sourceName, String outputName, int flags) throws IOException {
    //判断指定的ODex存储路径是否是应用自身的私有文件夹
    //如果不是自身的私有文件夹，会报异常
    //Google处于安全考虑，在多处的注释里都提过这点
    if (outputName != null) {
        try {
            String parent = new File(outputName).getParent();
            if (Libcore.os.getuid() != Libcore.os.stat(parent).st_uid) {
                throw new IllegalArgumentException("Optimized data directory " + parent
                        + " is not owned by the current user. Shared storage cannot protect"
                        + " your application from code injection attacks.");
            }
        } catch (ErrnoException ignored) {
            // assume we'll fail with a more contextual error later
        }
    }

    //这里关键
    mCookie = openDexFile(sourceName, outputName, flags);
    mFileName = sourceName;
    guard.open("close");
}
```

`mCookie`的定义，一个私有整型变量

```java
private int mCookie;
```

`openDexFile()`方法的前两个参数是待加载的Dex文件路径以及ODex文件的存储路径，前两个参数都是获取一个合法的绝对路径，第三个参数目测暂时没什么用

```java
private static int openDexFile(String sourceName, String outputName,
    int flags) throws IOException {
    return openDexFileNative(new File(sourceName).getCanonicalPath(),
                             (outputName == null) ? null : new File(outputName).getCanonicalPath(),
                             flags);
}
```

间接调用的是一个native函数

```java
native private static int openDexFileNative(String sourceName, String outputName,
    int flags) throws IOException;
```

native层中的对应关系

```c++
const DalvikNativeMethod dvm_dalvik_system_DexFile[] = {
    { "openDexFileNative",  "(Ljava/lang/String;Ljava/lang/String;I)I",
        Dalvik_dalvik_system_DexFile_openDexFileNative },
    { "openDexFile",        "([B)I",
        Dalvik_dalvik_system_DexFile_openDexFile_bytearray },
    { "closeDexFile",       "(I)V",
        Dalvik_dalvik_system_DexFile_closeDexFile },
    { "defineClassNative",  "(Ljava/lang/String;Ljava/lang/ClassLoader;I)Ljava/lang/Class;",
        Dalvik_dalvik_system_DexFile_defineClassNative },
    { "getClassNameList",   "(I)[Ljava/lang/String;",
        Dalvik_dalvik_system_DexFile_getClassNameList },
    { "isDexOptNeeded",     "(Ljava/lang/String;)Z",
        Dalvik_dalvik_system_DexFile_isDexOptNeeded },
    { NULL, NULL, NULL },
};
```

根据第一个Item找到对应的native函数，这里也是正是开始解析Dex文件的函数

```c++
static void Dalvik_dalvik_system_DexFile_openDexFileNative(const u4* args,
    JValue* pResult)
{
    //前两个参数转换类型
    //param1: sourceNameObj: 待加载Dex文件路径
    //param2: outputNameObj: ODex文件存储路径
    StringObject* sourceNameObj = (StringObject*) args[0];
    StringObject* outputNameObj = (StringObject*) args[1];

    //关键结构指针，解析Dex得到的数据，以及其它衍生出来的都会存储在这个结构里，用于返回
    DexOrJar* pDexOrJar = NULL;

    //如果加载的是Jar，Zip，APK文件会使用到这个变量
    //DexOrJar结构的一个成员变量
    JarFile* pJarFile;

    ////如果加载的是Dex文件会使用到这个变量
    //DexOrJar结构的一个成员变量
    RawDexFile* pRawDexFile;

    //两个char*类型的指针，用于存储两个路径
    char* sourceName;
    char* outputName;

    //判断待加载Dex文件路径是否为空
    //为空则无法加载，抛出异常
    if (sourceNameObj == NULL) {
        dvmThrowNullPointerException("sourceName == null");
        RETURN_VOID();
    }

    //先将待加载Dex文件的路径转为C的char*类型
    sourceName = dvmCreateCstrFromString(sourceNameObj);

    //判断ODex文件的存储路径是否为空
    //不为空则将路径转为char*类型
    //但这里为空并不会抛出异常，而是赋值为NULL
    if (outputNameObj != NULL)
        outputName = dvmCreateCstrFromString(outputNameObj);
    else
        outputName = NULL;

    //不能加载系统的Dex文件，这些Dex已经加载过了
    //还有一个原因，感兴趣的同学可以读一下源码的注释
    //注释就在这个位置，一大段
    if (dvmClassPathContains(gDvm.bootClassPath, sourceName)) {
        ALOGW("Refusing to reopen boot DEX '%s'", sourceName);
        dvmThrowIOException(
            "Re-opening BOOTCLASSPATH DEX files is not allowed");
        free(sourceName);
        free(outputName);
        RETURN_VOID();
    }

    //终于走完前面一大堆流程了
    //这里开始根据后缀名进行加载
    //dvmRawDexFileOpen()加载Dex文件
    //dvmJarFileOpen()加载Jar，Zip，APK文件
    if (hasDexExtension(sourceName)
            && dvmRawDexFileOpen(sourceName, outputName, &pRawDexFile, false) == 0) {
        ALOGV("Opening DEX file '%s' (DEX)", sourceName);

        pDexOrJar = (DexOrJar*) malloc(sizeof(DexOrJar));
        pDexOrJar->isDex = true;
        pDexOrJar->pRawDexFile = pRawDexFile;
        pDexOrJar->pDexMemory = NULL;
    } else if (dvmJarFileOpen(sourceName, outputName, &pJarFile, false) == 0) {
        ALOGV("Opening DEX file '%s' (Jar)", sourceName);

        pDexOrJar = (DexOrJar*) malloc(sizeof(DexOrJar));
        pDexOrJar->isDex = false;
        pDexOrJar->pJarFile = pJarFile;
        pDexOrJar->pDexMemory = NULL;
    } else {
        ALOGV("Unable to open DEX file '%s'", sourceName);
        dvmThrowIOException("unable to open DEX file");
    }

    //pDexOrJar就是前面说的一个关键数据结构指针，这个会返回存在Element List里
    //添加这个DexOrJar结构到一个Hash Table里，这个Table存的都是Dex文件，还有一些其它数据
    if (pDexOrJar != NULL) {
        pDexOrJar->fileName = sourceName;
        addToDexFileTable(pDexOrJar);
    } else {
        free(sourceName);
    }

    free(outputName);
    RETURN_PTR(pDexOrJar);
}
```

返回的这个`pDexOrJar`指针，我们往上翻，期间会将`pDexOrJar`的值赋值给`mCookie`，然后会返回Java的DexFile类实例对象，这个实例对象会赋值给`makeDexElements()`方法中的dex变量，也就是Element类的DexFile类型变量dexFile

- native private static int openDexFileNative(String sourceName, String outputName, int flags) throws IOException;
- private static int openDexFile(String sourceName, String outputName, int flags) throws IOException;
- private DexFile(String sourceName, String outputName, int flags) throws IOException;
    - mCookie = openDexFile(sourceName, outputName, flags);
    - mFileName = sourceName;
- static public DexFile loadDex(String sourcePathName, String outputPathName, int flags) throws IOException;
- private static DexFile loadDexFile(File file, File optimizedDirectory) throws IOException;
- private static Element[] makeDexElements(ArrayList<File> files, File optimizedDirectory, ArrayList<IOException> suppressedExceptions)

从这个方法开始，出现了多个结构体

- JarFile
- RawDexFile
- DexOrJar

这三个是直接就以迅雷不及掩耳盗铃之势出现的，其实还有两个很神奇的结构体，必须放最前面强势安利

- DexFile
- DvmDex

首先是DexFile，这个不是Java层的DexFile，这个结构如果详细解析的话蛮复杂的，脱壳的时候得此结构指针者得Dex文件啊，虽然现在可能不能这么说了

```c++
/*
 * Structure representing a DEX file.
 *
 * Code should regard DexFile as opaque, using the API calls provided here
 * to access specific structures.
 */
struct DexFile {
    /* directly-mapped "opt" header */
    const DexOptHeader* pOptHeader;

    /* pointers to directly-mapped structs and arrays in base DEX */
    const DexHeader*    pHeader;
    const DexStringId*  pStringIds;
    const DexTypeId*    pTypeIds;
    const DexFieldId*   pFieldIds;
    const DexMethodId*  pMethodIds;
    const DexProtoId*   pProtoIds;
    const DexClassDef*  pClassDefs;
    const DexLink*      pLinkData;

    /*
     * These are mapped out of the "auxillary" section, and may not be
     * included in the file.
     */
    //存储类的Hash和偏移，用于快速查找
    const DexClassLookup* pClassLookup;
    const void*         pRegisterMapPool;       // RegisterMapClassPool

    /* points to start of DEX file data */
    //Dex文件的内存首地址
    const u1*           baseAddr;

    /* track memory overhead for auxillary structures */
    int                 overhead;

    /* additional app-specific data structures associated with the DEX */
    //void*               auxData;
};
```

DvmDex的第一个成员就是`DexFile*`指针类型变量`pDexFile`，至关重要！！！！！！

```c++
/*
 * Some additional VM data structures that are associated with the DEX file.
 */
struct DvmDex {
    /* pointer to the DexFile we're associated with */
    DexFile*            pDexFile;

    /* clone of pDexFile->pHeader (it's used frequently enough) */
    const DexHeader*    pHeader;

    /* interned strings; parallel to "stringIds" */
    struct StringObject** pResStrings;

    /* resolved classes; parallel to "typeIds" */
    struct ClassObject** pResClasses;

    /* resolved methods; parallel to "methodIds" */
    struct Method**     pResMethods;

    /* resolved instance fields; parallel to "fieldIds" */
    /* (this holds both InstField and StaticField) */
    struct Field**      pResFields;

    /* interface method lookup cache */
    struct AtomicCache* pInterfaceCache;

    /* shared memory region with file contents */
    bool                isMappedReadOnly;
    MemMapping          memMap;

    jobject dex_object;

    /* lock ensuring mutual exclusion during updates */
    pthread_mutex_t     modLock;
};
```

介绍完这俩结构体，接下来才轮到刚才那仨

依次来分析一下，首先是`RawDexFile`，很是简单，就一个`pDvmDex`关键点

```c++
/*
 * Structure representing a "raw" DEX file, in its unswapped unoptimized
 * state.
 */
struct RawDexFile {
    char*       cacheFileName;
    DvmDex*     pDvmDex;
};
```

然后是`JarFile`，和上面的差不多，多了个`ZipArchive`

```c++
/*
 * This represents an open, scanned Jar file.  (It's actually for any Zip
 * archive that happens to hold a Dex file.)
 */
struct JarFile {
    ZipArchive  archive;
    //MemMapping  map;
    char*       cacheFileName;
    DvmDex*     pDvmDex;
};
```

这个重要了，`DexOrJar`是作为返回的数据，看起来成员就多了些

```c++
/*
 * Internal struct for managing DexFile.
 */
struct DexOrJar {
    char*       fileName;//文件名
    bool        isDex;//是否是Dex文件
    bool        okayToFree;
    RawDexFile* pRawDexFile;
    JarFile*    pJarFile;
    u1*         pDexMemory; // malloc()ed memory, if any
};
```

接下来，我们来整理一下中间调用到的几个函数

- hasDexExtension(sourceName);
- dvmRawDexFileOpen(sourceName, outputName, &pRawDexFile, false);
- dvmJarFileOpen(sourceName, outputName, &pJarFile, false);
- addToDexFileTable(pDexOrJar);

`hasDexExtension()`方法用于判断是否是Dex文件

```c++
static bool hasDexExtension(const char* name) {
    size_t len = strlen(name);

    return (len >= 5)
        && (name[len - 5] != '/')
        && (strcmp(&name[len - 4], ".dex") == 0);
}
```

`dvmRawDexFileOpen()`方法用于打开Dex以及一系列优化操作，这个才是真正的重点

一共四个参数，注意第三个，第三个原本就是一个指针，现在取指针的指针，是一个二级指针

```c++
sourceName
outputName
&pRawDexFile
false
int dvmRawDexFileOpen(const char* fileName, const char* odexOutputName,
    RawDexFile** ppRawDexFile, bool isBootstrap)
{
    DvmDex* pDvmDex = NULL;
    char* cachedName = NULL;
    int result = -1;
    int dexFd = -1;
    int optFd = -1;
    u4 modTime = 0;
    u4 adler32 = 0;
    size_t fileSize = 0;
    bool newFile = false;
    bool locked = false;

    //打开文件，获取一个只读的文件描述符
    dexFd = open(fileName, O_RDONLY);
    if (dexFd < 0) goto bail;

    //设置通过exec()运行时文件描述符会关闭
    dvmSetCloseOnExec(dexFd);

    //校验Magic Number，并且获取checkcum存储在adler32
    if (verifyMagicAndGetAdler32(dexFd, &adler32) < 0) {
        ALOGE("Error with header for %s", fileName);
        goto bail;
    }

    //获取修改时间和文件大小
    if (getModTimeAndSize(dexFd, &modTime, &fileSize) < 0) {
        ALOGE("Error with stat for %s", fileName);
        goto bail;
    }

    //对ODex文件存储路径做判断，如果为空，自动生成一个，不为空则使用指定的路径
    if (odexOutputName == NULL) {
        cachedName = dexOptGenerateCacheFileName(fileName, NULL);
        if (cachedName == NULL)
            goto bail;
    } else {
        cachedName = strdup(odexOutputName);
    }

    ALOGV("dvmRawDexFileOpen: Checking cache for %s (%s)",
            fileName, cachedName);

    //获取ODex文件的文件描述符，中间有不少其它操作和判断，包括newFile的赋值
    optFd = dvmOpenCachedDexFile(fileName, cachedName, modTime,
        adler32, isBootstrap, &newFile, /*createIfMissing=*/true);

    //获取ODex的文件描述符失败则退出
    if (optFd < 0) {
        ALOGI("Unable to open or create cache for %s (%s)",
                fileName, cachedName);
        goto bail;
    }
    locked = true;

    //如果是创建的新ODex文件，原先不存在，则进行处理
    //因为可能以前加载过，这是第二次或者第n次加载，那么就不需要进行各种优化什么的
    if (newFile) {
        u8 startWhen, copyWhen, endWhen;
        bool result;
        off_t dexOffset;

        dexOffset = lseek(optFd, 0, SEEK_CUR);
        result = (dexOffset > 0);

        //拷贝Dex文件到ODex文件的存储目录
        if (result) {
            startWhen = dvmGetRelativeTimeUsec();
            result = copyFileToFile(optFd, dexFd, fileSize) == 0;
            copyWhen = dvmGetRelativeTimeUsec();
        }

        //优化Dex--->ODex
        if (result) {
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

    //把ODex文件映射到内存中，但不仅仅是做了映射
    if (dvmDexFileOpenFromFd(optFd, &pDvmDex) != 0) {
        ALOGI("Unable to map cached %s", fileName);
        goto bail;
    }

    //释放文件锁
    if (locked) {
        if (!dvmUnlockCachedDexFile(optFd)) {
            ALOGE("Unable to unlock DEX file");
            goto bail;
        }
        locked = false;
    }

    ALOGV("Successfully opened '%s'", fileName);

    //申请空间，并给几个结构体成员赋值
    *ppRawDexFile = (RawDexFile*) calloc(1, sizeof(RawDexFile));
    (*ppRawDexFile)->cacheFileName = cachedName;
    (*ppRawDexFile)->pDvmDex = pDvmDex;
    cachedName = NULL;      // don't free it below
    result = 0;

bail:
    free(cachedName);
    if (dexFd >= 0) {
        close(dexFd);
    }
    if (optFd >= 0) {
        if (locked)
            (void) dvmUnlockCachedDexFile(optFd);
        close(optFd);
    }
    return result;
}
```

中间一些很重要的方法

- verifyMagicAndGetAdler32(dexFd, &adler32)
- getModTimeAndSize(dexFd, &modTime, &fileSize)
- dexOptGenerateCacheFileName(fileName, NULL)
- dvmOpenCachedDexFile(fileName, cachedName, modTime, adler32, isBootstrap, &newFile, /*createIfMissing=*/true)
- copyFileToFile(optFd, dexFd, fileSize)
- dvmOptimizeDexFile(optFd, dexOffset, fileSize, fileName, modTime, adler32, isBootstrap)
- dvmDexFileOpenFromFd(optFd, &pDvmDex)

`verifyMagicAndGetAdler32()`方法会校验前12个字节的数据，Magic Number和checksum

```c++
static int verifyMagicAndGetAdler32(int fd, u4 *adler32)
{
    //读取前12个字节，前八字节时Magic Number，后四个是checksum
    u1 headerStart[12];
    ssize_t amt = read(fd, headerStart, sizeof(headerStart));

    if (amt < 0) {
        ALOGE("Unable to read header: %s", strerror(errno));
        return -1;
    }

    if (amt != sizeof(headerStart)) {
        ALOGE("Unable to read full header (only got %d bytes)", (int) amt);
        return -1;
    }

    //校验是否是合法Magic Number
    if (!dexHasValidMagic((DexHeader*) (void*) headerStart)) {
        return -1;
    }

    //获取后四个字节的数据，存储在adler32中
    *adler32 = (u4) headerStart[8]
        | (((u4) headerStart[9]) << 8)
        | (((u4) headerStart[10]) << 16)
        | (((u4) headerStart[11]) << 24);

    return 0;
}
```

其中`dexHasValidMagic()`的参数转为DexHeader*类型

```c++
bool dexHasValidMagic(const DexHeader* pHeader)
{
    //获取前八字节
    const u1* magic = pHeader->magic;

    //指向第五个字节
    const u1* version = &magic[4];

    //校验前四字节，DEX_MAGIC的定义如下
    ///* DEX file magic number */
    //#define DEX_MAGIC       "dex\n"
    if (memcmp(magic, DEX_MAGIC, 4) != 0) {
        ALOGE("ERROR: unrecognized magic number (%02x %02x %02x %02x)",
            magic[0], magic[1], magic[2], magic[3]);
        return false;
    }

    //校验第五到第八字节，两个校验的字段定义如下
    //#define DEX_MAGIC_VERS  "036\0"
    //#define DEX_MAGIC_VERS_API_13  "035\0"
    if ((memcmp(version, DEX_MAGIC_VERS, 4) != 0) &&
            (memcmp(version, DEX_MAGIC_VERS_API_13, 4) != 0)) {
        /*
         * Magic was correct, but this is an unsupported older or
         * newer format variant.
         */
        ALOGE("ERROR: unsupported dex version (%02x %02x %02x %02x)",
            version[0], version[1], version[2], version[3]);
        return false;
    }

    //都校验通过返回True
    return true;
}
```

`getModTimeAndSize()`方法获取修改的时间以及文件大小，用到的`stat`结构体和`fstat()`有兴趣的同学可以深入分析一下，可以结合`ls -l`这个命令来理解

```c++
static int getModTimeAndSize(int fd, u4* modTime, size_t* size)
{
    struct stat buf;
    int result = fstat(fd, &buf);

    if (result < 0) {
        ALOGE("Unable to determine mod time: %s", strerror(errno));
        return -1;
    }

    *modTime = (u4) buf.st_mtime;
    *size = (size_t) buf.st_size;
    assert((size_t) buf.st_size == buf.st_size);

    return 0;
}
```

`stat`结构体大概是这样，具体我就不是很清楚了，但是可以直接通过文件描述符获取这些数据，然后直接获取修改时间和文件大小两个成员

```c++
struct stat {
    unsigned long long  st_dev;
    unsigned char       __pad0[4];

    unsigned long       __st_ino;
    unsigned int        st_mode;
    unsigned int        st_nlink;

    unsigned long       st_uid;
    unsigned long       st_gid;

    unsigned long long  st_rdev;
    unsigned char       __pad3[4];

    long long           st_size;
    unsigned long    st_blksize;
    unsigned long long  st_blocks;

    unsigned long       st_atime;
    unsigned long       st_atime_nsec;

    unsigned long       st_mtime;
    unsigned long       st_mtime_nsec;

    unsigned long       st_ctime;
    unsigned long       st_ctime_nsec;

    unsigned long long  st_ino;
};
```

当未指定ODex文件的存储路径时，会调用`dexOptGenerateCacheFileName()`生成一个存储路径，这应该是PathClassLoader的执行路径

```c++
char* dexOptGenerateCacheFileName(const char* fileName, const char* subFileName)
{
    char nameBuf[512];
    char absoluteFile[sizeof(nameBuf)];
    const size_t kBufLen = sizeof(nameBuf) - 1;
    const char* dataRoot;
    char* cp;

    absoluteFile[0] = '\0';

    //判断待加载Dex文件路径首字母，这里一定是相对路径
    if (fileName[0] != '/') {
        //获取当前路径
        if (getcwd(absoluteFile, kBufLen) == NULL) {
            ALOGE("Can't get CWD while opening jar file");
            return NULL;
        }
        //加上"/"
        strncat(absoluteFile, "/", kBufLen);
    }
    //再将两个路径结合，此时APK在"/data/app"下
    //absoluteFile: /data/app/packagaName/
    //fileName: xxx/xxx.dex
    strncat(absoluteFile, fileName, kBufLen);

    //我们讨论的情况它传进来的参数是空，这里不会执行
    if (subFileName != NULL) {
        strncat(absoluteFile, "/", kBufLen);
        strncat(absoluteFile, subFileName, kBufLen);
    }

    //除首字母"/"，把路径其余的"/"全部替换成"@"
    cp = absoluteFile + 1;
    while (*cp != '\0') {
        if (*cp == '/') {
            *cp = '@';
        }
        cp++;
    }

    dataRoot = getenv("ANDROID_DATA");
    if (dataRoot == NULL)
        dataRoot = "/data";

    //生成前缀"/data/dalvik-cache"
    snprintf(nameBuf, kBufLen, "%s/%s", dataRoot, kCacheDirectoryName);

    //再次结合
    //"/data/dalvik-cache/data@app@packageName.apk@xxx.dex"
    //举个例子: /data/dalvik-cache/data@app@com.wnagzihxa1n.dexclassloaderdemo-18.apk@classes.dex
    //由于没有指定ODex文件的存储路径，其实这里就是PathClassLoader会执行的分支
    //PathClassLoader用于加载这些已安装的APK的classes.dex文件
    strncat(nameBuf, absoluteFile, kBufLen);

    ALOGV("Cache file for '%s' '%s' is '%s'", fileName, subFileName, nameBuf);
    return strdup(nameBuf);
}
```

那么到这里，前面的准备工作都已经完成，包括各种路径的有效性验证，输出路径的生成等

下面的四个方法留着下一篇慢慢分析

- dvmOpenCachedDexFile(fileName, cachedName, modTime, adler32, isBootstrap, &newFile, /*createIfMissing=*/true)
- copyFileToFile(optFd, dexFd, fileSize)
- dvmOptimizeDexFile(optFd, dexOffset, fileSize, fileName, modTime, adler32, isBootstrap)
- dvmDexFileOpenFromFd(optFd, &pDvmDex)