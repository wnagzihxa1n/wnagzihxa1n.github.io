---
layout: post
title:  "DexClassLoader和PathClassLoader（四）"
date:   2017-04-01 18:00:00 +520
categories: Android_Security
---

## 0x00 前言
在DexClassLoader和PathClassLoader(三)中，分析到了最核心的生成ODex文件部分，在这个过程中，有几个比较关键的具体实现函数没有分析
- rewriteDex(((u1*) mapAddr) + dexOffset, dexLength, doVerify, doOpt, &pClassLookup, NULL)
- dvmDexFileOpenPartial(dexAddr, dexLength, &pDvmDex)
- dvmGenerateRegisterMaps(pDvmDex)
- writeDependencies(fd, modWhen, crc)
- writeOptData(fd, pClassLookup, pRegMapBuilder)

而且，还有一个上上篇留下来的最后一个函数
- dvmDexFileOpenFromFd(optFd, &pDvmDex)

## 0x01 继续优化
在上一篇，我说过`dvmOptimizeDexFile()`里无论生成多少变量，多少结构体对象，最终都是为了操作ODex文件，这一点尤其重要，只要知道这一点，我们就可以不需要过分关心中间的各种乱七八糟的变量

首先是`rewriteDex()`
```
static bool rewriteDex(u1* addr, int len, bool doVerify, bool doOpt,
    DexClassLookup** ppClassLookup, DvmDex** ppDvmDex)
{
    DexClassLookup* pClassLookup = NULL;
    u8 prepWhen, loadWhen, verifyOptWhen;
    DvmDex* pDvmDex = NULL;
    bool result = false;
    const char* msgStr = "???";

    //调整字节序，并且进行Magic Number和checksum等校验
    if (dexSwapAndVerify(addr, len) != 0)
        goto bail;

    //解析优化Dex
    if (dvmDexFileOpenPartial(addr, len, &pDvmDex) != 0) {
        ALOGE("Unable to create DexFile");
        goto bail;
    }

    //类的哈希表，用于快速查找
    pClassLookup = dexCreateClassLookup(pDvmDex->pDexFile);
    if (pClassLookup == NULL)
        goto bail;
    pDvmDex->pDexFile->pClassLookup = pClassLookup;

    //优化标志，由传进来的参数决定，如果不优化直接退出
    if (!doVerify && !doOpt) {
        result = true;
        goto bail;
    }

    prepWhen = dvmGetRelativeTimeUsec();

    //加载所有的类
    if (!loadAllClasses(pDvmDex))
        goto bail;
    loadWhen = dvmGetRelativeTimeUsec();

    if (!dvmCreateInlineSubsTable())
        goto bail;

    //验证并且优化所有的类
    verifyAndOptimizeClasses(pDvmDex->pDexFile, doVerify, doOpt);
    verifyOptWhen = dvmGetRelativeTimeUsec();

    if (doVerify && doOpt)
        msgStr = "verify+opt";
    else if (doVerify)
        msgStr = "verify";
    else if (doOpt)
        msgStr = "opt";
    ALOGD("DexOpt: load %dms, %s %dms, %d bytes",
        (int) (loadWhen - prepWhen) / 1000,
        msgStr,
        (int) (verifyOptWhen - loadWhen) / 1000,
        gDvm.pBootLoaderAlloc->curOffset);

    result = true;

bail:
    //用上面的数据对某些结构体成员进行赋值
    if (pDvmDex != NULL) {
        /* break link between the two */
        pDvmDex->pDexFile->pClassLookup = NULL;
    }

    if (ppDvmDex == NULL || !result) {
        dvmDexFileFree(pDvmDex);
    } else {
        *ppDvmDex = pDvmDex;
    }

    if (ppClassLookup == NULL || !result) {
        free(pClassLookup);
    } else {
        *ppClassLookup = pClassLookup;
    }

    return result;
}
```

又扩展出了几个函数
- dvmDexFileOpenPartial(addr, len, &pDvmDex)
- dexCreateClassLookup(pDvmDex->pDexFile)
- loadAllClasses(pDvmDex)
- dvmCreateInlineSubsTable()
- verifyAndOptimizeClasses(pDvmDex->pDexFile, doVerify, doOpt)

