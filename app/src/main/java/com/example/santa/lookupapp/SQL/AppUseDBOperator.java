package com.example.santa.lookupapp.SQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by santa on 16/7/8.
 */
public class AppUseDBOperator {
    private SQLiteDatabase db = null;

    public AppUseDBOperator(SQLiteDatabase db) {
        this.db = db;
    }

    public void insert(AppUseDB appuse) {
        ContentValues values = new ContentValues();
        values.put(SQLiteUtils.APPUSE_PKGNAME, appuse.getPkgName());
        values.put(SQLiteUtils.APPUSE_APPNAME, appuse.getAppname());
        values.put(SQLiteUtils.APPUSE_USEHOUR, appuse.getUseHour());
        values.put(SQLiteUtils.APPUSE_USEMIN, appuse.getUseMinute());
        values.put(SQLiteUtils.APPUSE_USESEC, appuse.getUseSecond());
        values.put(SQLiteUtils.APPUSE_DAY, appuse.getDay());
        db.insert(SQLiteUtils.APPUSE_TABLE, null, values);
        db.close();
    }

    public ArrayList<AppUseDB> find(String year, String month, String day) {
        ArrayList<AppUseDB> all = new ArrayList<>();
        DayTick dayTick = SQLiteUtils.getDayTick(year, month, day);
        Log.d("database", dayTick.getStartTick()+"");
//        Cursor result = this.db.query(SQLiteUtils.APPUSE_TABLE, null, SQLiteUtils.APPUSE_DAY+" = ?", new String[]{String.valueOf(dayTick.getStartTick())}, null, null, SQLiteUtils.APPUSE_USETIME+"DESC", null);
        Cursor result = this.db.query(SQLiteUtils.APPUSE_TABLE, null, SQLiteUtils.APPUSE_DAY+" >= ? and "+SQLiteUtils.APPUSE_DAY+" < ?", new String[]{String.valueOf(dayTick.getStartTick()), String.valueOf(dayTick.getEndTick())}, null, null, SQLiteUtils.APPUSE_USEHOUR+" DESC," + SQLiteUtils.APPUSE_USEMIN+" DESC", null);
        Log.i("database", "get "+ String.valueOf(result.getCount()) + "db info");
        for (result.moveToFirst() ; !result.isAfterLast();result.moveToNext()) {
            AppUseDB appUseDB = new AppUseDB(result.getString(result.getColumnIndex(SQLiteUtils.APPUSE_PKGNAME)),
                    result.getString(result.getColumnIndex(SQLiteUtils.APPUSE_APPNAME)),
                    Long.valueOf(result.getString(result.getColumnIndex(SQLiteUtils.APPUSE_DAY))),
                    Integer.valueOf(result.getString(result.getColumnIndex(SQLiteUtils.APPUSE_USEHOUR))),
                    Integer.valueOf(result.getString(result.getColumnIndex(SQLiteUtils.APPUSE_USEMIN))),
                    Integer.valueOf(result.getString(result.getColumnIndex(SQLiteUtils.APPUSE_USESEC)))  );
            all.add(appUseDB);
        }
        result.close();
        this.db.close();
        return all;
    }

    public ArrayList<AppUseDB> findByApp(String year, String month, String day, String appname) {
        ArrayList<AppUseDB> all = new ArrayList<>();
        DayTick dayTick = SQLiteUtils.getDayTick(year, month, day);
        Log.d("database", dayTick.getStartTick()+"");
//        Cursor result = this.db.query(SQLiteUtils.APPUSE_TABLE, null, SQLiteUtils.APPUSE_DAY+" = ?", new String[]{String.valueOf(dayTick.getStartTick())}, null, null, SQLiteUtils.APPUSE_USETIME+"DESC", null);
        Cursor result = this.db.query(SQLiteUtils.APPUSE_TABLE, null, SQLiteUtils.APPUSE_DAY+" = ? and "+SQLiteUtils.APPUSE_APPNAME+" = ?", new String[]{String.valueOf(dayTick.getStartTick()), appname}, null, null, null, null);
        Log.i("database", "get "+ String.valueOf(result.getCount()) + "db info");
        for (result.moveToFirst() ; !result.isAfterLast();result.moveToNext()) {
            AppUseDB appUseDB = new AppUseDB(result.getString(result.getColumnIndex(SQLiteUtils.APPUSE_PKGNAME)),
                    result.getString(result.getColumnIndex(SQLiteUtils.APPUSE_APPNAME)),
                    Long.valueOf(result.getString(result.getColumnIndex(SQLiteUtils.APPUSE_DAY))),
                    Integer.valueOf(result.getString(result.getColumnIndex(SQLiteUtils.APPUSE_USEHOUR))),
                    Integer.valueOf(result.getString(result.getColumnIndex(SQLiteUtils.APPUSE_USEMIN))),
                    Integer.valueOf(result.getString(result.getColumnIndex(SQLiteUtils.APPUSE_USESEC)))  );
            all.add(appUseDB);
        }
        result.close();
        this.db.close();
        return all;
    }

    public void update(long time, String appname, int hour, int minute, int sec) {
        ContentValues values = new ContentValues();
        values.put(SQLiteUtils.APPUSE_USEHOUR, hour);
        values.put(SQLiteUtils.APPUSE_USEMIN, minute);
        values.put(SQLiteUtils.APPUSE_USESEC, sec);
        this.db.update(SQLiteUtils.APPUSE_TABLE, values, SQLiteUtils.APPUSE_DAY+" = ? and "+SQLiteUtils.APPUSE_APPNAME+" = ?", new String[]{String.valueOf(time), appname});
        this.db.close();
    }


}
