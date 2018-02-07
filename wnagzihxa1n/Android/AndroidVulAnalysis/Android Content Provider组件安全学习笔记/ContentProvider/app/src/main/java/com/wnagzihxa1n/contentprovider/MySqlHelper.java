package com.wnagzihxa1n.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wnagzihxa1n on 2018/1/23 0023.
 */

public class MySqlHelper extends SQLiteOpenHelper {

    private static String DBName = "PersonDB.db";
    private static int version = 1;

    public MySqlHelper(Context context) {
        super(context, DBName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table Person (id integer primary key autoincrement, name nvarchar(100), phoneNumber nvarchar(100))";
        sqLiteDatabase.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
