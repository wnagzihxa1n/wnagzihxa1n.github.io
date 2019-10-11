#include "native-lib.h"

#ifdef  __cplusplus
    extern "C" {
#endif

bool extractDexFile(JNIEnv* env, jobject jobj, const char* szPath) {
    AAssetManager* assetManager;
    if (access(szPath, R_OK)) {
        jclass clazz_StupApplication = env->GetObjectClass(jobj);
        jmethodID methodID_getAssets = env->GetMethodID(clazz_StupApplication,
                                                        "getAssets",
                                                        "()Landroid/content/res/AssetManager;");
        jobject jobj_assetManager = env->CallObjectMethod(jobj, methodID_getAssets);
        assetManager = AAssetManager_fromJava(env, jobj_assetManager);

        if (assetManager == nullptr) {
            LOGE("Not get assetManager");
            return JNI_FALSE;
        }

        AAsset* asset = AAssetManager_open(assetManager, "encryptedDex.dex", AASSET_MODE_STREAMING);
        FILE* fp_encryptedDex = nullptr;
        void* buffer = nullptr;
        if (asset) {
            int readCount = 0;
            fp_encryptedDex = fopen(szPath, "w");
            buffer = malloc(1024);
            while ((readCount = AAsset_read(asset, buffer, 1024)) > 0) {
                fwrite(buffer, readCount, 1, fp_encryptedDex);
            }
            free(buffer);
            fclose(fp_encryptedDex);
            AAsset_close(asset);
        } else {
            LOGE("Not open file encryptedDex.dex");
            return JNI_FALSE;
        }
    }
    return JNI_TRUE;
}

bool makeDexElements(JNIEnv* env, jobject classloader, const std::vector<jobject>& jobj_dexFiles) {
    LOGI("---> In makeDexElements()");

    if (jobj_dexFiles.empty()) {
        return JNI_FALSE;
    }

    jclass clazz_dalvik_system_BaseDexClassLoader = env->FindClass("dalvik/system/BaseDexClassLoader");
    jfieldID filedID_pathList = env->GetFieldID(clazz_dalvik_system_BaseDexClassLoader,
                                                "pathList",
                                                "Ldalvik/system/DexPathList;");
    jobject jfield_pathList = env->GetObjectField(classloader, filedID_pathList);

    jclass clazz_dalvik_system_DexPathList = env->FindClass("dalvik/system/DexPathList");
    jfieldID jfieldID_dexElements = env->GetFieldID(clazz_dalvik_system_DexPathList,
                                                    "dexElements",
                                                    "[Ldalvik/system/DexPathList$Element;");
    jobjectArray jfield_dexElements = (jobjectArray) env->GetObjectField(jfield_pathList, jfieldID_dexElements);

    if (jfield_dexElements != nullptr ) {
        LOGI("Get dexElements");
    }

    jint dexElementCount = env->GetArrayLength(jfield_dexElements);
    jint dexElementNewCount = dexElementCount + jobj_dexFiles.size();

    jclass clazz_dalvik_system_DexPathList$Element = env->FindClass("dalvik/system/DexPathList$Element");
    jobjectArray new_jobj_dexElements = env->NewObjectArray(dexElementNewCount,
                                                            clazz_dalvik_system_DexPathList$Element,
                                                            nullptr);
    for (auto i = 0; i < dexElementCount; i++) {
        env->SetObjectArrayElement(new_jobj_dexElements, i, env->GetObjectArrayElement(jfield_dexElements, i));
    }

    jmethodID methodID_element_init = env->GetMethodID(clazz_dalvik_system_DexPathList$Element,
                                                       "<init>",
                                                       "(Ljava/io/File;ZLjava/io/File;Ldalvik/system/DexFile;)V");
    for (auto i = 0; i < jobj_dexFiles.size(); i++) {
        jobject new_dexElement = env->NewObject(clazz_dalvik_system_DexPathList$Element,
                                                methodID_element_init,
                                                nullptr,
                                                false,
                                                nullptr, jobj_dexFiles[i]);
        env->SetObjectArrayElement(new_jobj_dexElements, dexElementCount + i, new_dexElement);
    }

    env->SetObjectField(jfield_pathList, jfieldID_dexElements, new_jobj_dexElements);

    return JNI_TRUE;
}


JNIEXPORT void JNICALL native_onCreate(JNIEnv* env, jobject jobj) {
//    LOGI("---> In onCreate()");
    return;
}


JNIEXPORT void JNICALL native_attachBaseContext(JNIEnv* env, jobject jobj, jobject ctx) {
//    LOGI("---> In attachBaseContext()");

    // super.attachBaseContext(ctx);
    jclass clazz_android_content_ContextWrapper = env->FindClass("android/content/ContextWrapper");
    jmethodID methodID_super_attachBaseContext = env->GetMethodID(clazz_android_content_ContextWrapper,
                                                                  "attachBaseContext",
                                                                  "(Landroid/content/Context;)V");
    env->CallNonvirtualVoidMethod(jobj,
                                  clazz_android_content_ContextWrapper,
                                  methodID_super_attachBaseContext,
                                  ctx);

    jclass clazz_android_content_Context = env->FindClass("android/content/Context");
    jmethodID methodID_getPackageName = env->GetMethodID(clazz_android_content_Context,
                                                         "getPackageName",
                                                         "()Ljava/lang/String;");
    jstring jstr_PackageName = (jstring) env->CallObjectMethod(ctx, methodID_getPackageName);
    const char* pPackageName = env->GetStringUTFChars(jstr_PackageName, 0);
    LOGI("---> Get packageName : %s", pPackageName);

    std::string strPackageName = pPackageName;
    std::string cachePath = "/data/data/" + strPackageName;
    LOGI("---> cachePath : %s", cachePath.c_str());

    std::string cachefilePath = cachePath + "/encryptedDex.dex";
    std::string cachefileOpt = cachePath + "/encryptedDex.odex";
    LOGI("---> cachefilePath : %s", cachefilePath.c_str());
    LOGI("---> cachefileOpt : %s", cachefileOpt.c_str());

    extractDexFile(env, jobj, cachefilePath.c_str());

    jstring jstr_cachefilePath = env->NewStringUTF(cachefilePath.c_str());
    jstring jstr_cachefileOpt = env->NewStringUTF(cachefileOpt.c_str());

    jclass clazz_DexFile = env->FindClass("dalvik/system/DexFile");
    if (clazz_DexFile != nullptr) {
        LOGI("---> Find Class dalvik.system.DexFile");
    }

    jmethodID methodID_loadDex = env->GetStaticMethodID(clazz_DexFile,
                                            "loadDex",
                                            "(Ljava/lang/String;Ljava/lang/String;I)Ldalvik/system/DexFile;");
    if (methodID_loadDex != nullptr) {
        LOGI("---> Get methodID_loadDex");
    }

    auto jobj_dexFile = env->CallStaticObjectMethod(clazz_DexFile,
                                                   methodID_loadDex,
                                                   jstr_cachefilePath,
                                                   jstr_cachefileOpt,
                                                   false);
    if (jobj_dexFile != nullptr) {
        LOGI("---> Load dex file successfully");

        jmethodID methodID_getClassLoader = env->GetMethodID(clazz_android_content_Context,
                                                             "getClassLoader",
                                                             "()Ljava/lang/ClassLoader;");
        jobject jobj_classloader = env->CallObjectMethod(ctx, methodID_getClassLoader);
        std::vector<jobject>jobj_dexFiles;
        jobj_dexFiles.push_back(jobj_dexFile);

        if (makeDexElements(env, jobj_classloader, jobj_dexFiles) == JNI_TRUE) {
            LOGI("---> Bingo");
        } else {
            LOGE("---> Add the jobj_dexFile to array failed");
        }
    } else {
        LOGE("---> Load file \"encryptedDex\" faied");
    }

    return;
}


static JNINativeMethod methods[] = {
        {"attachBaseContext", "(Landroid/content/Context;)V", (void *) native_attachBaseContext},
        {"onCreate", "()V", (void *) native_onCreate},
};


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reser) {
//    LOGI("---> In JNI_OnLoad()");
    JNIEnv* env = nullptr;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    auto clazz = env->FindClass("com/wnagzihxa1n/protectapk/StupApplication");
    if (clazz != nullptr) {
        if (env->RegisterNatives(clazz, methods, 2) >= 0) {
//            LOGI("---> Register methods successfully");
            return JNI_VERSION_1_6;
        } else {
            LOGE("---> Register methods failed");
        }
    }

    return JNI_FALSE;
}

#ifdef  __cplusplus
    }
#endif  /* end of __cplusplus */






























