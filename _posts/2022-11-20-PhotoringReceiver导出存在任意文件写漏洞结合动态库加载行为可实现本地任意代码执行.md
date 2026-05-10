---
layout: post
title:  "[CVE-2021-25397] [Samsung] [TelephonyUI] 组件PhotoringReceiver导出存在任意文件写漏洞结合动态库加载行为可实现本地任意代码执行"
date:   2022-11-20 18:00:00 +520
categories: Android_Security
tags: [AndroidApplication, LogicBug, ExportedComponent]
---

|    Date    | Version |     Description      |   Author    |
| :--------: | :-----: | :------------------: | :---------: |
| 2022.11.20 |   1.0   | 完整的漏洞分析与利用 | wnagzihxa1n |

## 0x00 漏洞概述

三星手机系统的TelephonyUI存在一个导出组件`PhotoringReceiver`，其接收外部传入的两个字段`"photoring_uri"`和`"down_file"`，TelephonyUI会从`"photoring_uri"`下载文件到`"down_file"`指向的文件路径，此处存在任意文件写漏洞，结合私有目录下动态库加载的行为，可通过覆写动态库文件实现本地任意代码执行

## 0x01 触发条件

| 上线日期 |   应用名    |                包名                 |   版本号   |               MD5                | 下载链接 |
| :------: | :---------: | :---------------------------------: | :--------: | :------------------------------: | :------: |
|          | TelephonyUI | com.samsung.android.app.telephonyui | 12.1.02.37 | f5176d28be8a735ad3e53df19b0fa81d |          |

## 0x02 PoC

## 0x03 前置知识

## 0x04 Root Cause Analysis

组件`com.samsung.android.app.telephonyui.carrierui.photoring.model.PhotoringReceiver`导出

```xml
<receiver 
    android:name="com.samsung.android.app.telephonyui.carrierui.photoring.model.PhotoringReceiver" 
    android:exported="true">
    <intent-filter>
        <action android:name="com.samsung.android.app.telephonyui.action.DOWNLOAD_PHOTORING"/>
    </intent-filter>
</receiver>

```

`[1]`和`[2]`取出传入Intent里的`"photoring_uri"`和`"down_file"`字段，`[3]`调用方法`PhotoringMgr.downloadContent()`处理传入的数据

```java
// com.samsung.android.app.telephonyui.carrierui.photoring.model.PhotoringReceiver
public class PhotoringReceiver extends BroadcastReceiver {
    @Override  // android.content.BroadcastReceiver
    public void onReceive(Context __context__, Intent __intent__) {
        String __intent_action__ = __intent__.getAction();
        b.b("PhotoringReceiver", "onReceive : %s", new Object[]{__intent_action__});
        if(__intent_action__ == null) {
            return;
        }

        if(__intent_action__.hashCode() == 0xA26C9254 && (__intent_action__.equals("com.samsung.android.app.telephonyui.action.DOWNLOAD_PHOTORING"))) {
            String __intent_photoring_uri__ = __intent__.getStringExtra("photoring_uri");  // [1]
            String __intent_down_file__ = __intent__.getStringExtra("down_file");  // [2]
            b.b("PhotoringReceiver", "McidReceiver onReceive url : " + __intent_photoring_uri__ + ", file : " + __intent_down_file__, new Object[0]);
            PhotoringMgr.getInstance().b = true;  
            PhotoringMgr.getInstance().getContentSpecCall();
            PhotoringMgr.getInstance().downloadContent(__intent_photoring_uri__, __intent_down_file__);  // [3]
        }
    }
}

```

方法`PhotoringMgr.downloadContent()`对类变量`mCallMessageNetwork`进行判空校验并赋值给局部变量`callMessageNetwork`，`[1]`调用方法`CallMessageNetwork.a()`继续处理

```java
// com.samsung.android.app.telephonyui.carrierui.photoring.model.PhotoringMgr
public void downloadContent(String __intent_photoring_uri__, String __intent_down_file__) {
    com.samsung.android.app.telephonyui.utils.d.b.b("PhotoringMgr", "downloadContent", new Object[0]);
    CallMessageNetwork callMessageNetwork = this.mCallMessageNetwork;
    if(callMessageNetwork != null) {
        callMessageNetwork.a(__intent_photoring_uri__, __intent_down_file__);  // [1]
    }
}
```

方法`CallMessageNetwork.a()`调用多线程类`CallMessageNetworkThread`

