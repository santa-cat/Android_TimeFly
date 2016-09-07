package com.example.santa.lookupapp.service;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import com.example.santa.lookupapp.SQL.HourDB;
import com.example.santa.lookupapp.SQL.SQLOperatorManger;
import com.example.santa.lookupapp.utils.ActiveAppHelper;
import com.example.santa.lookupapp.utils.Utils;

/**
 * Created by santa on 16/7/9.
 */
public class HourManger {
//    private HourDB mHour = new HourDB();
    private long lastTick;
    private SQLOperatorManger mSQLmanger;
    private Context mContext;


    public HourManger(Context context, SQLOperatorManger sqlOperatorManger) {
        mContext =context;
        mSQLmanger = sqlOperatorManger;
    }

    //每一分钟调用一次
    public void updateHourInfo() {
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        final long curTick = System.currentTimeMillis();
        if (lastTick != 0) {
            mSQLmanger.insertHour(lastTick, curTick);
        }
        lastTick = pm.isScreenOn() ? curTick : 0;

    }
}
