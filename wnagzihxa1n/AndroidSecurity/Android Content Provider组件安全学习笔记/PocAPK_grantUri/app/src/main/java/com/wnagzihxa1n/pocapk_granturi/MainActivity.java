package com.wnagzihxa1n.pocapk_granturi;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView textView;
    static final String TAG = "toT0C";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = null;
        try {
            Uri uri_query = Uri.parse("content://com.wnagzihxa1n.contentprovider.MyContentProvider/person/query");
            cursor = contentResolver.query(uri_query, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String contactName = cursor.getString(1);
                    String phoneNumber = cursor.getString(2);
                    textView.setText(String.valueOf(id) + ' ' + contactName + ' ' + phoneNumber);
                    Log.i(TAG, String.valueOf(id) + ' ' + contactName + ' ' + phoneNumber);
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
