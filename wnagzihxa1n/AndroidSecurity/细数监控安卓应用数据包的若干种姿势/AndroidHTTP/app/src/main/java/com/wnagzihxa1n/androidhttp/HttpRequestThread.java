package com.wnagzihxa1n.androidhttp;

import android.os.Handler;
import android.os.Message;

public class HttpRequestThread extends Thread {
    private Handler handler;

    public HttpRequestThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        doHttpRequest();
    }

    private void doHttpRequest() {

    }

    private void sendMsg(int what, Object object) {
        Message message = new Message();
        message.what = what;
        message.obj = object;
        this.handler.sendMessage(message);
    }
}
