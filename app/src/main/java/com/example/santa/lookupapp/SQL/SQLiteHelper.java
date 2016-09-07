package com.example.santa.lookupapp.SQL;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by santa on 16/7/8.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DEBUG = "SQLiteHelper";
    private static final int  DATABASE_VERSION=1;//固定写死

    public SQLiteHelper(Context context) {
        super(context, SQLiteUtils.DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+SQLiteUtils.RECORD_TABLE+"(_id integer primary key autoincrement,  "
                +SQLiteUtils.RECORD_PKGNAME+" char(50), "
                +SQLiteUtils.RECORD_APPNAME+" char(20), "
                +SQLiteUtils.RECORD_STARTTIME+" integer, "
                +SQLiteUtils.RECORD_ENDTIME+" integer)");

        db.execSQL("create table "+SQLiteUtils.APPUSE_TABLE+"(_id integer primary key autoincrement,  "
                +SQLiteUtils.APPUSE_PKGNAME+" char(50), "
                +SQLiteUtils.APPUSE_APPNAME+" char(20), "
                +SQLiteUtils.APPUSE_DAY+" integer, "
                +SQLiteUtils.APPUSE_USEHOUR+" integer, "
                +SQLiteUtils.APPUSE_USEMIN+" integer, "
                +SQLiteUtils.APPUSE_USESEC+" integer)");

        db.execSQL("create table "+SQLiteUtils.HOUR_TABLE+"(_id integer primary key autoincrement,  "
                +SQLiteUtils.HOUR_TIME+" integer, "
                +SQLiteUtils.HOUR_USEMIN+" integer, "
                +SQLiteUtils.HOUR_USESEC+" integer)");
        //db.execSQL(sql);
        Log.d(DEBUG,"数据表创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