第一个函数，我们在上一层见过，也是待分析的函数之一，也就是说，在这个函数执行完，还会再执行一遍，而且这个是曾经传说中的脱壳点，我们认真分析一波
```
int dvmDexFileOpenPartial(const void* addr, int len, DvmDex** ppDvmDex)
{
    DvmDex* pDvmDex;
    DexFile* pDexFile;
    int parseFlags = kDexParseDefault;
    int result = -1;

    /* -- file is incomplete, new checksum has not yet been calculated
    if (gDvm.verifyDexChecksum)
        parseFlags |= kDexParseVerifyChecksum;
    */

    //解析Dex文件，此时的Dex只是原始的Dex
    pDexFile = dexFileParse((u1*)addr, len, parseFlags);
    if (pDexFile == NULL) {
        ALOGE("DEX parse failed");
        goto bail;
    }

    //填充*pDvmDex结构体数据
    pDvmDex = allocateAuxStructures(pDexFile);
    if (pDvmDex == NULL) {
        dexFileFree(pDexFile);
        goto bail;
    }

    pDvmDex->isMappedReadOnly = false;
    *ppDvmDex = pDvmDex;
    result = 0;

bail:
    return result;
}
```

跟进`dexFileParse()`，其中有一个对是否已经优化Dex的判断
```
DexFile* dexFileParse(const u1* data, size_t length, int flags)
{
    DexFile* pDexFile = NULL;
    const DexHeader* pHeader;
    const u1* magic;
    int result = -1;

    //长度校验
    if (length < sizeof(DexHeader)) {
        ALOGE("too short to be a valid .dex");
        goto bail;      /* bad file format */
    }

    //申请DexFile结构体大小的空间并初始化
    pDexFile = (DexFile*) malloc(sizeof(DexFile));
    if (pDexFile == NULL)
        goto bail;      /* alloc failure */
    memset(pDexFile, 0, sizeof(DexFile));

    //这里用于判断是否优化过
    //我们第一次调用这里，所以必然是没有优化过的
    //直接跳过，但是后面那次的调用，就需要跟进执行了
    if (memcmp(data, DEX_OPT_MAGIC, 4) == 0) {
        magic = data;
        if (memcmp(magic+4, DEX_OPT_MAGIC_VERS, 4) != 0) {
            ALOGE("bad opt version (0x%02x %02x %02x %02x)",
                 magic[4], magic[5], magic[6], magic[7]);
            goto bail;
        }

        pDexFile->pOptHeader = (const DexOptHeader*) data;
        ALOGV("Good opt header, DEX offset is %d, flags=0x%02x",
            pDexFile->pOptHeader->dexOffset, pDexFile->pOptHeader->flags);

        /* parse the optimized dex file tables */
        if (!dexParseOptData(data, length, pDexFile))
            goto bail;

        /* ignore the opt header and appended data from here on out */
        data += pDexFile->pOptHeader->dexOffset;
        length -= pDexFile->pOptHeader->dexOffset;
        if (pDexFile->pOptHeader->dexLength > length) {
            ALOGE("File truncated? stored len=%d, rem len=%d",
                pDexFile->pOptHeader->dexLength, (int) length);
            goto bail;
        }
        length = pDexFile->pOptHeader->dexLength;
    }

    //填充*pDexFile结构体数据
    dexFileSetupBasicPointers(pDexFile, data);
    pHeader = pDexFile->pHeader;

    //校验Magic Number
    if (!dexHasValidMagic(pHeader)) {
        goto bail;
    }

    //校验Checksum
    if (flags & kDexParseVerifyChecksum) {
        u4 adler = dexComputeChecksum(pHeader);
        if (adler != pHeader->checksum) {
            ALOGE("ERROR: bad checksum (%08x vs %08x)",
                adler, pHeader->checksum);
            if (!(flags & kDexParseContinueOnError))
                goto bail;
        } else {
            ALOGV("+++ adler32 checksum (%08x) verified", adler);
        }

        const DexOptHeader* pOptHeader = pDexFile->pOptHeader;
        if (pOptHeader != NULL) {
            adler = dexComputeOptChecksum(pOptHeader);
            if (adler != pOptHeader->checksum) {
                ALOGE("ERROR: bad opt checksum (%08x vs %08x)",
                    adler, pOptHeader->checksum);
                if (!(flags & kDexParseContinueOnError))
                    goto bail;
            } else {
                ALOGV("+++ adler32 opt checksum (%08x) verified", adler);
            }
        }
    }

    //校验Signature
    if (kVerifySignature) {
        unsigned char sha1Digest[kSHA1DigestLen];
        const int nonSum = sizeof(pHeader->magic) + sizeof(pHeader->checksum) +
                            kSHA1DigestLen;

        dexComputeSHA1Digest(data + nonSum, length - nonSum, sha1Digest);
        if (memcmp(sha1Digest, pHeader->signature, kSHA1DigestLen) != 0) {
            char tmpBuf1[kSHA1DigestOutputLen];
            char tmpBuf2[kSHA1DigestOutputLen];
            ALOGE("ERROR: bad SHA1 digest (%s vs %s)",
                dexSHA1DigestToStr(sha1Digest, tmpBuf1),
                dexSHA1DigestToStr(pHeader->signature, tmpBuf2));
            if (!(flags & kDexParseContinueOnError))
                goto bail;
        } else {
            ALOGV("+++ sha1 digest verified");
        }
    }

    if (pHeader->fileSize != length) {
        ALOGE("ERROR: stored file size (%d) != expected (%d)",
            (int) pHeader->fileSize, (int) length);
        if (!(flags & kDexParseContinueOnError))
            goto bail;
    }

    if (pHeader->classDefsSize == 0) {
        ALOGE("ERROR: DEX file has no classes in it, failing");
        goto bail;
    }

    /*
     * Success!
     */
    result = 0;

bail:
    if (result != 0 && pDexFile != NULL) {
        dexFileFree(pDexFile);
        pDexFile = NULL;
    }
    return pDexFile;
}
```

