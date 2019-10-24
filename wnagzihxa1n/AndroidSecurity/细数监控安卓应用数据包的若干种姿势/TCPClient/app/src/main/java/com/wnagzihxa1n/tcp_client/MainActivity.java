package com.wnagzihxa1n.tcp_client;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private String address = "172.27.35.3";
    private int port = 23333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.mButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TcpClientThread tcpClientThread = new TcpClientThread(mHandler, address, port, "Here is wnagzihxa1n.");
                tcpClientThread.start();
            }
        });
    }

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.e("logMethodTrace-Server", "handleMessage: " + (String)msg.obj);
                    break;
            }
        }
    };
}
