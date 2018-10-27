//
// Created by wangz on 2018/3/9 0009.
//

#include "native-lib.h"
#include "JNIInfo.h"

#ifndef PROTECTAPK_JNIUTILS_H
#define PROTECTAPK_JNIUTILS_H

namespace JNIUtils {
    bool makeDexElements(JNIEnv* env, jobject classloader, const std::vector<jobject>& jobj_dexFiles) {
//        LOGI("---> In makeDexElements()");

        if (jobj_dexFiles.empty()) {
            return JNI_FALSE;
        }

        jobject jfield_pathList = env->GetObjectField(classloader, JNIInfo::filedID_pathList);
        jobjectArray jfield_dexElements = (jobjectArray) env->GetObjectField(jfield_pathList, JNIInfo::jfieldID_dexElements);

//        if (jfield_dexElements != nullptr) {
//            LOGI("Get dexElements");
//        }

        jint dexElementCount = env->GetArrayLength(jfield_dexElements);
        jint dexElementNewCount = dexElementCount + jobj_dexFiles.size();

        jobjectArray new_jobj_dexElements = env->NewObjectArray(dexElementNewCount,
                                                                JNIInfo::clazz_dalvik_system_DexPathList$Element,
                                                                nullptr);
        for (auto i = 0; i < dexElementCount; i++) {
            env->SetObjectArrayElement(new_jobj_dexElements, i, env->GetObjectArrayElement(jfield_dexElements, i));
        }

        for (auto i = 0; i < jobj_dexFiles.size(); i++) {
            jobject new_dexElement = env->NewObject(JNIInfo::clazz_dalvik_system_DexPathList$Element,
                                                    JNIInfo::methodID_element_init,
                                                    nullptr,
                                                    false,
                                                    nullptr, jobj_dexFiles[i]);
            env->SetObjectArrayElement(new_jobj_dexElements, dexElementCount + i, new_dexElement);
        }

        env->SetObjectField(jfield_pathList, JNIInfo::jfieldID_dexElements, new_jobj_dexElements);

        return JNI_TRUE;
    }
}


#endif //PROTECTAPK_JNIUTILS_H