其中的`dexFileSetupBasicPointers()`对`*pDexFile`结构赋值的关键操作
```
void dexFileSetupBasicPointers(DexFile* pDexFile, const u1* data) {
    DexHeader *pHeader = (DexHeader*) data;

    pDexFile->baseAddr = data;
    pDexFile->pHeader = pHeader;
    pDexFile->pStringIds = (const DexStringId*) (data + pHeader->stringIdsOff);
    pDexFile->pTypeIds = (const DexTypeId*) (data + pHeader->typeIdsOff);
    pDexFile->pFieldIds = (const DexFieldId*) (data + pHeader->fieldIdsOff);
    pDexFile->pMethodIds = (const DexMethodId*) (data + pHeader->methodIdsOff);
    pDexFile->pProtoIds = (const DexProtoId*) (data + pHeader->protoIdsOff);
    pDexFile->pClassDefs = (const DexClassDef*) (data + pHeader->classDefsOff);
    pDexFile->pLinkData = (const DexLink*) (data + pHeader->linkOff);
}
```

另一个`allocateAuxStructures()`用于填充`*pDvmDex`结构体数据
```
static DvmDex* allocateAuxStructures(DexFile* pDexFile)
{
    DvmDex* pDvmDex;
    const DexHeader* pHeader;
    u4 stringSize, classSize, methodSize, fieldSize;

    pHeader = pDexFile->pHeader;

    stringSize = pHeader->stringIdsSize * sizeof(struct StringObject*);
    classSize  = pHeader->typeIdsSize * sizeof(struct ClassObject*);
    methodSize = pHeader->methodIdsSize * sizeof(struct Method*);
    fieldSize  = pHeader->fieldIdsSize * sizeof(struct Field*);

    u4 totalSize = sizeof(DvmDex) +
                   stringSize + classSize + methodSize + fieldSize;

    u1 *blob = (u1 *)dvmAllocRegion(totalSize,
                              PROT_READ | PROT_WRITE, "dalvik-aux-structure");
    if ((void *)blob == MAP_FAILED)
        return NULL;

    pDvmDex = (DvmDex*)blob;
    blob += sizeof(DvmDex);

    pDvmDex->pDexFile = pDexFile;
    pDvmDex->pHeader = pHeader;

    pDvmDex->pResStrings = (struct StringObject**)blob;
    blob += stringSize;
    pDvmDex->pResClasses = (struct ClassObject**)blob;
    blob += classSize;
    pDvmDex->pResMethods = (struct Method**)blob;
    blob += methodSize;
    pDvmDex->pResFields = (struct Field**)blob;

    ALOGV("+++ DEX %p: allocateAux (%d+%d+%d+%d)*4 = %d bytes",
        pDvmDex, stringSize/4, classSize/4, methodSize/4, fieldSize/4,
        stringSize + classSize + methodSize + fieldSize);

    pDvmDex->pInterfaceCache = dvmAllocAtomicCache(DEX_INTERFACE_CACHE_SIZE);

    dvmInitMutex(&pDvmDex->modLock);

    return pDvmDex;
}
```

