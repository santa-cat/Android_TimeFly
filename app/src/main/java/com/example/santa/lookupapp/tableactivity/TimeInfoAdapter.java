package com.example.santa.lookupapp.tableactivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.santa.lookupapp.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by santa on 16/7/9.
 */
public class TimeInfoAdapter extends RecyclerView.Adapter<TimeInfoAdapter.TimeViewHolder>{
    public final static String APP_NAME = "APP_NAME";
    public final static String APP_ICON = "APP_ICON";
    public final static String APP_TIME = "APP_TIME";
    public final static String APP_LINE = "APP_LINE";
    private List<HashMap<String, Object>> mListData;
    private Context mContext;

    public TimeInfoAdapter(Context context, List<HashMap<String, Object>> list) {
        mContext = context;
        mListData = list;
    }

    @Override
    public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_timeinfo, parent, false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimeViewHolder holder, int position) {
        holder.appName.setText(mListData.get(position).get(APP_NAME).toString());
        holder.appIcon.setImageDrawable((Drawable) mListData.get(position).get(APP_ICON));
        holder.appTime.setText(mListData.get(position).get(APP_TIME).toString());
        holder.appLine.setPercent((Float) mListData.get(position).get(APP_LINE));
    }


    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder {

        TextView appName;
        ImageView appIcon;
        LineView appLine;
        TextView appTime;

        public TimeViewHolder(View view) {
            super(view);
            appName = (TextView) view.findViewById(R.id.app_name);
            appIcon = (ImageView) view.findViewById(R.id.app_ic);
            appLine = (LineView) view.findViewById(R.id.app_line);
            appTime = (TextView) view.findViewById(R.id.app_time);
        }
    }
}
