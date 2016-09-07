package com.example.santa.lookupapp.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by santa on 16/7/8.
 */
public class SQLOperatorManger {
    private List<RecordDB> mRecordList;
    private RecordDBOperator mRecordOp;
    private SQLiteHelper mSqlHelper;
    private AppUseDBOperator mAppUseOp;
    private HourDBOperator mHourOp;

    public SQLOperatorManger(Context context){
        mRecordList = new ArrayList<>();
        mSqlHelper = new SQLiteHelper(context);
    }

    //RECORD
    private void pre() {
        SQLiteDatabase db = mSqlHelper.getWritableDatabase();
        mRecordOp = new RecordDBOperator(db);
    }

    public void insert(RecordDB record) {
        if (null == record) {
            return;
        }
        //如果到了24点,清空所有record
        if(SQLiteUtils.getDayTick(record.getStartTime()).getStartTick() != SQLiteUtils.getDayTick(record.getEndTime()).getStartTick()) {
            deleteBefore();
            record.setStartTime(SQLiteUtils.getDayTick(record.getEndTime()).getStartTick());
        }
        pre();
        mRecordOp.insert(record);

    }

    public ArrayList<RecordDB> find() {
        //清除前一天以及以前的记录
        long curTick = System.currentTimeMillis();
        deleteBefore(SQLiteUtils.getDayTick(curTick).getStartTick());
        pre();
        return mRecordOp.find(-1);
    }

    public void deleteBefore(long tick) {
        pre();
        mRecordOp.deleteRecordBefore(tick);
    }


    public void deleteBefore() {
        pre();
        mRecordOp.deleteRecordBefore(System.currentTimeMillis());
    }


    //APPUSE
    public void insertAppUse(long time1, long time2, String appname, String pkgname) {
        preAppuse();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(time1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(time2);
        if (calendar2.before(calendar1)) {
            return;
        }
        ArrayList<AppUseDB> list = findByAppName(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH), appname);
        Log.d("SQLmanager", "size is = "+list.size());
        if (list.size()==0) {
            //insert
            preAppuse();
            TimeFly timeFly = SQLiteUtils.getTimeFly(time1, time2);
            mAppUseOp.insert(new AppUseDB(pkgname, appname, SQLiteUtils.getDayTick(time1).getStartTick(), timeFly.getHourFly(), timeFly.getMinFly(), timeFly.getSecFly()));
            Log.d("SQLmanager", "appuse insert a new message");
            return;
        } else if(list.size()==1){
            //update
            preAppuse();
            AppUseDB appUseDB = list.get(0);
            TimeFly timeFly = SQLiteUtils.getTimeFly(time1, time2, appUseDB.getUseHour(), appUseDB.getUseMinute(), appUseDB.getUseSecond());
            mAppUseOp.update(SQLiteUtils.getDayTick(time1).getStartTick(), appname, timeFly.getHourFly(), timeFly.getMinFly(), timeFly.getSecFly());
            Log.d("SQLmanager", "appuse update a message");

        } else {
            Log.e("SQLmanager", "appuse size is illeage = "+list.size());
        }

    }

    private void preAppuse() {
        SQLiteDatabase db = mSqlHelper.getWritableDatabase();
        mAppUseOp = new AppUseDBOperator(db);
    }

    //根据appname查询现有的记录
    private ArrayList<AppUseDB> findByAppName(int year, int month, int day, String appname) {
        preAppuse();
        return mAppUseOp.findByApp(String.valueOf(year), String.valueOf(month), String.valueOf(day), appname);
    }

    public ArrayList<AppUseDB> finAppUse(String year, String month, String day) {
        preAppuse();
        return mAppUseOp.find(year, month, day);
    }


    //hour
    private void preHour() {
        SQLiteDatabase db = mSqlHelper.getWritableDatabase();
        mHourOp = new HourDBOperator(db);
    }

    public void insertHour(long time1, long time2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(time1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(time2);
        if (calendar2.before(calendar1)) {
            return;
        }
        HourDB hourDB = findByHour(time1);

        if (hourDB == null) {
            TimeFly timeFly = SQLiteUtils.getTimeFly(time1, time2);
            preHour();
            mHourOp.insert(new HourDB(SQLiteUtils.getHourTick(time1).getStartTick(), timeFly.getMinFly(), timeFly.getSecFly()));
            Log.d("SQLmanager", "hour insert a new message");
        } else {
            TimeFly timeFly = SQLiteUtils.getTimeFly(time1, time2);
            preHour();
            int min = timeFly.getMinFly()+hourDB.getMinCount();
            int sec = timeFly.getSecFly()+hourDB.getSecond();
            min = (sec >= 60) ? min + 1 : min;
            sec = (sec >= 60) ? sec-60 : sec;
            mHourOp.update(SQLiteUtils.getHourTick(time1).getStartTick(),  min, sec);
            Log.d("SQLmanager", "hour update a new message");
        }
    }

    private HourDB findByHour(long hourTick) {
        preHour();
        return mHourOp.findByTick(SQLiteUtils.getHourTick((hourTick)).getStartTick());
    }

    public ArrayList<HourDB> findHour(String year, String month, String day) {
        preHour();
        DayTick dayTick = SQLiteUtils.getDayTick(year, month, day);
        return mHourOp.find(dayTick.getStartTick(), dayTick.getEndTick());
    }

    public ArrayList<Day> findDays() {
        preHour();
        ArrayList<HourDB> list = mHourOp.find();
        ArrayList<Day> days = new ArrayList<>();
        int dayCount = 0;
        long dayTick = 0;
        for (int i = 0 ; i<list.size(); i++) {
            long startTick = SQLiteUtils.getDayTick(list.get(i).getTime()).getStartTick();
            if (startTick != dayTick) {
                dayTick = startTick;
                days.add(getDay(dayTick));
            }
        }
        return days ;
    }

    public Day getDay(long tick) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tick);
        return new Day(String.valueOf(c.get(Calendar.YEAR)), String.valueOf(c.get(Calendar.MONTH)+1), String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
    }

}
