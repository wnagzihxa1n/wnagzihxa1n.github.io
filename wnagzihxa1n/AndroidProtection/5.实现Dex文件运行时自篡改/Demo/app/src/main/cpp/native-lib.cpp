#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <math.h>
#include <string.h>
#include <sys/mman.h>
#include <assert.h>
#include <dlfcn.h>
#include <exception>
#include <inttypes.h>
#include "DexFile.h"

#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, "toT0C", __VA_ARGS__))

static char *getString(const DexFile *dexFile, int id)
{
    return (char *)(dexFile->baseAddr + dexFile->pStringIds[id].stringDataOff + 1);
}

static int getTypeIdStringId(const DexFile *dexFile, int id)
{
    const DexTypeId *typeId = dexFile->pTypeIds;
    return typeId[id].descriptorIdx;
}
#define getTypeIdString(dexFile, id) getString((dexFile), getTypeIdStringId((dexFile), (id)))

static DexFile gDexFile;

static void getModuleInfo(const char* moduleName, uint64_t* moduleBase, size_t* moduleSize)
{
    FILE* fp_dex = NULL;
    char filename[1024] = "/proc/self/maps";
    char line[1024];
    char* temp,* baseStart,* baseEnd;

    fp_dex = fopen(filename, "r");
    if (fp_dex != NULL)
    {
        while (fgets(line, sizeof(line), fp_dex))
        {
            if (strstr(line, moduleName))
            {
                LOGE("Current line : %s\n", line);
                temp = strtok(line, " ");
                baseStart = strtok(temp, "-");
                baseEnd = strtok(NULL, "-");
                LOGE("baseStart = 0x%s, baseEnd = 0x%s\n", baseStart, baseEnd);
                *moduleBase = strtoul(baseStart, NULL, 16);
                *moduleSize = strtoul(baseEnd, NULL, 16) - strtoul(baseStart, NULL, 16);
                break;
            }
        }
    }
    fclose(fp_dex);
}

static bool checkLegalODexFile(uint64_t moduleBase)
{
    void* optBase = (void*) moduleBase;
    DexOptHeader* dexOptHeader = (DexOptHeader*) (optBase);
    DexHeader* dexHeader = (DexHeader*) ((u4)optBase + sizeof(DexOptHeader));
    if (!strcmp((char*) dexOptHeader->magic, "dey\n036\0")
        && !strcmp((char*) dexHeader->magic, "dex\n035\0"))
    {
        return true;
    }
    return false;
}

static void asciify(char* out, const unsigned char* data, size_t len)
{
    while (len--) {
        if (*data < 0x20) {
            switch (*data) {
                case '\0':
                    *out++ = '\\';
                    *out++ = '0';
                    break;
                case '\n':
                    *out++ = '\\';
                    *out++ = 'n';
                    break;
                default:
                    *out++ = '.';
                    break;
            }
        } else if (*data >= 0x80) {
            *out++ = '?';
        } else {
            *out++ = *data;
        }
        data++;
    }
    *out = '\0';
}

static const DexClassDef *dexGetClassDef(const DexFile *pDexFile, u4 idx)
{
    assert(idx < pDexFile->pHeader->classDefsSize);
    return &pDexFile->pClassDefs[idx];
}

const u1* dexGetClassData(const DexFile* pDexFile, const DexClassDef* pClassDef)
{
    if (pClassDef->classDataOff == 0)
        return NULL;
    return (const u1*) (pDexFile->baseAddr + pClassDef->classDataOff);
}

static int readUnsignedLeb128(const u1 **pStream)
{
    const u1 *ptr = *pStream;
    int result = *(ptr++);

    if (result > 0x7f)
    {
        int cur = *(ptr++);
        result = (result & 0x7f) | ((cur & 0x7f) << 7);
        if (cur > 0x7f)
        {
            cur = *(ptr++);
            result |= (cur & 0x7f) << 14;
            if (cur > 0x7f)
            {
                cur = *(ptr++);
                result |= (cur & 0x7f) << 21;
                if (cur > 0x7f)
                {
                    /*
                     * Note: We don't check to see if cur is out of
                     * range here, meaning we tolerate garbage in the
                     * high four-order bits.
                     */
                    cur = *(ptr++);
                    result |= cur << 28;
                }
            }
        }
    }

    *pStream = ptr;
    return result;
}

