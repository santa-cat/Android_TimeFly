package com.example.santa.lookupapp.service;

import android.content.Context;
import android.os.PowerManager;
import android.provider.Settings;

import com.example.santa.lookupapp.SQL.AppUseDB;
import com.example.santa.lookupapp.SQL.SQLOperatorManger;
import com.example.santa.lookupapp.SQL.SQLiteUtils;
import com.example.santa.lookupapp.utils.ActiveAppHelper;
import com.example.santa.lookupapp.utils.Utils;

/**
 * Created by santa on 16/7/9.
 */
public class AppUseManger {
    private final String DEBUG = "RecordManager";
    private SQLOperatorManger mSQLManger;
    private Context mContext;
    private AppUseDB mAppUse = new AppUseDB(null, null, 0, 0, 0, 0);

    public AppUseManger(Context context, SQLOperatorManger sqlOperatorManger) {
        mSQLManger = sqlOperatorManger;
        mContext = context;

    }

    //每次改变APP或者到了24点调用
    public void updateAppUseInfo() {
        final String curPackageName = ActiveAppHelper.getForegroundApp();
        final String curAppName = Utils.getLabel(mContext, curPackageName);
        final long curTime = System.currentTimeMillis();
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (curAppName !=null && curAppName.equals(mAppUse.getAppname()) && isTheSameDay(mAppUse.getDay(), curTime)) {
            return;
        }
        if (mAppUse.isUsed()) {
            mSQLManger.insertAppUse(mAppUse.getDay(), curTime, mAppUse.getAppname(), mAppUse.getPkgName());
        }
        mAppUse.reset();
        if (curAppName !=null) {
            mAppUse.setAppname(curAppName);
            mAppUse.setPkgName(curPackageName);
            mAppUse.setDay(curTime);
        }
    }

    private boolean isTheSameDay(long time1, long time2) {
        if (time1 == 0 | time2 == 0) {
            return false;
        }
        return SQLiteUtils.getDayTick(time1).getStartTick() == SQLiteUtils.getDayTick(time2).getStartTick();
    }

}
