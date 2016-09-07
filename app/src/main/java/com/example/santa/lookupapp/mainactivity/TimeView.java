package com.example.santa.lookupapp.mainactivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.santa.lookupapp.R;

import java.util.Calendar;

/**
 * Created by santa on 16/7/7.
 */
public class TimeView extends RelativeLayout {
    private Paint mPaint = new Paint();
    private Paint mPaintCircle = new Paint();
    private Path mPath = new Path();
    private TextView mTimeInfo;
    private TextView mDateInfo;
    private int mTimeTextSize;
    private int mDateTextSize;
    private float mDensity;
    private static final int DEFAULT_CIRCLE_BG_LIGHT = 0xFFFAFAFA;

    public TimeView(Context context) {
        this(context, null);
    }

    public TimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mDensity = metrics.density;

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
        mPaint.setStrokeWidth(20);

        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.FILL);
        mPaintCircle.setColor(getResources().getColor(R.color.colorPrimaryDark));


        mTimeInfo = new TextView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        mTimeInfo.setLayoutParams(layoutParams);
        addView(mTimeInfo);


        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_HORIZONTAL);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        mDateInfo = new TextView(context);
        mDateInfo.setLayoutParams(layoutParams);
        addView(mDateInfo);
    }

    public void setTimInfo(String time) {
        mTimeInfo.setText(time);
        Calendar calendar = Calendar.getInstance();
        Log.d("DEBUG time", calendar.getTimeInMillis()+"");
        String date = String.valueOf(calendar.get(Calendar.MONTH))+"-"+String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        mDateInfo.setText(date);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTimeTextSize = Math.min(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.getSize(widthMeasureSpec))/10;
        mDateTextSize = Math.min(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.getSize(widthMeasureSpec))/20;
        mTimeInfo.setTextSize(mTimeTextSize);
        mDateInfo.setTextSize(mDateTextSize);

        setPadding(0 ,0 ,0 ,mDateTextSize);
        setShadow();
    }

    private void setShadow() {

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            ShapeDrawable mBgCircle = new ShapeDrawable(new OvalShape());
            ViewCompat.setElevation(this, 20 * mDensity);
            mBgCircle.getPaint().setColor(DEFAULT_CIRCLE_BG_LIGHT);
            setBackgroundDrawable(mBgCircle);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        //drawLine(canvas);
    }

    private void drawCircle(Canvas canvas) {
        int radius = Math.min(getWidth()/2, getHeight()/2);


        Point point1 = new Point((int)((2 - Math.sqrt(3))*radius/2), (int)(3*radius/2));
        Point point2 = new Point((int)(radius*2 - (2 - Math.sqrt(3))*radius/2), (int)(3*radius/2));
        mPath.reset();
        mPath.addArc(new RectF(0 , 0, 2*radius, 2*radius), 160 , 220);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        mPath.reset();
        mPath.addArc(new RectF(0 , 0, 2*radius, 2*radius), 20 , 140);
        mPath.close();
        canvas.drawPath(mPath, mPaintCircle);

    }
}
