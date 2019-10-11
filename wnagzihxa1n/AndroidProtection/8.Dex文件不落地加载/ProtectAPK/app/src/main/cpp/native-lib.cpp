#include "native-lib.h"
#include "DvmLoader.h"
#include "JAssets.h"
#include "JNIInfo.h"

#ifdef  __cplusplus
    extern "C" {
#endif


JNIEXPORT void JNICALL native_onCreate(JNIEnv* env, jobject jobj) {
//    LOGI("---> In onCreate()");
    return;
}


JNIEXPORT void JNICALL native_attachBaseContext(JNIEnv* env, jobject jobj, jobject context) {
//    LOGI("---> In attachBaseContext()");

    JNIInfo::initJavaClass(env, jobj);
    JNIInfo::initJavaMethodID(env, jobj);
    JNIInfo::initJavaFieldID(env, jobj);
    JNIInfo::initJNIInfo(env, jobj, context);

    // super.attachBaseContext(ctx);
    env->CallNonvirtualVoidMethod(jobj,
                                  JNIInfo::clazz_android_content_ContextWrapper,
                                  JNIInfo::methodID_super_attachBaseContext,
                                  context);

    JAssets::initJAssetsEnv(env, jobj);

    //#####################################################################
//    JAssets::releaseDexFileIntoInstallDirectory(env, jobj, JNIInfo::cachefilePath.c_str());
//    DvmLoader::loadDexFileFromFile(env, jobj, context, JNIInfo::clazz_android_content_Context, JNIInfo::cachefilePath, JNIInfo::cachefileOpt);

    //#####################################################################
    u1* pDex = nullptr;
    u4  pDexLen = 0;
    JAssets::releaseDexFileIntoMemory(env, jobj, (const u1 *&) pDex, pDexLen);
    LOGI("%s", pDex);
    DvmLoader::loadDexFileFromMemory(env, jobj, context, pDex, pDexLen);

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

    jclass clazz = env->FindClass("com/wnagzihxa1n/protectapk/StupApplication");
    if (clazz != nullptr) {
        if (env->RegisterNatives(clazz, methods, 2) >= 0) {
//            LOGI("---> Register methods successfully");
            return JNI_VERSION_1_6;
        } else {
//            LOGE("---> Register methods failed");
            return -1;
        }
    }

    return -1;
}

#ifdef  __cplusplus
    }
#endif  /* end of __cplusplus */

