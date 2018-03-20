//
// Created by wangz on 2018/3/9 0009.
//

#include "native-lib.h"
#include "Android/Common.h"

#ifndef PROTECTAPK_JASSETS_H
#define PROTECTAPK_JASSETS_H


namespace JAssets {

    AAssetManager* assetManager = nullptr;

    // 初始化JAssets环境
    bool initJAssetsEnv(JNIEnv* env, jobject jobj) {
        jobject jobj_assetManager = env->CallObjectMethod(jobj, JNIInfo::methodID_getAssets);
        assetManager = AAssetManager_fromJava(env, jobj_assetManager);

        if (assetManager == nullptr) {
//            LOGE("Not get assetManager");
            return JNI_FALSE;
        }
        return JNI_TRUE;
    }


    // 从Assets文件夹中释放文件到/data/data/包名/路径下
    bool releaseDexFileIntoInstallDirectory(JNIEnv* env, jobject jobj, const char* szPath) {
        if (access(szPath, R_OK) && assetManager != nullptr) {
            AAsset* asset = AAssetManager_open(assetManager, "encryptedDex.dex", AASSET_MODE_STREAMING);
            FILE* fp_encryptedDex = nullptr;
            void* buffer = nullptr;
            if (asset) {
                int readCount = 0;
                fp_encryptedDex = fopen(szPath, "w");
                buffer = malloc(1024);
                while ((readCount = AAsset_read(asset, buffer, 1024)) > 0) {
                    fwrite(buffer, (size_t) readCount, 1, fp_encryptedDex);
                }
                free(buffer);
                fclose(fp_encryptedDex);
                AAsset_close(asset);
            } else {
//                LOGE("Not open file encryptedDex.dex");
                return JNI_FALSE;
            }
        }
        return JNI_TRUE;
    }


    bool releaseDexFileIntoMemory(JNIEnv* env, jobject jobj, const u1* &pMem, u4 &len) {
        AAssetManager* aAssetManager;

        jobject jobj_assetManager = env->CallObjectMethod(jobj, JNIInfo::methodID_getAssets);
        aAssetManager = AAssetManager_fromJava(env, jobj_assetManager);

        if (aAssetManager == nullptr) {
//            LOGE("Not get aAssetManager");
            return JNI_FALSE;
        }

        AAsset* aAsset = AAssetManager_open(aAssetManager, "encryptedDex.dex", AASSET_MODE_STREAMING);
        if (aAsset != nullptr) {
            len = (u4) AAsset_getLength(aAsset);
            void* m = mmap(0, len, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANON,  -1, 0);
            if (m != MAP_FAILED) {
                pMem = (const u1 *) m;
                char tmp[1024];
                int readCount = 0;
                auto readPoint = pMem;
                auto restLen = len;
                while ((readCount = AAsset_read(aAsset, tmp, restLen > 1024 ? 1024 : restLen)) > 0) {
                    memcpy((void *) readPoint, tmp, (size_t) readCount);
                    readPoint += readCount;
                    restLen -= readCount;
                }
            }
            AAsset_close(aAsset);
            return m != MAP_FAILED;
        }
        return JNI_FALSE;
    }
}



#endif //PROTECTAPK_JASSETS_H