`dexCreateClassLookup()`用于生成类的哈希表，一个循环解决
```
DexClassLookup* dexCreateClassLookup(DexFile* pDexFile)
{
    DexClassLookup* pLookup;
    int allocSize;
    int i, numEntries;
    int numProbes, totalProbes, maxProbes;

    numProbes = totalProbes = maxProbes = 0;

    assert(pDexFile != NULL);

    numEntries = dexRoundUpPower2(pDexFile->pHeader->classDefsSize * 2);
    allocSize = offsetof(DexClassLookup, table)
                    + numEntries * sizeof(pLookup->table[0]);

    pLookup = (DexClassLookup*) calloc(1, allocSize);
    if (pLookup == NULL)
        return NULL;
    pLookup->size = allocSize;
    pLookup->numEntries = numEntries;

    for (i = 0; i < (int)pDexFile->pHeader->classDefsSize; i++) {
        const DexClassDef* pClassDef;
        const char* pString;

        pClassDef = dexGetClassDef(pDexFile, i);
        pString = dexStringByTypeIdx(pDexFile, pClassDef->classIdx);

        classLookupAdd(pDexFile, pLookup,
            (u1*)pString - pDexFile->baseAddr,
            (u1*)pClassDef - pDexFile->baseAddr, &numProbes);

        if (numProbes > maxProbes)
            maxProbes = numProbes;
        totalProbes += numProbes;
    }

    ALOGV("Class lookup: classes=%d slots=%d (%d%% occ) alloc=%d"
         " total=%d max=%d",
        pDexFile->pHeader->classDefsSize, numEntries,
        (100 * pDexFile->pHeader->classDefsSize) / numEntries,
        allocSize, totalProbes, maxProbes);

    return pLookup;
}
```

`loadAllClasses()`加载所有类，纯加载，啥都不干
```
static bool loadAllClasses(DvmDex* pDvmDex)
{
    u4 count = pDvmDex->pDexFile->pHeader->classDefsSize;
    u4 idx;
    int loaded = 0;

    ALOGV("DexOpt: +++ trying to load %d classes", count);

    dvmSetBootPathExtraDex(pDvmDex);

    //功能类似初始化和检查
    if (!dvmFindRequiredClassesAndMembers()) {
        return false;
    }

    //初始化Class，是Class
    if (!dvmInitClass(gDvm.classJavaLangClass)) {
        ALOGE("ERROR: failed to initialize the class Class!");
        return false;
    }

    for (idx = 0; idx < count; idx++) {
        const DexClassDef* pClassDef;
        const char* classDescriptor;
        ClassObject* newClass;

        pClassDef = dexGetClassDef(pDvmDex->pDexFile, idx);
        classDescriptor =
            dexStringByTypeIdx(pDvmDex->pDexFile, pClassDef->classIdx);

        ALOGV("+++  loading '%s'", classDescriptor);
        //newClass = dvmDefineClass(pDexFile, classDescriptor,
        //        NULL);
        newClass = dvmFindSystemClassNoInit(classDescriptor);
        if (newClass == NULL) {
            ALOGV("DexOpt: failed loading '%s'", classDescriptor);
            dvmClearOptException(dvmThreadSelf());
        } else if (newClass->pDvmDex != pDvmDex) {
            ALOGD("DexOpt: '%s' has an earlier definition; blocking out",
                classDescriptor);
            SET_CLASS_FLAG(newClass, CLASS_MULTIPLE_DEFS);
        } else {
            loaded++;
        }
    }
    ALOGV("DexOpt: +++ successfully loaded %d classes", loaded);

    dvmSetBootPathExtraDex(NULL);
    return true;
}
```

据说`dvmCreateInlineSubsTable()`是一个辅助的结构体

