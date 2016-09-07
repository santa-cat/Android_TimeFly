package com.example.santa.lookupapp;

import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.example.santa.lookupapp.SQL.AppUseDB;
import com.example.santa.lookupapp.SQL.DayTick;
import com.example.santa.lookupapp.SQL.SQLiteHelper;
import com.example.santa.lookupapp.SQL.SQLiteUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by santa on 16/7/8.
 */
public class SQLiteTest extends InstrumentationTestCase{

    public void testSQLCreateTable(){
//        SQLiteHelper mySQLhelper = new SQLiteHelper(getContext());
//        SQLiteDatabase db = mySQLhelper.getWritableDatabase();

    }

    public void testgetDayTick() {

        DayTick dayTick = SQLiteUtils.getDayTick("2016", "7", "8");
        Log.d("TEST", ""+ System.currentTimeMillis());
        Log.d("TEST", "start tick = "+dayTick.getStartTick()+", end tick ="+dayTick.getEndTick());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dayTick.getStartTick());
        Log.d("TEST", "year = "+calendar.get(Calendar.YEAR)+" month = "+calendar.get(Calendar.MONTH)+" day = "+calendar.get(Calendar.DAY_OF_MONTH));
    }

//    public void testAppUseDBUseTime() {
//        AppUseDB appUseDB = new AppUseDB("hh", "jj", 1233, 0x908);
//        Log.d("TEST", "hour = "+ appUseDB.getHour()+" min = "+appUseDB.getMinute());
//        appUseDB.setHour(12);
//        appUseDB.setMinute(33);
//        Log.d("TEST", "hour = "+ appUseDB.getHour()+" min = "+appUseDB.getMinute());
//    }
}
