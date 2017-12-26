#include <jni.h>
#include <string>
#include <cstdio>
#include <cstring>
#include <android/log.h>
#include <iostream>
#include <unistd.h>
#include <stdlib.h>
#include <sys/inotify.h>
using namespace std;

#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_INFO, "toT0C", fmt, ##args)

extern "C"
JNIEXPORT void JNICALL
Java_com_wnagzihxa1n_demo_MainActivity_checkUninstall(
        JNIEnv *env,
        jobject /* this */) {

    LOGE("Start");

    pid_t pid = fork();
    if (pid < 0) {
        LOGE("Fork failed");
        exit(1);
    } else if (pid == 0) {
        LOGE("Enter child process");
        int fd = inotify_init();

        if (fd < 0) {
            LOGE("Call inotify_init() failed");
            exit(1);
        }
        LOGE("Call inotify_init() successfully");

        int monitor = inotify_add_watch(fd, "/data/data/com.wnagzihxa1n.demo", IN_DELETE);
        if (monitor < 0) {
            LOGE("Call inotify_add_watch() failed");
            exit(1);
        }
        LOGE("Call inotify_add_watch() successfully");

        void* pBuffer = malloc(sitruct inotify_event));
        if (pBuffer == NULL) {
            LOGE("Malloc buffer failed");
            exit(1);
        }

        ssize_t readSize = read(fd, pBuffer, sizeof(struct inotify_event));

        free(pBuffer);
        inotify_rm_watch(fd, IN_DELETE);

        LOGE("**********Find Uninstall**********");
        exit(0);
    } else if (pid > 0) {
        return;
    } else {
        exit(1);
    }
}