`verifyAndOptimizeClasses()`用于验证和优化类
```
static void verifyAndOptimizeClasses(DexFile* pDexFile, bool doVerify,
    bool doOpt)
{
    u4 count = pDexFile->pHeader->classDefsSize;
    u4 idx;

    for (idx = 0; idx < count; idx++) {
        const DexClassDef* pClassDef;
        const char* classDescriptor;
        ClassObject* clazz;

        pClassDef = dexGetClassDef(pDexFile, idx);
        classDescriptor = dexStringByTypeIdx(pDexFile, pClassDef->classIdx);

        clazz = dvmLookupClass(classDescriptor, NULL, false);
        if (clazz != NULL) {
            //调用验证
            verifyAndOptimizeClass(pDexFile, clazz, pClassDef, doVerify, doOpt);

        } else {
            ALOGV("DexOpt: not optimizing unavailable class '%s'",
                classDescriptor);
        }
    }

#ifdef VERIFIER_STATS
    ALOGI("Verifier stats:");
    ALOGI(" methods examined        : %u", gDvm.verifierStats.methodsExamined);
    ALOGI(" monitor-enter methods   : %u", gDvm.verifierStats.monEnterMethods);
    ALOGI(" instructions examined   : %u", gDvm.verifierStats.instrsExamined);
    ALOGI(" instructions re-examined: %u", gDvm.verifierStats.instrsReexamined);
    ALOGI(" copying of register sets: %u", gDvm.verifierStats.copyRegCount);
    ALOGI(" merging of register sets: %u", gDvm.verifierStats.mergeRegCount);
    ALOGI(" ...that caused changes  : %u", gDvm.verifierStats.mergeRegChanged);
    ALOGI(" uninit searches         : %u", gDvm.verifierStats.uninitSearches);
    ALOGI(" max memory required     : %u", gDvm.verifierStats.biggestAlloc);
#endif
}
```

`verifyAndOptimizeClass()`间接被调用用于验证
```
static void verifyAndOptimizeClass(DexFile* pDexFile, ClassObject* clazz,
    const DexClassDef* pClassDef, bool doVerify, bool doOpt)
{
    const char* classDescriptor;
    bool verified = false;

    if (clazz->pDvmDex->pDexFile != pDexFile) {
        ALOGD("DexOpt: not verifying/optimizing '%s': multiple definitions",
            clazz->descriptor);
        return;
    }

    classDescriptor = dexStringByTypeIdx(pDexFile, pClassDef->classIdx);

    if (doVerify) {
        //注释说，先验证
        if (dvmVerifyClass(clazz)) {
            assert((clazz->accessFlags & JAVA_FLAGS_MASK) ==
                pClassDef->accessFlags);
            ((DexClassDef*)pClassDef)->accessFlags |= CLASS_ISPREVERIFIED;
            verified = true;
        } else {
            ALOGV("DexOpt: '%s' failed verification", classDescriptor);
        }
    }

    //再优化
    if (doOpt) {
        bool needVerify = (gDvm.dexOptMode == OPTIMIZE_MODE_VERIFIED ||
                           gDvm.dexOptMode == OPTIMIZE_MODE_FULL);
        if (!verified && needVerify) {
            ALOGV("DexOpt: not optimizing '%s': not verified",
                classDescriptor);
        } else {
            dvmOptimizeClass(clazz, false);

            /* set the flag whether or not we actually changed anything */
            ((DexClassDef*)pClassDef)->accessFlags |= CLASS_ISOPTIMIZED;
        }
    }
}
```

这里将验证和优化分开
- dvmVerifyClass(clazz)
- dvmOptimizeClass(clazz, false)

首先是验证`dvmVerifyClass()`，验证`directMethod`和`virtualMethod`
```
bool dvmVerifyClass(ClassObject* clazz)
{
    int i;

    if (dvmIsClassVerified(clazz)) {
        ALOGD("Ignoring duplicate verify attempt on %s", clazz->descriptor);
        return true;
    }

    for (i = 0; i < clazz->directMethodCount; i++) {
        if (!verifyMethod(&clazz->directMethods[i])) {
            LOG_VFY("Verifier rejected class %s", clazz->descriptor);
            return false;
        }
    }
    for (i = 0; i < clazz->virtualMethodCount; i++) {
        if (!verifyMethod(&clazz->virtualMethods[i])) {
            LOG_VFY("Verifier rejected class %s", clazz->descriptor);
            return false;
        }
    }

    return true;
}
```

然后是优化，跟上面类似，跟进去就复杂了
```
void dvmOptimizeClass(ClassObject* clazz, bool essentialOnly)
{
    int i;

    for (i = 0; i < clazz->directMethodCount; i++) {
        optimizeMethod(&clazz->directMethods[i], essentialOnly);
    }
    for (i = 0; i < clazz->virtualMethodCount; i++) {
        optimizeMethod(&clazz->virtualMethods[i], essentialOnly);
    }
}
```

终于讲完了第一个函数

第二个是`dvmDexFileOpenPartial()`，又见到了这个函数，参考上面

`dvmGenerateRegisterMaps()`这个函数很有必要专门写一篇文章来详细讲，用于精确回收的

`writeDependencies()`写依赖段

`writeOptData()`写一些优化数据段

