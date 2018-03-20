//
// Created by wangz on 2018/3/9 0009.
//

#include "native-lib.h"
#include "JNIUtils.h"
#include "Android/mAndroid.h"
#include "JNIInfo.h"
#include "Hook/Hook.h"

#ifndef PROTECTAPK_DVMLOADER_H
#define PROTECTAPK_DVMLOADER_H


namespace DvmLoader {
    bool loadDexFileFromFile(JNIEnv* env, jobject jobj, jobject context, jclass clazz_android_content_Context,
               std::string cachefilePath, std::string cachefileOpt) {

        auto libdvm = dlopen("libdvm.so", RTLD_NOW);
        auto dvm_dalvik_system_DexFile = (JNINativeMethod *) dlsym(libdvm, "dvm_dalvik_system_DexFile");
        void (*fnOpenDexFileNative)(const u4* args, JValue* pResult) = nullptr;
        for (auto p = dvm_dalvik_system_DexFile; p->fnPtr != nullptr; p++) {
            if (strcmp(p->name, "openDexFileNative") == 0
                && strcmp(p->signature, "(Ljava/lang/String;Ljava/lang/String;I)I") == 0) {
                fnOpenDexFileNative = (void (*)(const u4 *, JValue *)) p->fnPtr;
                break;
            }
        }

        DexOrJar* pDexOrJar = nullptr;
        if (fnOpenDexFileNative != nullptr) {
//            LOGI("Found fnOpenDexFileNative");
            auto fndvmCreateStringFromCstr = (void* (*)(const char* utf8Str)) dlsym(libdvm, "_Z23dvmCreateStringFromCstrPKc");
            u4 args[2];
            args[0] = static_cast<u4>(reinterpret_cast<uintptr_t>(fndvmCreateStringFromCstr(cachefilePath.c_str())));
            args[1] = static_cast<u4>(reinterpret_cast<uintptr_t>(fndvmCreateStringFromCstr(cachefileOpt.c_str())));
            JValue result;
            fnOpenDexFileNative(args, &result);
            pDexOrJar = (DexOrJar*) result.l;
        }

        jobject jobj_dexFile = env->AllocObject(JNIInfo::clazz_dalvik_system_DexFile);
        env->SetIntField(jobj_dexFile, JNIInfo::fieldID_mCookie, static_cast<jint>(reinterpret_cast<uintptr_t>(pDexOrJar)));

        if (jobj_dexFile != nullptr) {
//            LOGI("---> Load dex file successfully");

            jobject jobj_classloader = env->CallObjectMethod(context, JNIInfo::methodID_getClassLoader);
            std::vector<jobject>jobj_dexFiles;
            jobj_dexFiles.push_back(jobj_dexFile);

            if (JNIUtils::makeDexElements(env, jobj_classloader, jobj_dexFiles) == JNI_TRUE) {
//                LOGI("---> Bingo");
                dlclose(libdvm);
                return JNI_TRUE;
            } else {
//                LOGE("---> Add the jobj_dexFile to array failed");
                return JNI_FALSE;
            }
        } else {
//            LOGE("---> Load file \"encryptedDex\" faied");
            return JNI_FALSE;
        }
        return JNI_FALSE;
    }

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

