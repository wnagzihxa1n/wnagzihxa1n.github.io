package com.wnagzihxa1n.obfuscatordemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Activity01.funcA();
        Activity02.funcB();
        Activity03.funcC();
        Activity04.funcD();
        Activity05.funcE();
    }
}
