//
// Created by wangz on 2018/3/6 0006.
//

#include <jni.h>
#include <string>
#include <unistd.h>
#include <dlfcn.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <sys/stat.h>
#include <sys/types.h>
#include <android/log.h>
#include <assert.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>

#include "DexFile.h"
#include "Common.h"

#ifndef PROTECTAPK_NATIVE_LIB_H
#define PROTECTAPK_NATIVE_LIB_H

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "toT0C",__VA_ARGS__) // 定义LOGI类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "toT0C" ,__VA_ARGS__) // 定义LOGE类型

#endif //PROTECTAPK_NATIVE_LIB_H

