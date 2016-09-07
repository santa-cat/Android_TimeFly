package com.example.santa.lookupapp.SQL;

/**
 * Created by santa on 16/7/8.
 */
public class HourDB {
    private long time;
    private int minCount;
    private int second;

    public HourDB(long time, int minCount, int second) {
        this.time = time;
        this.minCount = minCount;
        this.second = second;
    }

    public int getSecond() {
        return second;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }
}
