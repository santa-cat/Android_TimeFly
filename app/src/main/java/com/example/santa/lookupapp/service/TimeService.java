package com.example.santa.lookupapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.santa.lookupapp.SQL.RecordDB;
import com.example.santa.lookupapp.SQL.SQLOperatorManger;
import com.example.santa.lookupapp.mainactivity.TimeChangeListener;
import com.example.santa.lookupapp.utils.ActiveAppHelper;
import com.example.santa.lookupapp.utils.Utils;

/**
 * Created by santa on 16/7/7.
 */
public class TimeService extends Service {
    private final String DEBUG = "TimeService";
    private boolean isFinish = false;
    private int time = 5000;
    private TimeChangeListener mTimeChangeListener;
    private TimeBinder mTimeBinder = new TimeBinder();
    private SQLOperatorManger mSQLManger = new SQLOperatorManger(this);
    private RecordManager mRecordManager;
    private AppUseManger mAppUseManger;
    private HourManger mHourManger;
    private int mTimeStep = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mTimeBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isFinish = false;
        mRecordManager = new RecordManager(this, mSQLManger);
        mAppUseManger = new AppUseManger(this, mSQLManger);
        mHourManger = new HourManger(this, mSQLManger);
    }

    //忘数据库写入数据
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isFinish) {
                    try {
                        Thread.sleep(time);
                        mRecordManager.updateTimeInfo();
                        mAppUseManger.updateAppUseInfo();
                        if(mTimeStep == 60000/time) {
                            mHourManger.updateHourInfo();
                            mTimeStep = 0;
                        }
                        mTimeStep ++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isFinish = true;
    }

    public void setChangeListener(TimeChangeListener tcl) {
        mTimeChangeListener = tcl;
    }


    public class TimeBinder extends Binder{

        public TimeService getTimeService() {
            return TimeService.this;
        }
    }


}
