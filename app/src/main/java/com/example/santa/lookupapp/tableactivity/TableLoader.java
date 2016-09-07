package com.example.santa.lookupapp.tableactivity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.santa.lookupapp.SQL.AppUseDB;
import com.example.santa.lookupapp.SQL.Day;
import com.example.santa.lookupapp.SQL.HourDB;
import com.example.santa.lookupapp.SQL.SQLOperatorManger;
import com.example.santa.lookupapp.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by santa on 16/7/9.
 */
public class TableLoader extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {

    private Context mContext;
    private Listener mListener;
    private SQLOperatorManger mSQLManger;
    private ArrayList<Integer> mList;
    private Day day;

    public TableLoader(Context context, Listener listener) {
        mListener = listener;
        mContext = context;
        this.mSQLManger = new SQLOperatorManger(context);
    }

    public TableLoader(Context context, Day day, Listener listener) {
        mListener = listener;
        mContext = context;
        this.mSQLManger = new SQLOperatorManger(context);
        this.day = day;
    }


    public void setDay(Day day) {
        this.day = day;
    }

    @Override
    protected ArrayList<HashMap<String, Object>> doInBackground(Void... params) {


        //折线图
        mList = new ArrayList<>();
        ArrayList<HourDB> hourDBList = mSQLManger.findHour(day.getYear(), day.getMonth(), day.getDay());
        Log.d("SANTA", "hour size is ="+hourDBList.size());
        Log.d("SANTA", "day ="+day.getYear()+day.getMonth()+day.getDay());
        for (int i = 0 , j = 0; i<24; i++) {
            HourDB hourDB = null;
            int houIdx = 0;
            if (j<hourDBList.size()) {
                hourDB = hourDBList.get(j);
                houIdx = getHourIdx(hourDB.getTime());
            }
            if (i+1 == houIdx && hourDB != null) {
                mList.add(hourDB.getMinCount());
                j++;
            } else {
                mList.add(0);
            }
        }

        //
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        ArrayList<AppUseDB> appUseDBList = mSQLManger.finAppUse(day.getYear(), day.getMonth(), day.getDay());
        int timeSum = getSumAppUseTime(appUseDBList);
        for (int i = 0 ; i<appUseDBList.size(); i++) {
            AppUseDB a = appUseDBList.get(i);
            if(a.getUseHour() <=0 && a.getUseMinute() <= 0) {
                continue;
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put(TimeInfoAdapter.APP_NAME, a.getAppname());
            map.put(TimeInfoAdapter.APP_TIME, getStrByTime(a.getUseHour(), ":")+getStrByTime(a.getUseMinute(), "\""));
            map.put(TimeInfoAdapter.APP_ICON, Utils.getIcon(mContext, a.getPkgName()));
            map.put(TimeInfoAdapter.APP_LINE, (a.getUseHour()*60+a.getUseMinute())*1.0f/timeSum);
            list.add(map);
        }

        return list;
    }

    private String getStrByTime(int time, String unit) {
        return (time>0) ? String.valueOf(time) +unit : "";
    }

    private int getHourIdx(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.HOUR_OF_DAY)+1;
    }


    private int getSumAppUseTime(ArrayList<AppUseDB> list) {
        int timeMax = 0;
        for (int i = 0; i<list.size(); i++) {
            AppUseDB a = list.get(i);
            if(a.getUseHour() <=0 && a.getUseMinute() <= 0) {
                continue;
            }
            int timeNew = a.getUseHour()*60 + a.getUseMinute();
            timeMax = (timeNew > timeMax) ? timeNew : timeMax;
        }
        return timeMax;
    }

    @Override
    protected void onPostExecute(ArrayList<HashMap<String, Object>> list) {
        super.onPostExecute(list);
        if (mListener != null) {
            mListener.onComplete(list, mList);
        }

    }

    public interface Listener {

        void onComplete(ArrayList<HashMap<String, Object>> list1, ArrayList<Integer> list2);
    }
}
