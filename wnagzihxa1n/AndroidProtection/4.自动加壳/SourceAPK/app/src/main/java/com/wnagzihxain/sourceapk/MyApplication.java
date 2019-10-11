package com.wnagzihxain.sourceapk;

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
