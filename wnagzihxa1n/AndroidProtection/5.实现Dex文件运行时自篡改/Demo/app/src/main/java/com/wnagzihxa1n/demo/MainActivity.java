package com.wnagzihxa1n.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    EditText editText1, editText2;
    Button button;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public void getSum(int a, int b) {
        int add = a + b;
        int sub = a - b;
        Toast.makeText(MainActivity.this, "add = " + add + "\nsub = " + sub, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSum(Integer.valueOf(editText1.getText().toString()), Integer.valueOf(editText2.getText().toString()));
            }
        });
    }
}
