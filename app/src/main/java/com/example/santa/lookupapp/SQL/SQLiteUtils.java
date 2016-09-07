package com.example.santa.lookupapp.SQL;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by santa on 16/7/8.
 */
public class SQLiteUtils {
    public static final String DATABASE_NAME="lookup.db";

    public static final String RECORD_TABLE="timerecord";
    public static final String RECORD_PKGNAME="packagename";
    public static final String RECORD_APPNAME="appname";
    public static final String RECORD_STARTTIME="starttime";
    public static final String RECORD_ENDTIME="endtime";

    public static final String APPUSE_TABLE = "appuseinfo";
    public static final String APPUSE_PKGNAME="packagename";
    public static final String APPUSE_APPNAME = "appname";
    public static final String APPUSE_USEHOUR = "usehour";
    public static final String APPUSE_USEMIN = "useminute";
    public static final String APPUSE_USESEC = "usesecond";
    public static final String APPUSE_DAY = "day";

    public static final String HOUR_TABLE = "houruseinfo";
    public static final String HOUR_TIME="hourtime";
    public static final String HOUR_USEMIN = "usetimemin";
    public static final String HOUR_USESEC = "usetimesec";

    public static DayTick getDayTick(String year, String month, String day) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(0);
        calendar1.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day) ,0 ,0 ,0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(0);
        calendar2.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day)+1, 0, 0, 0);
//        Date dateStart = new Date();
//        Date dateEnd = new Date(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day+1));
        return new DayTick(calendar1.getTimeInMillis(), calendar2.getTimeInMillis());
    }

    public static DayTick getDayTick(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return getDayTick(String.valueOf(calendar.get(Calendar.YEAR)), String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
    }

    public static DayTick getHourTick(String year, String month, String day, String hour) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(0);
        calendar1.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), Integer.valueOf(hour), 0 ,0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(0);
        calendar2.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day), Integer.valueOf(hour)+1, 0, 0);
//        Date dateStart = new Date();
//        Date dateEnd = new Date(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day+1));
        return new DayTick(calendar1.getTimeInMillis(), calendar2.getTimeInMillis());
    }

    public static DayTick getHourTick(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return getHourTick(String.valueOf(calendar.get(Calendar.YEAR)), String.valueOf(calendar.get(Calendar.MONTH)),
                String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
    }

    public static int getHour(long time1, long time2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(time1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(time2);

        return calendar2.get(Calendar.HOUR_OF_DAY)-calendar1.get(Calendar.HOUR_OF_DAY);

    }

    public static int getMinute(long time1, long time2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(time1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(time2);

        return calendar2.get(Calendar.MINUTE)-calendar1.get(Calendar.MINUTE);

    }

    public static int getSecond(long time1, long time2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(time1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(time2);

        return calendar2.get(Calendar.SECOND)-calendar1.get(Calendar.SECOND);

    }

    public static TimeFly getTimeFly(long time1, long time2) {
        int houfly = getHour(time1, time2);
        int minutefly = getMinute(time1, time2);
        int secondfly = getSecond(time1, time2);

        if(secondfly<0) {
            minutefly --;
            secondfly += 60;
        }else if(secondfly>=60) {
            minutefly ++;
            secondfly -= 60;
        }
        if (minutefly<0) {
            houfly --;
            minutefly += 60;
        } else if (minutefly >= 60) {
            houfly++;
            minutefly -=60;
        }
        return new TimeFly(houfly, minutefly, secondfly);
    }

    public static TimeFly getTimeFly(long time1, long time2, int hour, int min, int sec) {
        TimeFly timeFly = getTimeFly(time1, time2);
        int hourNew = timeFly.getHourFly() + hour;
        int minNew = timeFly.getMinFly() + min;
        int secNew = timeFly.getSecFly() + sec;
        if (secNew >= 60) {
            minNew ++;
            secNew -= 60;
        }
        if (minNew >= 60) {
            hourNew++;
            minNew -= 60;
        }
        return new TimeFly(hourNew, minNew, secNew);
    }

}
