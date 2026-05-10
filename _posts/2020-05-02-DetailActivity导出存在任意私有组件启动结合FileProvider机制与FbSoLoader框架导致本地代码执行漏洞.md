---
layout: post
title:  "[ByteDance] [TikTok] DetailActivity导出存在任意私有组件启动结合FileProvider机制与FbSoLoader框架导致本地代码执行漏洞"
date:   2020-05-02 18:00:00 +520
categories: Android_Security
tags: [AndroidApplication, LogicBug, ExportedComponent]
---

| 日期 | 版本 | 描述 | 作者 |
| :-: | :-: | :-: | :-: |
| 2020.05.02 | 1.0 | 完整的漏洞分析与利用 | wnagzihxa1n |

## 0x00 漏洞概述

## 0x01 触发条件

| 上线时间 | 应用名 | 包名 | 软件版本 | 下载链接 |
| :-: | :-: | :-: | :-: | :-: |
| 2020.02.08 | TikTok | com.zhiliaoapp.musically | 14.8.3 | https://www.apkmirror.com/apk/tiktok-pte-ltd/tik-tok-including-musical-ly/tik-tok-including-musical-ly-14-8-3-release/ |

## 0x02 PoC

## 0x03 前置知识

## 0x04 Root Cause

组件`com.ss.android.ugc.aweme.detail.ui.DetailActivity`导出

```xml
<activity 
    android:name="com.ss.android.ugc.aweme.detail.ui.DetailActivity" 
    android:screenOrientation="portrait" 
    android:configChanges="keyboard|keyboardHidden|orientation|screenSize"
    android:windowSoftInputMode="adjustUnspecified|stateHidden|adjustResize">
    <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <data android:scheme="taobao" android:host="detail.aweme.sdk.com"/>
    </intent-filter>
</activity>
```

很直接的获取传入的Intent去跳转，点击返回键来触发，返回键大部分的安卓机都是有的

```java
@Override  // android.support.v4.app.FragmentActivity
public void onBackPressed() {
    if(com.ss.android.ugc.aweme.utils.d.c.c()) {
        Intent intent = (Intent)this.getIntent().getParcelableExtra("VENDOR_BACK_INTENT_FOR_INTENT_KEY");
        if(intent != null && intent.resolveActivity(this.getPackageManager()) != null) {
            this.startActivity(intent);
            this.finish();
            return;
        }
    }
    
    ...
}
```

## 0x05 漏洞调试与利用

利用过程和前一个漏洞一样，利用FileProvider的权限获得对私有目录动态库的读写能力，覆写后重启应用实现持久化RCE

## 0x06 漏洞研究

## 0x07 References

- https://blog.oversecured.com/Oversecured-detects-dangerous-vulnerabilities-in-the-TikTok-Android-app/

## 附录：调试过程记录