static int readAndVerifyUnsignedLeb128(const u1 **pStream, const u1 *limit, bool *okay)
{
    const u1 *ptr = *pStream;
    int result = readUnsignedLeb128(pStream);

    if (((limit != NULL) && (*pStream > limit)) || (((*pStream - ptr) == 5) && (ptr[4] > 0x0f)))
    {
        *okay = false;
    }

    return result;
}

static bool verifyUlebs(const u1 *pData, const u1 *pLimit, u4 count)
{
    bool okay = true;
    u4 i;

    while (okay && (count-- != 0))
    {
        readAndVerifyUnsignedLeb128(&pData, pLimit, &okay);
    }

    return okay;
}

static void dexReadClassDataField(const u1 **pData, DexField *pField, u4 *lastIndex)
{
    u4 index = *lastIndex + readUnsignedLeb128(pData);

    pField->accessFlags = readUnsignedLeb128(pData);
    pField->fieldIdx = index;
    *lastIndex = index;
}

static bool dexReadAndVerifyClassDataField(const u1 **pData, const u1 *pLimit, DexField *pField, u4 *lastIndex)
{
    if (!verifyUlebs(*pData, pLimit, 2))
    {
        return false;
    }

    dexReadClassDataField(pData, pField, lastIndex);
    return true;
}

static void dexReadClassDataMethod(const u1 **pData, DexMethod *pMethod, u4 *lastIndex)
{
    u4 index = *lastIndex + readUnsignedLeb128(pData);

    pMethod->accessFlags = readUnsignedLeb128(pData);
    pMethod->codeOff = readUnsignedLeb128(pData);
    pMethod->methodIdx = index;
    *lastIndex = index;
}

static bool dexReadAndVerifyClassDataMethod(const u1 **pData, const u1 *pLimit, DexMethod *pMethod, u4 *lastIndex)
{
    if (!verifyUlebs(*pData, pLimit, 3))
    {
        return false;
    }

    dexReadClassDataMethod(pData, pMethod, lastIndex);
    return true;
}

static void dexReadClassDataHeader(const u1 **pData, DexClassDataHeader *pHeader)
{
    pHeader->staticFieldsSize = readUnsignedLeb128(pData);
    pHeader->instanceFieldsSize = readUnsignedLeb128(pData);
    pHeader->directMethodsSize = readUnsignedLeb128(pData);
    pHeader->virtualMethodsSize = readUnsignedLeb128(pData);
}

static bool dexReadAndVerifyClassDataHeader(const u1 **pData, const u1 *pLimit, DexClassDataHeader *pHeader)
{
    if (!verifyUlebs(*pData, pLimit, 4))
    {
        return false;
    }

    dexReadClassDataHeader(pData, pHeader);
    return true;
}

