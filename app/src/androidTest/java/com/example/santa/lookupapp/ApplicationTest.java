package com.example.santa.lookupapp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.example.santa.lookupapp.SQL.AppUseDB;
import com.example.santa.lookupapp.SQL.AppUseDBOperator;
import com.example.santa.lookupapp.SQL.DayTick;
import com.example.santa.lookupapp.SQL.HourDB;
import com.example.santa.lookupapp.SQL.HourDBOperator;
import com.example.santa.lookupapp.SQL.RecordDB;
import com.example.santa.lookupapp.SQL.RecordDBOperator;
import com.example.santa.lookupapp.SQL.SQLOperatorManger;
import com.example.santa.lookupapp.SQL.SQLiteHelper;
import com.example.santa.lookupapp.SQL.SQLiteUtils;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private SQLiteDatabase db;
    private RecordDBOperator mRecordOp;
    private AppUseDBOperator mAppUseOp;
    private HourDBOperator mHoutOp;
    private SQLOperatorManger manger;

    public ApplicationTest() {
        super(Application.class);
    }

    public void testQLCreateTable(){
//        mSqlOp.insert("微信",mDate.getTime(),  mDate.getTime() );
        //mSqlOp.insert("支付宝",mDate.getTime(),  mDate.getTime() );
        //mSqlOp.insert("网易",mDate.getTime(),  mDate.getTime() );
    }


    public void testInsertTest() {
//        mSqlOp.insert("微信",mDate.getTime(),  mDate.getTime() );
//        mSqlOp.insert("支付宝",mDate.getTime(),  mDate.getTime() );
//        mSqlOp.insert("网易",mDate.getTime(),  mDate.getTime() );

    }

    public void testQuery() {
        ArrayList<RecordDB> arrayList = mRecordOp.find(0);
        for(int i = 0; i<arrayList.size();i++) {
//            Log.d("query", arrayList.get(i))
            System.out.println(arrayList.get(i));
        }
    }

    public void testDelete() {
        mRecordOp.deleteRecordBefore(System.currentTimeMillis());
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        SQLiteHelper mySQLhelper = new SQLiteHelper(getContext());
        db = mySQLhelper.getWritableDatabase();
        mRecordOp = new RecordDBOperator(db);
        mAppUseOp = new AppUseDBOperator(db);
        manger = new SQLOperatorManger(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if(db != null && db.isOpen()) {
            db.close();
        }
    }

    //appuse
    public void testinsertapp(){
        DayTick dayTick = SQLiteUtils.getDayTick("2016", "6", "9");
        mAppUseOp.insert(new AppUseDB("com.santa.ahah", "微信", dayTick.getStartTick(), 21, 58, 33));
    }

    public void testInsertappuse() {
        manger.insertAppUse(getTick("2016", "7", "7", "2", "21", "21"), getTick("2016", "7", "7", "2", "22", "59"), "weixin", "com.weixin");
    }

    public void testTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1468047600000L);
        Log.d("SANTA", calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+ " "+calendar.get(Calendar.HOUR_OF_DAY) +":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND));
        Log.d("SANTA", System.currentTimeMillis()+"");
        calendar.setTimeInMillis(System.currentTimeMillis());
        Log.d("SANTA", calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DAY_OF_MONTH));


    }

    public void testTime1() {
        String year = "2016";
        String month = "6";
        String day = "9";
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(0);
        calendar1.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
        Log.d("SANTA", calendar1.get(Calendar.YEAR)+"-"+calendar1.get(Calendar.MONTH)+"-"+calendar1.get(Calendar.DAY_OF_MONTH) + " "+calendar1.get(Calendar.HOUR_OF_DAY) +":"+calendar1.get(Calendar.MINUTE)+":"+calendar1.get(Calendar.SECOND));

    }

    public void testfindapp() {
        ArrayList<AppUseDB> appUseDB = mAppUseOp.findByApp("2016", "7", "9", "LookUpApp");
        for (int i =0 ; i<appUseDB.size(); i++) {
            AppUseDB app = appUseDB.get(i);
            Log.d("TEST", "aaa"+app.getAppname().concat(String.valueOf(app.getUseHour()))+" "+app.getUseMinute());
        }
    }

    public void testFindAll() {
        ArrayList<AppUseDB> appUseDB = mAppUseOp.find("2016", "6", "9");
        for (int i =0 ; i<appUseDB.size(); i++) {
            AppUseDB app = appUseDB.get(i);
            Log.d("TEST", "aaa"+app.getAppname().concat(String.valueOf(app.getUseHour()))+" "+app.getUseMinute());
        }
    }

    public long getTick(String year, String month, String day ,String hour, String min, String sec){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), Integer.valueOf(hour), Integer.valueOf(min), Integer.valueOf(sec));
        Log.d("TEST", "hour = "+calendar1.get(Calendar.HOUR_OF_DAY)+ " min = "+calendar1.get(Calendar.MINUTE));
        return calendar1.getTimeInMillis();
    }


    public void testInsert(){
        manger.insertAppUse(getTick("2016", "7", "7", "2", "22", "33"), getTick("2016", "7", "7", "23", "20", "44"), "android", "com.momo.haha");
    }


    //houtDB
    public void testInsertHour() {
        manger.insertHour(getTick("2016", "7", "7", "4", "5", "0"), getTick("2016", "7", "7", "4", "5", "30"));
    }

    public void testFindHour() {
        ArrayList<HourDB> arrayList = manger.findHour("2016", "6", "11");
        for (int i =0 ; i<arrayList.size(); i++) {
            HourDB hourDB = arrayList.get(i);
            Log.d("TEST", "aaa");
        }
    }

//    public void testFindDays() {
//        ArrayList<SQLOperatorManger.Day> day = manger.findDays();
//        for (int i = 0 ; i<day.size(); i++) {
//            Log.d("TEST", day.get(i).getYear()+day.get(i).getMonth()+day.get(i).getDay());
//        }
//
//    }

}