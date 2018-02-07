package com.wnagzihxa1n.contentprovider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;

public class MainActivity extends Activity {

    static final String TAG = "toT0C";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentResolver contentResolver = getContentResolver();

        // 插入数据
        Uri uri_insert = Uri.parse("content://com.wnagzihxa1n.contentprovider.MyContentProvider/person/insert");
        ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put("name", "wnagzihxa1n");
        values.put("phoneNumber", "1 999-999-3389");
        contentResolver.insert(uri_insert, values);

        // 查询数据
        Cursor cursor = null;
        try {
            Uri uri_query = Uri.parse("content://com.wnagzihxa1n.contentprovider.MyContentProvider/person/query");
            cursor = contentResolver.query(uri_query, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String contactName = cursor.getString(1);
                    String phoneNumber = cursor.getString(2);
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

        // 创建文件
//        FileOutputStream fileOutputStream = null;
//        File directory = getFilesDir();
////        Log.i(TAG, directory.getAbsolutePath());
//        File demoXML = new File(directory.getAbsolutePath(), "/demo.xml");
//        try {
//            if (!demoXML.exists()) {
//                demoXML.createNewFile();
//            }
//            fileOutputStream = new FileOutputStream(demoXML);
//            byte[] data = "Yes, you got me\n".getBytes();
//            fileOutputStream.write(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fileOutputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        // 读取文件
//        FileInputStream fileInputStream = null;
//        Uri uri_openfile = Uri.parse("content://com.wnagzihxa1n.contentprovider.MyContentProvider/files/demo.xml");
//        try {
//            fileInputStream = (FileInputStream) contentResolver.openInputStream(uri_openfile);
//            byte[] data = new byte[fileInputStream.available()];
//            fileInputStream.read(data);
//            Log.i(TAG, new String(data));
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fileInputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}
