package com.example.santa.lookupapp.mainactivity;

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
 * Created by santa on 16/7/8.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordHolder>{
    private List<Record> mData;
    public final static String ITEM_COLOR = "ITEM_COLOR";
    private Context mContext;

    public RecordAdapter(Context context, List<Record> list) {
        mContext = context;
        mData = list;

    }

    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_record, parent, false);
        return new RecordHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordHolder holder, int position) {
        holder.recordIcon.setImageDrawable(mData.get(position).getAppIcon());
        holder.recordContent.setText(mData.get(position).getTimeUsed());
        holder.recordStartTime.setText(mData.get(position).getTimeStart());
        holder.recordAppname.setText(mData.get(position).getAppName());
        if (position == 0) {
            holder.timeLineIdxView.setState(TimeLineIdxView.STATE.TOP);
        } else if (position == getItemCount()-1) {
            holder.timeLineIdxView.setState(TimeLineIdxView.STATE.BOTTOM);
        } else {
            holder.timeLineIdxView.setState(TimeLineIdxView.STATE.COMM);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class RecordHolder extends RecyclerView.ViewHolder {

        private TextView recordContent;
        private ImageView recordIcon;
        private TextView recordAppname;
        private TextView recordStartTime;
        private TimeLineIdxView timeLineIdxView;

        public RecordHolder(View itemView) {
            super(itemView);
            recordStartTime = (TextView) itemView.findViewById(R.id.record_startTime);
            timeLineIdxView = (TimeLineIdxView) itemView.findViewById(R.id.record_line);
            recordContent = (TextView) itemView.findViewById(R.id.record_content);
            recordIcon = (ImageView) itemView.findViewById(R.id.record_ic);
            recordAppname = (TextView) itemView.findViewById(R.id.record_appname);
        }
    }
}