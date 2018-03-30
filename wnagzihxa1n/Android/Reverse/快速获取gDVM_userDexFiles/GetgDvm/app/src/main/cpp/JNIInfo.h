//
// Created by wangz on 2018/3/9 0009.
//

#ifndef PROTECTAPK_JNIINFO_H
#define PROTECTAPK_JNIINFO_H


#include "native-lib.h"

namespace JNIInfo {
    std::string strPackageName;
    std::string cachePath;
    std::string cachefilePath;
    std::string cachefileOpt;

    bool isHook = true;

    jclass clazz_android_content_Context;
    jclass clazz_android_content_ContextWrapper;
    jclass clazz_dalvik_system_DexPathList$Element;
    jclass clazz_dalvik_system_BaseDexClassLoader;
    jclass clazz_dalvik_system_DexPathList;
    jclass clazz_dalvik_system_DexFile;
    jclass clazz_com_wnagzihxa1n_protectapk_StupApplication;

    jmethodID methodID_super_attachBaseContext;
    jmethodID methodID_getPackageName;
    jmethodID methodID_getClassLoader;
    jmethodID methodID_element_init;
    jmethodID methodID_getAssets;

    jfieldID filedID_pathList;
    jfieldID jfieldID_dexElements;
    jfieldID fieldID_mCookie;


    bool initJavaClass(JNIEnv* env, jobject jobj) {
        clazz_android_content_Context = env->FindClass("android/content/Context");
        clazz_android_content_ContextWrapper = env->FindClass("android/content/ContextWrapper");
        clazz_dalvik_system_DexPathList$Element = env->FindClass("dalvik/system/DexPathList$Element");
        clazz_dalvik_system_BaseDexClassLoader = env->FindClass("dalvik/system/BaseDexClassLoader");
        clazz_dalvik_system_DexPathList = env->FindClass("dalvik/system/DexPathList");
        clazz_dalvik_system_DexFile = env->FindClass("dalvik/system/DexFile");
        clazz_com_wnagzihxa1n_protectapk_StupApplication = env->FindClass("com/wnagzihxa1n/protectapk/StupApplication");

        return JNI_TRUE;
    }


    bool initJavaMethodID(JNIEnv* env, jobject jobj) {
        methodID_super_attachBaseContext = env->GetMethodID(clazz_android_content_ContextWrapper,
                                                                      "attachBaseContext",
                                                                      "(Landroid/content/Context;)V");
        methodID_getPackageName = env->GetMethodID(clazz_android_content_Context,
                                                             "getPackageName",
                                                             "()Ljava/lang/String;");
        methodID_getClassLoader = env->GetMethodID(clazz_android_content_Context,
                                                             "getClassLoader",
                                                             "()Ljava/lang/ClassLoader;");
        methodID_element_init = env->GetMethodID(JNIInfo::clazz_dalvik_system_DexPathList$Element,
                                                           "<init>",
                                                           "(Ljava/io/File;ZLjava/io/File;Ldalvik/system/DexFile;)V");
        methodID_getAssets = env->GetMethodID(clazz_com_wnagzihxa1n_protectapk_StupApplication,
                                                        "getAssets",
                                                        "()Landroid/content/res/AssetManager;");

        return JNI_TRUE;
    }


    bool initJavaFieldID(JNIEnv* env, jobject jobj) {
        filedID_pathList = env->GetFieldID(clazz_dalvik_system_BaseDexClassLoader,
                                                    "pathList",
                                                    "Ldalvik/system/DexPathList;");
        jfieldID_dexElements = env->GetFieldID(clazz_dalvik_system_DexPathList,
                                                        "dexElements",
                                                        "[Ldalvik/system/DexPathList$Element;");
        fieldID_mCookie = env->GetFieldID(clazz_dalvik_system_DexFile,
                                          "mCookie",
                                          "I");
        return JNI_TRUE;
    }


    bool initApplicationInfo(JNIEnv* env, jobject jobj, jobject context) {
        jstring jstr_PackageName = (jstring) env->CallObjectMethod(context, methodID_getPackageName);
        const char* pPackageName = env->GetStringUTFChars(jstr_PackageName, 0);
//        LOGI("---> Get packageName : %s", pPackageName);

        strPackageName = pPackageName;
        cachePath = "/data/data/" + strPackageName;
//        LOGI("---> cachePath : %s", cachePath.c_str());

        cachefilePath = cachePath + "/encryptedDex.dex";
        cachefileOpt = cachePath + "/encryptedDex.odex";
//        LOGI("---> cachefilePath : %s", cachefilePath.c_str());
//        LOGI("---> cachefileOpt : %s", cachefileOpt.c_str());

        return JNI_TRUE;
    }


    bool initJNIInfo(JNIEnv* env, jobject jobj, jobject context) {
        initJavaClass(env, jobj);
        initApplicationInfo(env, jobj, context);
        initJavaMethodID(env, jobj);
        initJavaFieldID(env, jobj);

        return JNI_TRUE;
    }
}


#endif //PROTECTAPK_JNIINFO_H
