package com.example.santa.lookupapp.mainactivity;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.example.santa.lookupapp.SQL.HourDB;
import com.example.santa.lookupapp.SQL.RecordDB;
import com.example.santa.lookupapp.SQL.SQLOperatorManger;
import com.example.santa.lookupapp.SQL.SQLiteUtils;
import com.example.santa.lookupapp.SQL.TimeFly;
import com.example.santa.lookupapp.mainactivity.RecordAdapter;
import com.example.santa.lookupapp.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by santa on 16/7/7.
 */
public class QueryAppInfoLoader extends AsyncTask<Void, Void, ArrayList<Record>> {
    private SQLOperatorManger mSQLManger;
    private final Listener listener;
    private Context mContext;
    private String mTime;

    public QueryAppInfoLoader(Context context, Listener listener) {
        this.mContext = context.getApplicationContext();
        this.listener = listener;
        this.mSQLManger = new SQLOperatorManger(context);
    }

    @Override protected ArrayList<Record> doInBackground(Void... params) {
        Calendar c = Calendar.getInstance();
        ArrayList<HourDB> hourDBList = mSQLManger.findHour(String.valueOf(c.get(Calendar.YEAR)), String.valueOf(c.get(Calendar.MONTH)), String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
        Log.d("SANTA", "hour size is ="+hourDBList.size());
        int hours = 0;
        int minutes =0;
        for (int i = 0 ; i<hourDBList.size(); i++) {
            HourDB hourDB = hourDBList.get(i);
            minutes = minutes+hourDB.getMinCount();
            if(minutes>=60) {
                minutes -= 60;
                hours ++;
            }
        }
        mTime = String.valueOf(hours)+":"+String.valueOf(minutes)+"\"";

        ArrayList<Record> recordList = new ArrayList<>();
        ArrayList<RecordDB> dbList = mSQLManger.find();
        for (int i = 0 ; i<dbList.size(); i++) {
            RecordDB r = dbList.get(i);
            Record record = new Record();
            String string = getTimeLag( r.getEndTime(), System.currentTimeMillis());
            string = (string.equals("")) ? "刚刚" : string+" ago";
//            map.put(RecordAdapter.ITEM_CONTENT, "您"+string+"使用了"+r.getAppname()+""+getTimeLag(r.getStartTime(), r.getEndTime()) +".");
//            map.put(RecordAdapter.ITEM_COLOR, Color.DKGRAY);
            record.setTimeStart(string);
            record.setTimeUsed(getTimeLag(r.getStartTime(), r.getEndTime()));
            record.setAppIcon(Utils.getIcon(mContext, r.getPkgName()));
            record.setAppName(r.getAppname());
            recordList.add(record);
        }
        return recordList;
    }

    @Override protected void onPostExecute(ArrayList<Record> list) {
        if (listener != null) {
            listener.onComplete(list, mTime);
        }
    }



    private String getTimeLag(long time1, long time2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTimeInMillis(time1);
        calendar2.setTimeInMillis(time2);
        if (calendar1.after(calendar2)) {
            return "";
        }

        TimeFly timeFly = SQLiteUtils.getTimeFly(time1, time2);
        timeFly.getMinFly();

        int minFly = timeFly.getMinFly();
        int hourfly = timeFly.getHourFly();
//        if(minFly<0) {
//            hourfly --;
//            minFly += 60;
//        }
        String hour = hourfly>0 ? String.valueOf(hourfly) + ":" : "";
        String min = minFly>0 ? String.valueOf(minFly) + "\"" : "";

        return hour.concat(min);
    }

    private String getStrByTick(int timeAfter, int timeBefore, String unit) {
        Log.d("getTimeLag", "timeAfter = "+timeAfter+" timeBefore = "+timeBefore);

        return (timeAfter>timeBefore) ? timeAfter - timeBefore+unit : "";
    }

    public interface Listener {

        void onComplete(ArrayList<Record> list, String mTimeString);
    }
}
