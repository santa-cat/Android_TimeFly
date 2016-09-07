package com.example.santa.lookupapp.SQL;

/**
 * Created by santa on 16/7/10.
 */

public class Day{

    String year;

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    String month;
    String day;

    public Day(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

}