    static void addToDexFileTable(DexOrJar* pDexOrJar) {
        u4 hash = (u4) pDexOrJar;
        void* result;

        auto libdvm = dlopen("libdvm.so", RTLD_NOW);
        auto fndvmHashTableLock = (void (*)(HashTable* pHashTable)) dlsym(libdvm, "_Z16dvmHashTableLockP9HashTable");
        auto fndvmHashTableUnlock = (void (*)(HashTable* pHashTable)) dlsym(libdvm, "_Z18dvmHashTableUnlockP9HashTable");
        auto fndvmHashTableLookup = (void* (*)(HashTable* pHashTable, u4 itemHash, void* item, HashCompareFunc cmpFunc, bool doAdd)) dlsym(libdvm, "_Z18dvmHashTableLookupP9HashTablejPvPFiPKvS3_Eb");

// ###############Hook dvmInternalNativeShutdown() and get gDvm.userDexFiles
        auto fndvmInternalNativeShutdown = (void (*)(void)) dlsym(libdvm, "_Z25dvmInternalNativeShutdownv");

        Hook::hookMethod(libdvm, "_Z16dvmHashTableFreeP9HashTable", (void*) newfndvmHashTableFree, (void**)&olddvmHashTableFree);
        fndvmInternalNativeShutdown();

// #########################################################################
        fndvmHashTableLock(userDexFiles);
        result = fndvmHashTableLookup(userDexFiles, hash, pDexOrJar, hashcmpDexOrJar, true);
        fndvmHashTableUnlock(userDexFiles);

        if (result != pDexOrJar) {
            LOGE("Pointer has already been added?");
        }

        pDexOrJar->okayToFree = true;
        dlclose(libdvm);
    }

    bool loadDexFileFromMemory(JNIEnv* env, jobject jobj, jobject context, const u1* pMem, u4 len) {
        auto libdvm = dlopen("libdvm.so", RTLD_NOW);

        auto fndvmDexFileOpenPartial = (int (*)(const void* addr, int len, DvmDex** ppDvmDex)) dlsym(libdvm, "_Z21dvmDexFileOpenPartialPKviPP6DvmDex");
        auto fndexCreateClassLookup = (DexClassLookup* (*)(DexFile* pDexFile)) dlsym(libdvm, "_Z20dexCreateClassLookupP7DexFile");

        DvmDex* pDvmDex = nullptr;
        if (fndvmDexFileOpenPartial(pMem, len, &pDvmDex) != 0) {
            return JNI_FALSE;
        }
//        LOGI("%s", pDvmDex->pHeader->magic);

        DexClassLookup* pClassLookup = fndexCreateClassLookup(pDvmDex->pDexFile);
        if (pClassLookup == nullptr) {
            return JNI_FALSE;
        }
        pDvmDex->pDexFile->pClassLookup = pClassLookup;

        RawDexFile* pRawDexFile = (RawDexFile*) calloc(1, sizeof(RawDexFile));
        pRawDexFile->pDvmDex = pDvmDex;
        pRawDexFile->cacheFileName = nullptr;

        DexOrJar* pDexOrJar = (DexOrJar*) malloc(sizeof(DexOrJar));
        pDexOrJar->isDex = true;
        pDexOrJar->pRawDexFile = pRawDexFile;
        pDexOrJar->pDexMemory = (u1 *) pMem;
        pDexOrJar->fileName = strdup("<memory>"); // Needs to be free()able.
        addToDexFileTable(pDexOrJar);

        jobject jobj_dexFile = env->AllocObject(JNIInfo::clazz_dalvik_system_DexFile);
        env->SetIntField(jobj_dexFile, JNIInfo::fieldID_mCookie, static_cast<jint>(reinterpret_cast<uintptr_t>(pDexOrJar)));

        if (jobj_dexFile != nullptr) {
//            LOGI("---> Load dex file successfully");
            jobject jobj_classloader = env->CallObjectMethod(context, JNIInfo::methodID_getClassLoader);
            std::vector<jobject>jobj_dexFiles;
            jobj_dexFiles.push_back(jobj_dexFile);

            if (JNIUtils::makeDexElements(env, jobj_classloader, jobj_dexFiles) == JNI_TRUE) {
//                LOGI("---> Bingo");
                dlclose(libdvm);
                return JNI_TRUE;
            } else {
//                LOGE("---> Add the jobj_dexFile to array failed");
                return JNI_FALSE;
            }
        } else {
//            LOGE("---> Load file \"encryptedDex\" faied");
            return JNI_FALSE;
        }
        return JNI_FALSE;
    }
}


#endif //PROTECTAPK_DVMLOADER_H


























