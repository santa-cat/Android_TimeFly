package com.example.santa.lookupapp.tableactivity;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.santa.lookupapp.R;
import com.example.santa.lookupapp.SQL.Day;
import com.example.santa.lookupapp.tableactivity.LineView;
import com.example.santa.lookupapp.tableactivity.TableView;
import com.example.santa.lookupapp.utilview.LetterDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by santa on 16/7/6.
 */
public class TimeInfoActivity extends AppCompatActivity{

    private TableView mTableView;
    private RecyclerView mRecyclerView;
    private SlidingTagView mSlidingTagView;
    private TableLoader.Listener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeinfo);

        mRecyclerView = (RecyclerView) findViewById(R.id.listview);
        assert mRecyclerView!=null;
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(2));

        mTableView = (TableView) findViewById(R.id.tableview);

//        final TableLoader tableLoader = new TableLoader(this, new TableLoader.Listener() {
//            @Override
//            public void onComplete(ArrayList<HashMap<String, Object>> list1, ArrayList<Integer> list2) {
//                mRecyclerView.setAdapter(new TimeInfoAdapter(TimeInfoActivity.this, list1));
////                Log.d("DEBUG", "mRecyclerView.setAdapter list size ="+list1.size() );
//                mTableView.setTimeDate(list2);
//            }
//        });

        mSlidingTagView = (SlidingTagView) findViewById(R.id.slidingview);
        mSlidingTagView.setListener(new SlidingTagView.TagChangeListener() {
            @Override
            public void changeTag(String string) {
                if (null == string || string.equals("")) return;
                Day day = new Day("2016", Integer.valueOf(string.split("\\.")[0])-1+"", string.split("\\.")[1]);
                new TableLoader(TimeInfoActivity.this, day, new TableLoader.Listener() {
                    @Override
                    public void onComplete(ArrayList<HashMap<String, Object>> list1, ArrayList<Integer> list2) {

                        mRecyclerView.setAdapter(new TimeInfoAdapter(TimeInfoActivity.this, list1));
//                       Log.d("DEBUG", "mRecyclerView.setAdapter list size ="+list1.size() );
                        mTableView.setTimeDate(list2);
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        new DateLoader(this, new DateLoader.Listener() {
            @Override
            public void onComplete(ArrayList<Day> days, int index) {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0 ; i<days.size(); i++) {
                    String string = days.get(i).getMonth()+"."+days.get(i).getDay();
                    list.add(string);
                }
                mSlidingTagView.addTag(list,index);
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            //设置左右的间隔如果想设置的话自行设置，我这用不到就注释掉了
/*          outRect.left = space;
            outRect.right = space;*/

            //       System.out.println("position"+parent.getChildPosition(view));
            //       System.out.println("count"+parent.getChildCount());

            //         if(parent.getChildPosition(view) != parent.getChildCount() - 1)
            outRect.bottom = space;
            outRect.top = space;
            //改成使用上面的间隔来设置
//            if(parent.getChildPosition(view) != 0)
//                outRect.top = space;
        }
    }

}
