package com.example.santa.lookupapp.SQL;

/**
 * Created by santa on 16/7/8.
 */
public class DayTick {
    private long startTick;
    private long endTick;

    public DayTick(long startTick, long endTick) {
        this.startTick = startTick;
        this.endTick = endTick;
    }

    public long getStartTick() {
        return startTick;
    }

    public void setStartTick(long startTick) {
        this.startTick = startTick;
    }

    public long getEndTick() {
        return endTick;
    }

    public void setEndTick(long endTick) {
        this.endTick = endTick;
    }
}
