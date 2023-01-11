---
layout: post
title:  "保护技术开发03 - 添加Application"
date:   2017-09-03 18:00:00 +520
categories: Android_Security
---

一般情况下，一个正常的APP都会有一个Application类，少有APP没有这个，那么我们加壳的时候就要处理这个Application

我们前面直接加上了自己的ProtectApplication，因为没有Application，所以直接就调用了入口的MainActivity，如果待加壳的应用存在Application，我们在加壳的时候，就需要先保存这个Application，然后加载完自己的ProtectApplication后，恢复应用自身的Application，再去调用入口的MainActivity

依旧使用前一篇文章的SourceAPK工程，我们稍微进行一下修改，给添加一个MyApplication，在这个Application里定义两个字符串，一个计数器，两个Activity互相跳转的时候获取Application的数据进行显示

MyApplication.java
```
package com.wnagzihxa1n.sourceapk;

import android.app.Application;

public class MyApplication extends Application {
    private int count = 1;
    private String main_activity = "I am MainActivity";
    private String second_activity = "I am SecondActivity";

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMain_activity() {
        return main_activity;
    }

    public String getSecond_activity() {
        return second_activity;
    }
}
```

MainActivity.java
```
package com.wnagzihxa1n.sourceapk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.view1_textView);
        button = (Button) findViewById(R.id.view1_button);

        MyApplication myApplication = (MyApplication)getApplicationContext();
        textView.setText(myApplication.getMain_activity() + "\nCount : " + myApplication.getCount());

        myApplication.setCount(myApplication.getCount() + 1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SecondActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }
}
```

SecondActivity.java
```
package com.wnagzihxa1n.sourceapk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends Activity {

    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = (TextView) findViewById(R.id.view2_textView);
        button = (Button) findViewById(R.id.view2_button);

        MyApplication myApplication = (MyApplication)getApplicationContext();
        textView.setText(myApplication.getSecond_activity() + "\nCount : " + myApplication.getCount());

        myApplication.setCount(myApplication.getCount() + 1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SecondActivity.this, MainActivity.class);
                startActivity(intent);
                SecondActivity.this.finish();
            }
        });
    }
}
```

运行，字符串和下面的数字均从MyApplication获取

![IMAGE](/assets/resources/28D8B97C90CDEACB4F3C9C6C66E07D0E.jpg)

点击按钮，修改数字并跳转

![IMAGE](/assets/resources/35F2639106F8D9011E1381792B676A5A.jpg)

调试着要是没啥问题，就编译签名，命名为SourceAPK.apk

删掉所有代码，添加壳代码，因为我们添加了Application，所以除了动态加载APK之外，还需要加载MyApplication，所以代码中比原来的多了一段
```
public void onCreate() {
    String appClassName = null;
    try {
        ApplicationInfo ai = this.getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
        Bundle bundle = ai.metaData;
        if (bundle != null && bundle.containsKey("APPLICATION_CLASS_NAME")) {
            appClassName = bundle.getString("APPLICATION_CLASS_NAME");
        } else {
            return;
        }
    } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
    }
    Object currentActivityThread = RefInvoke.invokeStaticMethod("android.app.ActivityThread", "currentActivityThread", new Class[]{}, new Object[]{});
    Object mBoundApplication = RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mBoundApplication");
    Object loadedApkInfo = RefInvoke.getFieldOjbect("android.app.ActivityThread$AppBindData", mBoundApplication, "info");
    RefInvoke.setFieldOjbect("android.app.LoadedApk", "mApplication", loadedApkInfo, null);
    Object oldApplication = RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mInitialApplication");
    ArrayList<Application> mAllApplications = (ArrayList<Application>) RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mAllApplications");
    mAllApplications.remove(oldApplication);
    ApplicationInfo appinfo_In_LoadedApk = (ApplicationInfo) RefInvoke.getFieldOjbect("android.app.LoadedApk", loadedApkInfo, "mApplicationInfo");
    ApplicationInfo appinfo_In_AppBindData = (ApplicationInfo) RefInvoke.getFieldOjbect("android.app.ActivityThread$AppBindData", mBoundApplication, "appInfo");
    appinfo_In_LoadedApk.className = appClassName;
    appinfo_In_AppBindData.className = appClassName;
    Application app = (Application) RefInvoke.invokeMethod("android.app.LoadedApk", "makeApplication", loadedApkInfo, new Class[]{boolean.class, Instrumentation.class}, new Object[]{false, null});
    RefInvoke.setFieldOjbect("android.app.ActivityThread", "mInitialApplication", currentActivityThread, app);
    ArrayMap mProviderMap = (ArrayMap) RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread, "mProviderMap");
    Iterator it = mProviderMap.values().iterator();
    while (it.hasNext()) {
        Object providerClientRecord = it.next();
        Object localProvider = RefInvoke.getFieldOjbect("android.app.ActivityThread$ProviderClientRecord", providerClientRecord, "mLocalProvider");
        RefInvoke.setFieldOjbect("android.content.ContentProvider", "mContext", localProvider, app);
    }
    app.onCreate();
}
```

大概的作用就是替换源APK的Application对象，同时在AndroidManifest.xml里添加
```
 <meta-data android:name="APPLICATION_CLASS_NAME" android:value="com.wnagzihxa1n.sourceapk.MyApplication"/>
 ```
 
这样我们就可以找到待加壳程序的入口Application

再把SourceAPK.apk重命名为`a`放到`assets`，跑起来

![IMAGE](/assets/resources/0DF15BB6FDECA6FD0793EF853F2F2EF9.jpg)

![IMAGE](/assets/resources/305D778C544BE669A1C3FBBBFCBB511A.jpg)

点击按钮也能正常获取数据，跳转页面没啥大问题