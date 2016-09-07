package com.example.santa.lookupapp.SQL;

/**
 * Created by santa on 16/7/8.
 */
public class AppUseDB {
    //packagename for get icon
    private String mPkgName = null;
    private String mAppname = null;
    //high for hours, low for mins
    private int useHour = 0;
    private int useMinute = 0;
    private int useSec = 0;
    private long day = 0;


    public AppUseDB(String mPkgName, String mAppname, long day, int useHour, int useMinute, int useSec) {
        this.mPkgName = mPkgName;
        this.mAppname = mAppname;
        this.day = day;
        this.useHour = useHour;
        this.useMinute = useMinute;
        this.useSec = useSec;
    }

//    public void setHour(int hour) {
//        useTime = (useTime + (hour<<8 & 0xff00));
//    }
//
//    public void setMinute(int minute) {
//        useTime = useTime + (minute & 0xff);
//    }
//
//    public int getUseTime() {
//        return useTime;
//    }
//
//    public int getHour() {
//        return (useTime >> 8) & 0xff;
//    }
//
//    public int getMinute() {
//        return (useTime) & 0xff;
//    }


    public int getUseHour() {
        return useHour;
    }

    public void setUseHour(int useHour) {
        this.useHour = useHour;
    }

    public int getUseMinute() {
        return useMinute;
    }

    public void setUseMinute(int useMinute) {
        this.useMinute = useMinute;
    }

    public int getUseSecond() {
        return useSec;
    }

    public String getPkgName() {
        return mPkgName;
    }

    public void setPkgName(String pkgName) {
        this.mPkgName = pkgName;
    }

    public String getAppname() {
        return mAppname;
    }

    public void setAppname(String appname) {
        this.mAppname = appname;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public void reset() {
        mAppname = mPkgName =null;
        day = useHour = useMinute = 0;
    }

    public boolean isVaild() {
        if (mAppname == null || mPkgName == null || day == 0 || useMinute == 0 || useHour == 0 || useSec == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isUsed() {
        if (mAppname == null || mPkgName == null || day == 0) {
            return false;
        } else {
            return true;
        }
    }
}
