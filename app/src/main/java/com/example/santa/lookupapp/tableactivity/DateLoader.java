package com.example.santa.lookupapp.tableactivity;

import android.content.Context;
import android.os.AsyncTask;

import com.example.santa.lookupapp.SQL.Day;
import com.example.santa.lookupapp.SQL.SQLOperatorManger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by santa on 16/7/9.
 */
public class DateLoader extends AsyncTask<Void, Void, ArrayList<Day>>{
    private ArrayList<Day> days = null;
    private Listener mListener;
    private SQLOperatorManger mSQLManger;
    private int curDay = -1;

    public DateLoader(Context context, Listener listener) {
        mListener = listener;
        this.mSQLManger = new SQLOperatorManger(context);
    }


    @Override
    protected ArrayList<Day> doInBackground(Void... params) {
        //第一次需要查询
        if (days == null || curDay == -1) {
            days = mSQLManger.findDays();
            curDay = days.size()-1;
        }
        return days;
    }


    @Override
    protected void onPostExecute(ArrayList<Day> days) {
        super.onPostExecute(days);
        if (mListener != null) {
            mListener.onComplete(days, curDay);
        }
    }

    public interface Listener {

        void onComplete(ArrayList<Day> days, int index);
    }
}
