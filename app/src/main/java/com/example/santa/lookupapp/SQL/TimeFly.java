package com.example.santa.lookupapp.SQL;

/**
 * Created by santa on 16/7/9.
 */
public class TimeFly {

    private int hourFly;
    private int minFly;
    private int secFly;

    public TimeFly(int hourFly, int minFly, int secFly) {
        this.hourFly = hourFly;
        this.minFly = minFly;
        this.secFly = secFly;
    }

    public int getHourFly() {
        return hourFly;
    }

    public int getMinFly() {
        return minFly;
    }

    public int getSecFly() {
        return secFly;
    }

    public void hourFly() {
        hourFly++;
    }

    public void minuteFly() {
        minFly++;
    }
}