static DexClassData *dexReadAndVerifyClassData(const u1 **pData, const u1 *pLimit)
{
    DexClassDataHeader header;
    u4 lastIndex;

    if (*pData == NULL)
    {
        DexClassData *result = (DexClassData *)malloc(sizeof(DexClassData));
        memset(result, 0, sizeof(*result));
        return result;
    }

    if (!dexReadAndVerifyClassDataHeader(pData, pLimit, &header))
    {
        return NULL;
    }

    size_t resultSize = sizeof(DexClassData) +
                        (header.staticFieldsSize * sizeof(DexField)) +
                        (header.instanceFieldsSize * sizeof(DexField)) +
                        (header.directMethodsSize * sizeof(DexMethod)) +
                        (header.virtualMethodsSize * sizeof(DexMethod));

    DexClassData *result = (DexClassData *)malloc(resultSize);
    u1 *ptr = ((u1 *)result) + sizeof(DexClassData);
    bool okay = true;
    u4 i;

    if (result == NULL)
    {
        return NULL;
    }

    result->header = header;

    if (header.staticFieldsSize != 0)
    {
        result->staticFields = (DexField *)ptr;
        ptr += header.staticFieldsSize * sizeof(DexField);
    }
    else
    {
        result->staticFields = NULL;
    }

    if (header.instanceFieldsSize != 0)
    {
        result->instanceFields = (DexField *)ptr;
        ptr += header.instanceFieldsSize * sizeof(DexField);
    }
    else
    {
        result->instanceFields = NULL;
    }

    if (header.directMethodsSize != 0)
    {
        result->directMethods = (DexMethod *)ptr;
        ptr += header.directMethodsSize * sizeof(DexMethod);
    }
    else
    {
        result->directMethods = NULL;
    }

    if (header.virtualMethodsSize != 0)
    {
        result->virtualMethods = (DexMethod *)ptr;
    }
    else
    {
        result->virtualMethods = NULL;
    }

    lastIndex = 0;
    for (i = 0; okay && (i < header.staticFieldsSize); i++)
    {
        okay = dexReadAndVerifyClassDataField(pData, pLimit,
                                              &result->staticFields[i], &lastIndex);
    }

    lastIndex = 0;
    for (i = 0; okay && (i < header.instanceFieldsSize); i++)
    {
        okay = dexReadAndVerifyClassDataField(pData, pLimit,
                                              &result->instanceFields[i], &lastIndex);
    }

    lastIndex = 0;
    for (i = 0; okay && (i < header.directMethodsSize); i++)
    {
        okay = dexReadAndVerifyClassDataMethod(pData, pLimit,
                                               &result->directMethods[i], &lastIndex);
    }

    lastIndex = 0;
    for (i = 0; okay && (i < header.virtualMethodsSize); i++)
    {
        okay = dexReadAndVerifyClassDataMethod(pData, pLimit,
                                               &result->virtualMethods[i], &lastIndex);
    }

    if (!okay)
    {
        free(result);
        return NULL;
    }

    return result;
}

static void dumpDexCode(const DexCode *pCode)
{
    LOGE("      registers     : %d", pCode->registersSize);
    LOGE("      ins           : %d", pCode->insSize);
    LOGE("      outs          : %d", pCode->outsSize);
    LOGE("      insns size    : %ld 16-bit code units", pCode->insnsSize);

    char buffer[256] = {0, 0};
    char tmp[32];

    for (u4 k = 0; k < pCode->insnsSize; k++)
    {
        sprintf(tmp, "%04x ", pCode->insns[k]);
        strcat(buffer, tmp);
        if (k % 8 == 7)
        {
            LOGE("%s", buffer);
            buffer[0] = 0;
        }
    }
    LOGE("%s", buffer);
}

static const DexClassData* getClassData(const DexFile* pDexFile, const char* pClassName)
{
    size_t classDefCount = pDexFile->pHeader->classDefsSize;
    for (size_t classDefIndex = 0; classDefIndex < classDefCount; classDefIndex++)
    {
        const DexClassDef* pClassDef = dexGetClassDef(pDexFile, classDefIndex);
        const u1* pEncodedData = dexGetClassData(pDexFile, pClassDef);
        const DexClassData* pClassData = dexReadAndVerifyClassData(&pEncodedData, NULL);
        char* descriptor = getTypeIdString(pDexFile, pClassDef->classIdx);
        if (strcmp(descriptor, pClassName) == 0)
        {
            LOGE("Found Class %s\n", pClassName);
            return pClassData;
        }
    }
    return NULL;
}

static const DexCode *dexGetCode(const DexFile *pDexFile, const DexMethod *pDexMethod) {
    if (pDexMethod->codeOff == 0)
        return NULL;
    return (const DexCode *) (pDexFile->baseAddr + pDexMethod->codeOff);
}

static const DexCode* getCode(const DexFile* pDexFile, const DexClassData* pClassData, const char* pMethodName)
{
    size_t directMethodSize = pClassData->header.directMethodsSize;
    size_t virtualMethodSize = pClassData->header.virtualMethodsSize;

    for (int directMethodIndex = 0; directMethodIndex < directMethodSize; ++directMethodIndex)
    {
        int idx = pClassData->directMethods[directMethodIndex].methodIdx;
        const DexCode* pCode = dexGetCode(pDexFile, &pClassData->directMethods[directMethodIndex]);
        if (strcmp(getString(pDexFile, pDexFile->pMethodIds[idx].nameIdx), pMethodName) == 0)
        {
            return pCode;
        }
    }

    for (int virtualMethodIndex = 0; virtualMethodIndex < virtualMethodSize; ++virtualMethodIndex)
    {
        int idx = pClassData->virtualMethods[virtualMethodIndex].methodIdx;
        const DexCode* pCode = dexGetCode(pDexFile, &pClassData->virtualMethods[virtualMethodIndex]);
        if (strcmp(getString(pDexFile, pDexFile->pMethodIds[idx].nameIdx), pMethodName) == 0)
        {
            return pCode;
        }
    }
    return NULL;
}