```java
// com.samsung.android.app.telephonyui.carrierui.photoring.model.CallMessageNetwork
public void a(String __intent_photoring_uri__, String __intent_down_file__) {
    this.interruptThread();
    CallMessageNetworkThread callMessageNetworkThread = new CallMessageNetworkThread(this, __intent_photoring_uri__, __intent_down_file__);  // [1]
    this.mThread = callMessageNetworkThread;
    callMessageNetworkThread.start();
}
```

`[1]`根据当前运行环境编辑字段`"photoring_uri"`，不影响后续逻辑，`[2]`使用参数`RequestType.DOWNLOAD_CONTENT`和`"photoring_uri"`初始化`HTTPRequestHelper`对象，并调用方法`get()`

```java
class CallMessageNetworkThread extends Thread {
    String __intent_photoring_uri__;
    String __intent_down_file__;
    final CallMessageNetwork callMessageNetwork;

    @Override
    public void run() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if(TuiImsFeature.INSTANCE.SUPPORT_PHOTO_RING) {
            this.__intent_photoring_uri__ = this.__intent_photoring_uri__ + "?" + CallMessageNetwork.this.aes256Encrypt("CTN=" + CallMessageNetwork.this.getUserCTN());  // [1]
        }

        new HTTPRequestHelper(CallMessageNetwork.this.e, RequestType.DOWNLOAD_CONTENT, this.__intent_down_file__, this).get(this.__intent_photoring_uri__, linkedHashMap);  // [2]
    }
}
```

在方法`HTTPRequestHelper.get()`里，`[1]`获取`HttpClient`对象，用于后续网络请求，`[2]`和`[4]`都是打印参数操作，`[3]`执行网络请求，`[5]`处理请求回来的返回值

```java
// com.samsung.android.app.telephonyui.carrierui.photoring.model.a.HTTPRequestHelper
public boolean get(String __intent_photoring_uri__, Map paramMap) {
    Logger.b("HTTPRequestHelper", "get %s", new Object[]{__intent_photoring_uri__});
    this.__url__ = __intent_photoring_uri__.startsWith("https://") ? __intent_photoring_uri__ : "https://" + __intent_photoring_uri__;
    if(paramMap != null && !paramMap.isEmpty()) {
        ...
    }

    HttpClient httpClient = HTTPClientFactory.getHttpClient();  // [1]
    httpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(12000));
    httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(12000));
    HttpGet httpGet = new HttpGet(this.__url__);
    try {
        this.dumpHttpGet(httpGet);  // [2]
        HttpResponse __httpResponse__ = httpClient.execute(((HttpUriRequest)httpGet));  // [3]
        if(this.mThread != null && (this.mThread.isInterrupted())) {
            return false;
        }

        this.dumpHttpResponse(__httpResponse__);  // [4]
        this.b(__httpResponse__);  // [5]
        return true;
    }
    catch(IOException iOException) {
        Logger.c("HTTPRequestHelper", "IOException " + iOException, new Object[0]);
        this.b(null);
        return false;
    }
}
```

`[1]`处理网络请求返回值

```java
// com.samsung.android.app.telephonyui.carrierui.photoring.model.a.HTTPRequestHelper
private void b(HttpResponse __httpResponse__) {
    int v;
    Message message = this.mHandler.obtainMessage();
    Bundle bundle = new Bundle();
    bundle.putString("URL", this.__url__);  // intent取出来的字段"photoring_uri"
    bundle.putString("ReqType", this.mRequestType.name());
    this.a();
    if(__httpResponse__ == null) {
        v = 1;
    }
    else {
        HTTPResponseParser __httpResponseParser__ = this.mRequestType == RequestType.DOWNLOAD_CONTENT ? new HTTPResponseParser(this.__m_intent_down_fiile__) : new HTTPResponseParser();
        if((b.d.isCurrentOperator()) && this.mRequestType == RequestType.DOWNLOAD_CONTENT) {
            __httpResponseParser__.a(0x100000);
            __httpResponseParser__.a("image/png");
        }

        v = __httpResponseParser__.a(__httpResponse__);  // [1]
        if(v == 0) {  // 正常解析进入的分支
            __httpResponseParser__.c();  // [2]
            ...
        }
    }

    ...

    message.setData(bundle);
    this.mHandler.sendMessage(message);  // [3]
}
```

方法`HTTPResponseParser.a()`调用`HTTPResponseParser.e()`，加了层异常捕获

