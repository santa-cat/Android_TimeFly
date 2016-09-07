package com.example.santa.lookupapp.service;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import com.example.santa.lookupapp.SQL.RecordDB;
import com.example.santa.lookupapp.SQL.SQLOperatorManger;
import com.example.santa.lookupapp.utils.ActiveAppHelper;
import com.example.santa.lookupapp.utils.Utils;

/**
 * Created by santa on 16/7/9.
 */
public class RecordManager {
    private final String DEBUG = "RecordManager";
    private RecordDB mRecord = new RecordDB(null, null, -1, -1);
    private SQLOperatorManger mSQLManger;
    private Context mContext;

    public RecordManager(Context context, SQLOperatorManger sqlOperatorManger) {
        mSQLManger = sqlOperatorManger;
        mContext = context;

    }

    public void updateTimeInfo() {
        //需要转化成APPNAME
        final String curPackageName = ActiveAppHelper.getForegroundApp();
        final String curAppName = Utils.getLabel(mContext, curPackageName);
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (curAppName !=null && curAppName.equals(mRecord.getAppname()) && isScreenOn) {
            return;
        }
        final long curtime = System.currentTimeMillis();
        if (mRecord.isUsed()) {
            //插入数据库
            mRecord.setEndTime(curtime);
//            Log.d(DEBUG, "insert: name = "+mRecord.getAppname());
//            Log.d(DEBUG, "insert: start = "+mRecord.getStartTime());
//            Log.d(DEBUG, "insert: end = "+mRecord.getEndTime());
            mSQLManger.insert(getRecord());
        }
        mRecord.reset();
        //TODO:增加过滤条件: 1.是否为第三方应用; 2.是否为屏幕关闭
        if(curAppName != null && isScreenOn) {
            mRecord.setPkgName(curPackageName);
            mRecord.setAppname(curAppName);
            mRecord.setStartTime(curtime);
        }
    }



    private RecordDB getRecord() {
        if (mRecord.isVaild()) {
            return mRecord;
        } else {
            return null;
        }
    }

    //一定时间需要清除record表,或者数据库能不能设置最多容量
    public void clearData() {

    }

}