static DexCode* getClassMethodCode(const DexFile* pDexFile, const char* pClassName, const char* pMethodName)
{
    LOGE("Finding %s -> %s\n", pClassName, pMethodName);
    const DexClassData* pClassData = getClassData(pDexFile, pClassName);
    if (pClassData == NULL)
    {
        return NULL;
    }

    DexCode* pCode = (DexCode*) getCode(pDexFile, pClassData, pMethodName);

    if (pCode == NULL)
    {
        return NULL;
    }

    dumpDexCode(pCode);

    return pCode;
}

static void myModifyDexInst(void)
{
    uint64_t moduleBase = 0;
    size_t moduleSize = 0;
    char filename[1024];

    for (int i = 1; i < 3; i++)
    {
        sprintf(filename, "/data/dalvik-cache/data@app@com.wnagzihxa1n.demo-%d.apk@classes.dex", i);
        getModuleInfo(filename, &moduleBase, &moduleSize);
        if (moduleBase != 0)
        {
            break;
        }
    }

    if (moduleBase == 0)
    {
        LOGE("Can't locate the module %s\n", filename);
        return;
    }
    LOGE("moduleBase is : 0x%x, moduleSize is : 0x%x\n", (unsigned int) moduleBase, (unsigned int) moduleSize);

    if (!checkLegalODexFile(moduleBase))
    {
        LOGE("Check ODexFile failed\n");
        return;
    }

    LOGE("Check ODexFile successed\n");
    void* optBase = (void*) moduleBase;
    DexHeader* pHeader = (DexHeader*) ((u4) optBase + sizeof(DexOptHeader));

    gDexFile.pOptHeader = (DexOptHeader*) optBase;
    gDexFile.pHeader    = (DexHeader*) pHeader;
    gDexFile.baseAddr   = (u1*) ((u4) optBase + sizeof(DexOptHeader));
    gDexFile.pStringIds = (DexStringId*) ((u4) gDexFile.baseAddr + pHeader->stringIdsOff);
    gDexFile.pTypeIds   = (DexTypeId*) ((u4) gDexFile.baseAddr + pHeader->typeIdsOff);
    gDexFile.pFieldIds  = (DexFieldId*) ((u4) gDexFile.baseAddr + pHeader->fieldIdsOff);
    gDexFile.pMethodIds = (DexMethodId*) ((u4) gDexFile.baseAddr + pHeader->methodIdsOff);
    gDexFile.pProtoIds  = (DexProtoId*) ((u4) gDexFile.baseAddr + pHeader->protoIdsOff);
    gDexFile.pClassDefs = (DexClassDef*) ((u4) gDexFile.baseAddr + pHeader->classDefsOff);

//    char sanitized[sizeof(pHeader->magic) * 2 + 1];
//    asciify(sanitized, pHeader->magic, sizeof(pHeader->magic));
//    LOGE("pHeader->magic = %s\n", sanitized);
//    LOGE("pHeader->stringIdsOff = 0x%x\n", pHeader->stringIdsOff);
//    LOGE("pHeader->stringIdsSize = 0x%x\n", pHeader->stringIdsSize);


    DexCode* pCode = getClassMethodCode(&gDexFile,
                                        (char*) "Lcom/wnagzihxa1n/demo/MainActivity;",
                                        (char*) "getSum");

    if (pCode == NULL)
    {
        LOGE("Get Method getSum ins failed\n");
        return;
    }

    if (mprotect((void*) moduleBase, moduleSize, PROT_READ | PROT_WRITE | PROT_EXEC) == 0)
    {
        pCode->insns[0] = 0x0091;
        pCode->insns[2] = 0x0190;
        dumpDexCode(pCode);
        mprotect((void*) moduleBase, moduleSize, PROT_READ | PROT_EXEC);
    }
}

extern "C"
jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
//    LOGE("Call JNI_OnLoad()");
    JNIEnv *env = NULL;
    jint result = -1;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return result;
    }
    myModifyDexInst();
    return JNI_VERSION_1_6;
}
