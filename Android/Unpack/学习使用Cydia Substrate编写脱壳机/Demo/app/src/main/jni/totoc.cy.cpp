//
// Created by wnagzihxain on 2017/3/6 0006.
//

#include "totoc.cy.h"

#define BUFLEN 1024
#define TAG "toT0C"
#define errno (*__errno())
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

MSConfig(MSFilterLibrary, "/system/lib/libdvm.so")

//int dvmDexFileOpenPartial(const void* addr, int len, DvmDex** ppDvmDex)
int (*_dvmDexFileOpenPartial)(const void* addr, int len, void** ppDvmDex);

//get packagename from pid
int getProcessName(char* buffer) {
    char path_t[256] = {0};
    pid_t pid = getpid();
    char str[15];
    sprintf(str, "%d", pid);
    memset(path_t, 0, sizeof(path_t));
    strcat(path_t, "/proc/");
    strcat(path_t, str);
    strcat(path_t, "/cmdline");

    int fd_t = open(path_t, O_RDONLY);
    if(fd_t > 0) {
        int read_count = read(fd_t, buffer, BUFLEN);
        if(read_count > 0) {
            int processIndex = 0;
            for(processIndex = 0; processIndex < strlen(buffer); processIndex++) {
                if(buffer[processIndex] == ':'){
                    buffer[processIndex] = '_';
                }
            }
            return 1;
        }
    }
    return 0;
}

int my_dvmDexFileOpenPartial(const void* addr, int len, void** dvmdex) {
    extern volatile int* __errno(void);
    LOGI("Call myDexFileParse Pid is : %d", getpid());
    char dexbuffer[64] = {0};
    char dexbufferNamed[128] = {0};
    char *bufferProcess = (char *)calloc(256, sizeof(char));
    int  processStatus = getProcessName(bufferProcess);

    sprintf(dexbuffer, "_dump_%d", len);
    strcat(dexbufferNamed, "/sdcard/");

    if (processStatus == 1) {
        strcat(dexbufferNamed, bufferProcess);
        strcat(dexbufferNamed, dexbuffer);
    }
    else {
        LOGI("Fault Pid not found\n");
    }

    if(bufferProcess != NULL) {
        free(bufferProcess);
    }

    strcat(dexbufferNamed, ".dex");

    FILE *f = fopen(dexbufferNamed, "wb");
    if(!f) {
        LOGI("##########%s########## : Error open sdcard file to write", dexbufferNamed);
        LOGI("##########%s##########", strerror(errno));
    }
    else {
        fwrite(addr, 1, len, f);
        fclose(f);
        LOGI("Write %s successfully!!!!!!", dexbufferNamed);
    }
    return _dvmDexFileOpenPartial(addr, len, dvmdex);
}

//Substrate entry point
MSInitialize {
    MSImageRef image;
    image = MSGetImageByName("/system/lib/libdvm.so");
    if (image != NULL) {
        void *dexload = MSFindSymbol(image, "_Z21dvmDexFileOpenPartialPKviPP6DvmDex");
        if(dexload == NULL) {
            LOGI("Error find _Z21dvmDexFileOpenPartialPKviPP6DvmDex");
        }
        else {
            LOGI("dvmDexFileOpenPartial : 0x%p", dexload);
            MSHookFunction(dexload,
                           (void *)&my_dvmDexFileOpenPartial,
                           (void **)&_dvmDexFileOpenPartial);
        }
    }
    else{
        LOGI("Error find libdvm.so");
    }
}














