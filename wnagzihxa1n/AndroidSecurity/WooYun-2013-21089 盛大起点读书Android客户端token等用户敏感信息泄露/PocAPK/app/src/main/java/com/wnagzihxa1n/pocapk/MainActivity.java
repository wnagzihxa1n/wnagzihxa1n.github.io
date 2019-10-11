package com.wnagzihxa1n.pocapk;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.FileInputStream;
import java.util.ArrayList;

public class MainActivity extends Activity {

    static final String TAG = "toT0C";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = null;
        try {
            Uri uri_query = Uri.parse("content://com.qidian.QDReader/searchkey");
            cursor = contentResolver.query(uri_query, null, null, null, null);
            while (cursor.moveToNext()) {
                if (cursor != null) {
                    String keyword = cursor.getString(cursor.getColumnIndex("Keyword"));
                    Log.i(TAG, keyword);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}