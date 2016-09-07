package com.example.santa.lookupapp.SQL;

import android.util.Log;

/**
 * Created by santa on 16/7/8.
 */
public class RecordDB {
    private String mPkgName = null;
    private String mAppname = null;
    private long mStartTime = -1;
    private long mEndTime = -1;

    public RecordDB(String pkgName, String appname, long start, long end) {
        mPkgName = pkgName;
        mAppname = appname;
        mStartTime = start;
        mEndTime = end;
    }

    public String getPkgName(){
        return mPkgName;
    }

    public String getAppname() {
        return mAppname;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public void setPkgName(String name) {
        mPkgName = name;
    }

    public void setAppname(String name) {
        mAppname = name;
    }

    public void setStartTime(long startTime) {
        mStartTime = startTime;
    }

    public void setEndTime(long endTime) {
        mEndTime = endTime;
    }

    public boolean isVaild() {
        if (mPkgName == null || mAppname == null || mStartTime == -1 || mEndTime == -1){
            return false;
        } else if(mStartTime < mEndTime){
            return true;
        }
        return false;
    }

    public void reset() {
        mPkgName = null;
        mAppname = null;
        mStartTime = -1;
        mEndTime = -1;
    }


    public boolean isUsed() {
        if(mPkgName == null || mAppname == null || mStartTime == -1) {
            return false;
        } else {
            return true;
        }
    }

}
