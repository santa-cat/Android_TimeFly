package com.example.santa.lookupapp.SQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by santa on 16/7/8.
 */
public class RecordDBOperator {
    private SQLiteDatabase db = null;

    public RecordDBOperator(SQLiteDatabase db) {
        this.db = db;
    }

    public void insert(RecordDB record) {
        if(record.getStartTime() >= record.getEndTime()) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(SQLiteUtils.RECORD_PKGNAME, record.getPkgName());
        values.put(SQLiteUtils.RECORD_APPNAME, record.getAppname());
        values.put(SQLiteUtils.RECORD_STARTTIME, record.getStartTime());
        values.put(SQLiteUtils.RECORD_ENDTIME, record.getEndTime());
        db.insert(SQLiteUtils.RECORD_TABLE, null, values);
        db.close();
    }

    //TODO:删除前面的记录
    public void deleteRecordBefore(long curTime) {
        db.delete(SQLiteUtils.RECORD_TABLE, SQLiteUtils.RECORD_STARTTIME +" < ?", new String[]{String.valueOf(curTime)});
        db.close();
    }



    public void deleteByAppname(String appname) {
        db.delete(SQLiteUtils.RECORD_TABLE, SQLiteUtils.RECORD_APPNAME+" = ?", new String[]{appname});
    }



    public ArrayList<RecordDB> find(int limit) {
        ArrayList<RecordDB> all = new ArrayList<>();
        String lim = (limit == -1) ? null : String.valueOf(limit);
        Cursor result = this.db.query(SQLiteUtils.RECORD_TABLE, null, null, null, null, null, SQLiteUtils.RECORD_STARTTIME+" DESC", lim);
        Log.i("database", "get "+ String.valueOf(result.getCount()) + "db info");
        for(result.moveToFirst();!result.isAfterLast();result.moveToNext()) {

            RecordDB q = new RecordDB(result.getString(result.getColumnIndex(SQLiteUtils.RECORD_PKGNAME)), result.getString(result.getColumnIndex(SQLiteUtils.RECORD_APPNAME)),
                    Long.valueOf(result.getString(result.getColumnIndex(SQLiteUtils.RECORD_STARTTIME))), Long.valueOf(result.getString(result.getColumnIndex(SQLiteUtils.RECORD_ENDTIME))));
            all.add(q);
        }
        result.close();
        this.db.close();
        return all;
    }

}
