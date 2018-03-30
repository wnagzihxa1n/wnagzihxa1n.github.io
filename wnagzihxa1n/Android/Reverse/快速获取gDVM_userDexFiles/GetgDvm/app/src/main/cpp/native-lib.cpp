#include "native-lib.h"
#include "JNIInfo.h"
#include "Android/mAndroid.h"
#include "Hook/Hook.h"

#ifdef  __cplusplus
    extern "C" {
#endif

static void (*olddvmHashTableFree)(HashTable* pHashTable);

HashTable *userDexFiles = nullptr;

static void newfndvmHashTableFree(HashTable* pHashTable) {
    if (JNIInfo::isHook) {
        userDexFiles = pHashTable;
//        LOGI("Get----------------------->userDexFiles = %x", userDexFiles);
        JNIInfo::isHook = false;
        return;
    } else {
        return olddvmHashTableFree(pHashTable);
    }
}

JNIEXPORT void JNICALL Java_com_wnagzihxa1n_getgdvm_MainActivity_getgDvm(JNIEnv* env, jobject jobj) {
    auto libdvm = dlopen("libdvm.so", RTLD_NOW);
    auto fndvmInternalNativeShutdown = (void (*)(void)) dlsym(libdvm, "_Z25dvmInternalNativeShutdownv");

    Hook::hookMethod(libdvm, "_Z16dvmHashTableFreeP9HashTable", (void*) newfndvmHashTableFree, (void**)&olddvmHashTableFree);
    fndvmInternalNativeShutdown();

    LOGI("gDvm.userDexFiles = %p", userDexFiles);
}

#ifdef  __cplusplus
    }
#endif  /* end of __cplusplus */