```java
// com.samsung.android.app.telephonyui.carrierui.photoring.model.a.HTTPResponseParser
public int a(HttpResponse __httpResponse__) {
    this.__m_httpResponse__ = __httpResponse__;
    this.parseStatusLine();
    try {
        return this.e();  // [1]
    }
    catch(IOException iOException0) {
        ...
    }
}
```

`[1]`从网络请求返回值里取出`HttpEntity`，`[2]`从`HttpEntity`里取出`InputStream`，`[3]`调用方法`HTTPResponseParser.convertIsToFile()`把`InputStream`写入到字段`"down_file"`指向的文件路径

```java
// com.samsung.android.app.telephonyui.carrierui.photoring.model.a.HTTPResponseParser
private int e() {
    HttpEntity __httpEntity__ = this.__m_httpResponse__.getEntity();  // [1]
    InputStream __inputStream__ = __httpEntity__.getContent();  // [2]
    if(__inputStream__ == null) {
        return 1;
    }

    this.__mContentLength__ = (int)__httpEntity__.getContentLength();
    if(__httpEntity__.getContentType() != null) {
        HeaderElement[] arr_headerElement = __httpEntity__.getContentType().getElements();
        if(arr_headerElement.length > 0) {
            NameValuePair nameValuePair = arr_headerElement[0].getParameterByName("charset");
            if(nameValuePair != null) {
                this.mContentCharSet = nameValuePair.getValue();
            }

            this.mContentType = arr_headerElement[0].getName();
        }
    }

    if(b.KTT.isCurrentOperator()) {
        int v = this.j;
        if(v != 0) {
            this.__mContentLength__ = v;
        }

        String s = this.k;
        if(s != null) {
            this.mContentType = s;
        }
    }

    int contentLength = this.__mContentLength__;
    if(contentLength <= 0x7FFFFFFF) {
        if(contentLength < 0) {
            this.__mContentLength__ = 0x1000;
        }

        if(this.mContentCharSet == null) {
            this.mContentCharSet = "UTF-8";
        }

        if(this.__intent_down_file__ != null && ((this.mContentType.startsWith("video/")) || (this.mContentType.startsWith("image/")))) {
            if(this.isSpaceAvailable()) {
                this.convertIsToFile(__inputStream__, this.__intent_down_file__);  // [3]
                __inputStream__.close();
                return 0;
            }

            __inputStream__.close();
            return 2;
        }

        this.f = this.a(__inputStream__);
        __inputStream__.close();
        return 0;
    }

    throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
}
```

写出到文件的方法`HTTPResponseParser.convertIsToFile()`，`[1]`创建对应的`FileOutputStream`对象，`[2]`读取数据，`[3]`写出到文件

```java
// com.samsung.android.app.telephonyui.carrierui.photoring.model.a.HTTPResponseParser
private void convertIsToFile(InputStream __inputStream__, String __intent_down_file__) {
    FileOutputStream fileOutputStream;
    Logger.b("HTTPResponse", "convertIsToFile %s", new Object[]{__intent_down_file__});
    if(__intent_down_file__ == null) {
        return;
    }

    File file = new File(new File(__intent_down_file__).getParent());
    if(!file.exists() && !file.mkdirs()) {
        Logger.b("HTTPResponse", "Error with Make directory", new Object[0]);
    }

    try {
        fileOutputStream = new FileOutputStream(__intent_down_file__);  // [1]
        int contentLength = this.__mContentLength__;
        int validContentLength = Math.min(0x1000, contentLength);
        byte[] arr_b = new byte[validContentLength];
        while(true) {
            int readCount = __inputStream__.read(arr_b, 0, validContentLength);  // [2]
            if(readCount <= 0) {
                break;
            }

            fileOutputStream.write(arr_b, 0, readCount);  // [3] 写出到指定路径的文件
            contentLength -= readCount;
            validContentLength = Math.min(0x1000, contentLength);
        }
    }
    catch(IOException iOException) {
        Logger.c("HTTPResponse", "IOException " + iOException, new Object[0]);
        return;
    }

    try {
        fileOutputStream.close();
    }
    catch(IOException iOException) {
        Logger.c("HTTPResponse", "IOException2 " + iOException, new Object[0]);
    }
}
```

完整的逻辑调用图如下

![逻辑调用图](/assets/resources/87FAEB044F23B6B2BF9207C7DE718944.svg)

## 0x05 调试与利用

## 0x06 漏洞研究

## 0x07 References

《Two weeks of securing Samsung devices: Part 1》

- https://blog.oversecured.com/Two-weeks-of-securing-Samsung-devices-Part-1/

## 附录：调试过程记录



