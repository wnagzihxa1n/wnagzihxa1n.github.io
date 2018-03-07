package com.wnagzihxa1n.protectapk;

import android.app.Application;
import android.content.Context;


/**
 * Created by wangz on 2018/3/6 0006.
 */

public class StupApplication extends Application {


    static {
        System.loadLibrary("native-lib");
    }


    @Override
    public native void onCreate();


    @Override
    protected native void attachBaseContext(Context base);

}
