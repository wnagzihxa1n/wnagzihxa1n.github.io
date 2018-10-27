package com.wnagzihxain.sourceapk;

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