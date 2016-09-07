package com.example.santa.lookupapp.mainactivity;

import android.graphics.drawable.Drawable;

/**
 * Created by santa on 16/8/29.
 */
public class Record {
    private String timeStart;
    private String timeUsed;
    private Drawable appIcon;
    private String appName;


    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(String timeUsed) {
        this.timeUsed = timeUsed;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
