package com.wnagzihxa1n.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.nio.file.Path;

/**
 * Created by wnagzihxa1n on 2018/1/23 0023.
 */

public class MyContentProvider extends ContentProvider {
    private static UriMatcher uriMatcher;
    private MySqlHelper mySqlHelper = null;
    private static final int CONTENT_INSERT = 0;
    private static final int CONTENT_QUERY = 1;
    private static final int CONTENT_OPENFILE = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.wnagzihxa1n.contentprovider.MyContentProvider", "person/insert", CONTENT_INSERT);
        uriMatcher.addURI("com.wnagzihxa1n.contentprovider.MyContentProvider", "person/query", CONTENT_QUERY);
        uriMatcher.addURI("com.wnagzihxa1n.contentprovider.MyContentProvider", "files", CONTENT_OPENFILE);
    }

    @Override
    public boolean onCreate() {
        try {
            mySqlHelper = new MySqlHelper(getContext());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteDatabase sqLiteDatabase = mySqlHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case CONTENT_QUERY:
                if (sqLiteDatabase.isOpen()) {
                    Cursor cursor = sqLiteDatabase.query("Person", strings, s, strings1, null, null, null);
                    return cursor;
                }
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase = mySqlHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case CONTENT_INSERT:
                if (sqLiteDatabase.isOpen()) {
                    long id = sqLiteDatabase.insert("Person", null, contentValues);
                    sqLiteDatabase.close();
                    return ContentUris.withAppendedId(uri, id);
                }
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

//    @Override
//    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
//        String temp = uri.getPath().toString();
//        String fileName = temp.substring(temp.lastIndexOf("/") + 1);
//        String decoded_fileName = URLDecoder.decode(fileName);
//        File file = new File(getContext().getFilesDir() + "/" + fileName);
//        if (file.exists()) {
//            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
//        }
//        return super.openFile(uri, mode);
//    }

//        @Override
//        public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
//            File file = new File(getContext().getFilesDir(), Uri.parse(uri.getLastPathSegment()).getLastPathSegment());
//            if (file.exists()) {
//                return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
//            }
//            return super.openFile(uri, mode);
//        }
}
