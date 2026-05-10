---
layout: post
title:  "[CVE-2022-22292] [Samsung] [Telecom] 动态注册BroadcastReceiver默认导出存在任意私有组件启动漏洞"
date:   2022-08-01 18:00:00 +520
categories: Android_Security
tags: [AndroidApplication, LogicBug, ExportedComponent]
---

|    Date    | Version |                       Description                        |   Author    |
| :--------: | :-----: | :------------------------------------------------------: | :---------: |
| 2022.08.01 |   1.0   |                      完整的漏洞分析                      | wnagzihxa1n |

CVSS打分参考

- https://nvd.nist.gov/vuln/detail/CVE-2022-22292

## 0x00 漏洞概述

三星的系统应用Telecom动态注册的BrocastReceiver存在任意私有组件启动漏洞，造成三方应用获取系统权限发送Intent实现手机系统数据清空，任意应用安装卸载等效果

## 0x01 触发条件

| 上线时间 | 应用名  |            包名            | 软件版本 |               MD5                | 下载链接 |
| :------: | :-----: | :------------------------: | :------: | :------------------------------: | :------: |
|          | Telecom | com.android.server.telecom |          | bb3bbdc5e459ac163c296ba35dc01d29 |          |

固件下载

- https://samfw.com/firmware/SM-G998U1/XAA/G998U1UEU4BUK7

## 0x02 PoC

## 0x03 前置知识

## 0x04 Root Cause

固件下载与解压缩请自行完成，对应APK的相对路径如下

```
system/priv-app/Telecom/Telecom.apk
```

类`com.samsung.server.telecom.advancedcall.wificall.SamsungUsaWpsBroadcastReceiver`动态注册了一个BroadcastReceiver，且无调用权限限制，`[2]`获取传入的Intent里的一个Intent并将其设置到类全局变量，`[3]`进入调用Intent环节

```java
// com.samsung.server.telecom.advancedcall.wificall.SamsungUsaWpsBroadcastReceiver
class SamsungUsaWpsBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = SamsungUsaWpsBroadcastReceiver.class.getSimpleName();
    private final CallsManager mCallsManager;
    private final Context mContext;
    private final SamsungFeatureRepository mSamsungFeatureRepository;
    private final SamsungUsaWpsCallsManagerListener mSamsungUsaWpsCallsManagerListener;
    private final SamsungUsaWpsHelper mSamsungUsaWpsHelper;

    public SamsungUsaWpsBroadcastReceiver(Context context, CallsManager callsManager, SamsungFeatureRepository samsungFeatureRepository, SamsungUsaWpsCallsManagerListener samsungUsaWpsCallsManagerListener, SamsungUsaWpsHelper samsungUsaWpsHelper) {
        this.mContext = context;
        this.mCallsManager = callsManager;
        this.mSamsungFeatureRepository = samsungFeatureRepository;
        this.mSamsungUsaWpsCallsManagerListener = samsungUsaWpsCallsManagerListener;
        this.mSamsungUsaWpsHelper = samsungUsaWpsHelper;
    }

    public void init() {
        this.mContext.registerReceiver(this, new IntentFilter("com.samsung.server.telecom.USER_SELECT_WIFI_SERVICE_CALL"));  // [1]
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("com.samsung.server.telecom.USER_SELECT_WIFI_SERVICE_CALL".equals(intent.getAction())) {
            boolean areNoCallsPresent = this.mSamsungUsaWpsHelper.areNoCallsPresent();
            boolean WIRELESS_PRIORITY_SERVICE_3WAYCALL = this.mSamsungFeatureRepository.WIRELESS_PRIORITY_SERVICE_3WAYCALL();
            String str = TAG;
            SamsungLogger.i(str, "onReceive - areNoCallsPresent: " + areNoCallsPresent + ", isFeatureEnabled: " + WIRELESS_PRIORITY_SERVICE_3WAYCALL);
            this.mSamsungUsaWpsHelper.setCallIntent((Intent) intent.getParcelableExtra("extra_call_intent"));  // [2]
            if (areNoCallsPresent || WIRELESS_PRIORITY_SERVICE_3WAYCALL) {
                this.mSamsungUsaWpsHelper.wirelessPriorityServiceCall();  // [3]
                return;
            }
            this.mCallsManager.disconnectAllCalls(str);
            this.mCallsManager.addListener(this.mSamsungUsaWpsCallsManagerListener);
        }
    }
}
```

`[4]`调用`mSamsungTelecomManagerAdapter.makeCall()`处理`mCallIntent`，此时`mCallIntent`是外部完全可控

```java
// com.samsung.server.telecom.advancedcall.wificall.SamsungUsaWpsHelper
class SamsungUsaWpsHelper {
    private Intent mCallIntent;

    public void setCallIntent(Intent intent) {
        this.mCallIntent = intent;  // 设置传入Intent里的Intent数据到类全局变量
    }

    public void wirelessPriorityServiceCall() {
        if (this.mCallIntent != null) {
            this.mCallIntent.setFlags(268435456);  // 0x10000000
            this.mSamsungTelecomManagerAdapter.makeCall(this.mContext, this.mCallIntent);  // [4]
            return;
        }
        SamsungLogger.i(TAG, "wirelessPriorityServiceCall, call intent is null");
    }
}
```

`mSamsungTelecomManagerAdapter`是一个接口类，只有动态运行时才能确定其具体的实现类，这是静态分析的一个难点

```java
// com.samsung.server.telecom.model.telecom.SamsungTelecomManagerAdapter
public interface SamsungTelecomManagerAdapter {
    default void makeCall(Context context, Intent intent) {
    }

    default void placeCall(Context context, Intent intent) {
    }

    default void placeCall(Context context, Uri uri, Bundle bundle) {
    }
}
```

我们手动分析时可以通过跟踪对应变量的初始化过程找到具体的实现类，`[5]`调用方法`startCall()`，`[6]`最终使用系统权限将外部可控的Intent发出

```java
//
class SamsungTelecomManagerAdapterImpl implements SamsungTelecomManagerAdapter {

    @Override // com.samsung.server.telecom.model.telecom.SamsungTelecomManagerAdapter
    public void makeCall(Context context, Intent intent) {
        startCall(context, intent, UserHandle.CURRENT);  // [5]
    }

    private void startCall(Context context, Intent intent, UserHandle userHandle) {
        try {
            context.startActivityAsUser(intent, userHandle);  // [6]
        } catch (ActivityNotFoundException e) {
            SamsungLogger.i(str, "startCall - ActivityNotFoundException : " + e);
        }
    }
}
```

## 0x05 调试与利用

## 0x06 漏洞研究

## 0x07 References

- https://www.blackhat.com/asia-22/briefings/schedule/#start-arbitrary-activity-app-components-as-the-system-user-vulnerability-affecting-samsung-android-devices-26190

## 附录：调试过程记录