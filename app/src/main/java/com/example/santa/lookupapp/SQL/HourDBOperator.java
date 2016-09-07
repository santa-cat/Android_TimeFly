package com.example.santa.lookupapp.SQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by santa on 16/7/8.
 */
public class HourDBOperator {
    private SQLiteDatabase db = null;

    public HourDBOperator(SQLiteDatabase db) {
        this.db = db;
    }

    public void insert(HourDB hour) {
        ContentValues values = new ContentValues();
        values.put(SQLiteUtils.HOUR_TIME, hour.getTime());
        values.put(SQLiteUtils.HOUR_USEMIN, hour.getMinCount());
        values.put(SQLiteUtils.HOUR_USESEC, hour.getSecond());
        this.db.insert(SQLiteUtils.HOUR_TABLE, null, values);
        this.db.close();
    }


    public void update(long time, int min, int sec) {
        if(min > 60){
            min = 60;
        }
        ContentValues values = new ContentValues();
        values.put(SQLiteUtils.HOUR_USEMIN, min);
        values.put(SQLiteUtils.HOUR_USESEC, sec);
        this.db.update(SQLiteUtils.HOUR_TABLE, values, SQLiteUtils.HOUR_TIME+" = ?", new String[]{String.valueOf(time)});
        this.db.close();
    }

    public HourDB findByTick(long time) {
        HourDB hourDB = null;
        Cursor result = this.db.query(SQLiteUtils.HOUR_TABLE, null, SQLiteUtils.HOUR_TIME+" = ?", new String[]{String.valueOf(time)}, null, null, null, null);
        Log.i("database", "get "+ String.valueOf(result.getCount()) + "db info");
        for (result.moveToFirst() ; !result.isAfterLast();result.moveToNext()) {
            hourDB = new HourDB(result.getLong(result.getColumnIndex(SQLiteUtils.HOUR_TIME)),
                    result.getInt(result.getColumnIndex(SQLiteUtils.HOUR_USEMIN)),
                    result.getInt(result.getColumnIndex(SQLiteUtils.HOUR_USESEC)));
        }
        result.close();
        this.db.close();
        return hourDB;
    }

    public ArrayList<HourDB> find(long time1, long time2) {
        ArrayList<HourDB> all = new ArrayList<>();
        Log.d("database", "time1 = "+time1+" time2 = "+time2);
        Cursor result = this.db.query(SQLiteUtils.HOUR_TABLE, null, SQLiteUtils.HOUR_TIME+" >= ? and "+SQLiteUtils.HOUR_TIME+" < ?", new String[]{String.valueOf(time1), String.valueOf(time2)}, null, null, null, null);
        Log.i("database", "get "+ String.valueOf(result.getCount()) + "db info");
        for (result.moveToFirst() ; !result.isAfterLast();result.moveToNext()) {
            HourDB hourDB = new HourDB(result.getLong(result.getColumnIndex(SQLiteUtils.HOUR_TIME)),
                    result.getInt(result.getColumnIndex(SQLiteUtils.HOUR_USEMIN)),
                    result.getInt(result.getColumnIndex(SQLiteUtils.HOUR_USESEC)));
            all.add(hourDB);
        }
        result.close();
        this.db.close();
        return all;
    }

    public ArrayList<HourDB> find() {
        ArrayList<HourDB> all = new ArrayList<>();
        Cursor result = this.db.query(SQLiteUtils.HOUR_TABLE, null, null, null, null, null, null, null);
        Log.i("database", "get "+ String.valueOf(result.getCount()) + "db info");
        for (result.moveToFirst() ; !result.isAfterLast();result.moveToNext()) {
            HourDB hourDB = new HourDB(result.getLong(result.getColumnIndex(SQLiteUtils.HOUR_TIME)),
                    result.getInt(result.getColumnIndex(SQLiteUtils.HOUR_USEMIN)),
                    result.getInt(result.getColumnIndex(SQLiteUtils.HOUR_USESEC)));
            all.add(hourDB);
        }
        result.close();
        this.db.close();
        return all;
    }
}
