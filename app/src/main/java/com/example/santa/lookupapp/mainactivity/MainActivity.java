package com.example.santa.lookupapp.mainactivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.santa.lookupapp.R;
import com.example.santa.lookupapp.tableactivity.TimeInfoActivity;
import com.example.santa.lookupapp.service.TimeService;
import com.jaredrummler.android.processes.models.ActiveAppHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private TimeService mService;
    private TimeView mTimeView ;
    private TextView mDate;
    private TextView mTimeUsed;
    private Switch mSwitch;

    private ServiceConnection mTimeServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((TimeService.TimeBinder) service).getTimeService();
            mService.setChangeListener(new TimeChangeListener() {
                @Override
                public void onChange(String appname) {
                    Log.d("SERVICE", appname);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    public void initView() {

        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TimeInfoActivity.class);
                startActivity(i);
            }
        };

        mTimeView = (TimeView) findViewById(R.id.time_view);
        assert mTimeView != null;
        mTimeView.setOnClickListener(ocl);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new SpaceItemDecoration(2));


        ImageView table = (ImageView) findViewById(R.id.image_table);
        table.setOnClickListener(ocl);

        mSwitch = (Switch) findViewById(R.id.main_switch);
        mSwitch.setChecked(SwitchOptions.readSwitchState(this));
//        Log.d("DEBUG", "switch is "+mSwitch.isChecked());
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SwitchOptions.saveSiwtchState(MainActivity.this, isChecked);
                if (isChecked) {
                    startTimeService();
                } else {
                    stopTimeService();
                }
            }
        });

        mDate = (TextView) findViewById(R.id.main_date);
        mTimeUsed = (TextView) findViewById(R.id.main_timeused);

    }
    public void setTimInfo(String time) {
        mTimeUsed.setText(time);
        Calendar calendar = Calendar.getInstance();
        String date = String.valueOf(calendar.get(Calendar.MONTH)+1)+"-"+String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        mDate.setText(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("DEBUG", "onResume");

        initRecordLoader();
    }

    public void initRecordLoader() {
        new QueryAppInfoLoader(MainActivity.this, new QueryAppInfoLoader.Listener() {
            @Override
            public void onComplete(ArrayList<Record> list, String timeStr) {
                Log.d("DEBUG", "onResume loader onComplete");
                mRecyclerView.setAdapter(new RecordAdapter(MainActivity.this, list));
//                mTimeView.setTimInfo(timeStr);
                setTimInfo(timeStr);
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void startTimeService() {
        Intent startIntent = new Intent(this, TimeService.class);
        startService(startIntent);
        Toast.makeText(MainActivity.this, "开始统计",Toast.LENGTH_SHORT).show();
    }

    public void stopTimeService() {
        Intent endIntent = new Intent(this, TimeService.class);
        stopService(endIntent);
    }
}