## 0x02 映射到内存中
现在仅剩最后一个映射功能的函数，但是并不是最后一层函数
```
dvmDexFileOpenFromFd(optFd, &pDvmDex)
```

此时ODex文件已经填充满了数据，`*pDvmDex`也差不多了

接下来就是映射到内存中
```
int dvmDexFileOpenFromFd(int fd, DvmDex** ppDvmDex)
{
    DvmDex* pDvmDex;
    DexFile* pDexFile;
    MemMapping memMap;
    int parseFlags = kDexParseDefault;
    int result = -1;

    if (gDvm.verifyDexChecksum)
        parseFlags |= kDexParseVerifyChecksum;

    //将文件指针置为0
    if (lseek(fd, 0, SEEK_SET) < 0) {
        ALOGE("lseek rewind failed");
        goto bail;
    }

    //映射ODex文件，此时的ODex文件已经填充的比较完整
    //先映射，再修改为只读
    if (sysMapFileInShmemWritableReadOnly(fd, &memMap) != 0) {
        ALOGE("Unable to map file");
        goto bail;
    }

    //解析出*pDexFile
    pDexFile = dexFileParse((u1*)memMap.addr, memMap.length, parseFlags);
    if (pDexFile == NULL) {
        ALOGE("DEX parse failed");
        sysReleaseShmem(&memMap);
        goto bail;
    }

    //填充*pDvmDex
    pDvmDex = allocateAuxStructures(pDexFile);
    if (pDvmDex == NULL) {
        dexFileFree(pDexFile);
        sysReleaseShmem(&memMap);
        goto bail;
    }

    //把这篇内存的数据填充到*pDvmDex->memMap中
    sysCopyMap(&pDvmDex->memMap, &memMap);
    pDvmDex->isMappedReadOnly = true;
    *ppDvmDex = pDvmDex;
    result = 0;

bail:
    return result;
}
```

其中就这个函数可以分析一下
```
sysMapFileInShmemWritableReadOnly(fd, &memMap)
```

跟进去，映射数据，然后修改属性为只读
```
int sysMapFileInShmemWritableReadOnly(int fd, MemMapping* pMap)
{
#ifdef HAVE_POSIX_FILEMAP
    off_t start;
    size_t length;
    void* memPtr;

    assert(pMap != NULL);

    if (getFileStartAndLength(fd, &start, &length) < 0)
        return -1;

    memPtr = mmap(NULL, length, PROT_READ | PROT_WRITE, MAP_FILE | MAP_PRIVATE,
            fd, start);
    if (memPtr == MAP_FAILED) {
        ALOGW("mmap(%d, R/W, FILE|PRIVATE, %d, %d) failed: %s", (int) length,
            fd, (int) start, strerror(errno));
        return -1;
    }
    if (mprotect(memPtr, length, PROT_READ) < 0) {
        int err = errno;
        ALOGV("mprotect(%p, %d, PROT_READ) failed: %s",
            memPtr, length, strerror(err));
        ALOGD("mprotect(RO) failed (%d), file will remain read-write", err);
    }

    pMap->baseAddr = pMap->addr = memPtr;
    pMap->baseLength = pMap->length = length;

    return 0;
#else
    return sysFakeMapFile(fd, pMap);
#endif
}
```

这一路到底终于扯完，该回到最开始那个加载Dex的地方了，这里把返回值拿来填充`*pDexOrJar`，第二个分支用于加载Jar文件，多了个解压的流程而已
```
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

if (pDexOrJar != NULL) {
    pDexOrJar->fileName = sourceName;
    addToDexFileTable(pDexOrJar);
} else {
    free(sourceName);
}

free(outputName);
RETURN_PTR(pDexOrJar);
```

倒是最后的`addToDexFileTable()`可以顺便分析一下，发现这是一个关于Dex文件的哈希表，而哈希值就是`pDexOrJar`指针低32位比特的值

中间有个对比，应该由两个作用，第一是判断是否出错，第二是判断是否已经添加过
```
static void addToDexFileTable(DexOrJar* pDexOrJar) {
    u4 hash = (u4) pDexOrJar;
    void* result;

    dvmHashTableLock(gDvm.userDexFiles);
    result = dvmHashTableLookup(gDvm.userDexFiles, hash, pDexOrJar,
            hashcmpDexOrJar, true);
    dvmHashTableUnlock(gDvm.userDexFiles);

    if (result != pDexOrJar) {
        ALOGE("Pointer has already been added?");
        dvmAbort();
    }

    pDexOrJar->okayToFree = true;
}
```