package com.example.santa.lookupapp.tableactivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.example.santa.lookupapp.R;

import java.util.List;

/**
 * Created by santa on 16/7/6.
 */
public class TableView extends View{
    private int mHighLightIdx = 0;
    private Paint mHighLightPaint = new Paint();
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private int mSpace = 0;
    private int mPadding = 0;
    private int mUnitHeight = 0;
    private float mDensity;
    private final int LINE_NUM = 24;
    private final int mTextSize = 15;
    private int mRadius;
    private List<Integer> mTimeList;
    private int mActivePointerId =-1;
    private int mLastPosition = -1;


    public TableView(Context context) {
        this(context, null);
    }

    public TableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void init(Context context) {
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mDensity = metrics.density;

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.lightGray));
        mPaint.setStrokeWidth(3);

        mPaint.setTextSize(calPxFromDp(mTextSize));

        mHighLightPaint.setAntiAlias(true);
        mHighLightPaint.setStyle(Paint.Style.FILL);
        mHighLightPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        mHighLightPaint.setStrokeWidth(4);

    }

    public int calPxFromDp(int px){
        return (int)(px * mDensity);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mSpace = MeasureSpec.getSize(widthMeasureSpec)/25;
        mPadding = mSpace;
        mUnitHeight= (MeasureSpec.getSize(heightMeasureSpec)- mPadding*2)/60;
        mRadius = mSpace/6;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mTimeList == null||mTimeList.size() == 0) {return;}
        drawTable(canvas);
        drawCircle(canvas);
        drawLine(canvas);
        drawText(canvas);
        drawHorizontalLine(canvas);
    }

    public void drawTable(Canvas canvas) {
        final int countRestore = canvas.save();
        for (int i = 1; i<= LINE_NUM; i++) {
            canvas.translate(mSpace, 0);
            mPaint.setColor(getResources().getColor(R.color.lightMoreGray));
            Paint p = (mHighLightIdx == i) ? mHighLightPaint : mPaint;
            canvas.drawLine(0 ,mPadding, 0, getHeight() - mPadding, p);
            if(i % 4 == 1 || i == LINE_NUM) {
                final int count = canvas.save();
                canvas.translate(-mTextSize , 0);
                mPaint.setColor(getResources().getColor(R.color.lightGray));
                canvas.drawText(String.valueOf(i), 0, getHeight(), mPaint);
                canvas.restoreToCount(count);
            }
        }
        canvas.restoreToCount(countRestore);

        mPaint.setColor(getResources().getColor(R.color.lightGray));
    }

    public void drawCircle(Canvas canvas) {

        final int countRestore = canvas.save();
        for (int i = 1 ; i<=LINE_NUM; i++){
            canvas.translate(mSpace, 0);
            Paint p = (mHighLightIdx == i) ? mHighLightPaint : mPaint;
            canvas.drawCircle(0, getHeight()-mUnitHeight*mTimeList.get(i-1) - mPadding, mRadius, p);
        }
        canvas.restoreToCount(countRestore);

    }

    public void drawLine(Canvas canvas) {
        final int countRestore = canvas.save();
        for (int i = 1 ; i<LINE_NUM; i++){
            canvas.translate(mSpace, 0);
            canvas.drawLine(0, getHeight()-mUnitHeight*mTimeList.get(i-1) - mPadding, mSpace, getHeight()-mUnitHeight*mTimeList.get(i) - mPadding, mPaint);
        }
        canvas.restoreToCount(countRestore);
    }

    public void drawText(Canvas canvas) {
        if(mHighLightIdx >=1 && mHighLightIdx <= 24) {
            int i = 2;
            i = (mHighLightIdx < 2 ) ? mHighLightIdx : i;
            RectF rectF = new RectF((mHighLightIdx-i)*mSpace, mPadding, (mHighLightIdx - i + 3)*mSpace, mPadding + mSpace*2);
            canvas.drawRoundRect(rectF, 20, 20, mHighLightPaint);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(mTimeList.get(mHighLightIdx-1)+"min", (mHighLightIdx-i)*mSpace, (int)(mSpace*2.5), mPaint);
            mPaint.setColor(getResources().getColor(R.color.lightGray));
        }
    }

    public void drawHorizontalLine(Canvas canvas) {
        canvas.drawLine(0, mPadding/10 , getWidth(), mPadding/10, mPaint);
//       canvas.drawText("时间曲线图", getWidth()/2, 0, mPaint);
    }

    public void setTimeDate(List<Integer> list) {
        mTimeList = list;
        Log.d("SANTA", "mTimeList size is ="+mTimeList.size());
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                resetHighLightIdx();
                break;
            case MotionEvent.ACTION_DOWN:

//                mLastPosition = (int) event.getX();
//                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                calcuHighLightIndex((int) event.getX());
                break;
            case MotionEvent.ACTION_MOVE:
//                final int activePointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
//                if (activePointerIndex == -1) {
//                    Log.e("refresh", "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
//                    break;
//                }
//                int curPosition = (int)(event.getX());
//                moveBy(mIndicator.getHeaderLastPosition() - curPosition);
//                mIndicator.setHeaderLastPosition(curPosition);
//                mLastPosition = (int) curPosition;
                calcuHighLightIndex((int) event.getX());

                break;
        }
        return true;
    }

    private void resetHighLightIdx() {
        mHighLightIdx = 0;
        invalidate();
    }

    private void calcuHighLightIndex(int x) {
        if (x<0 || x>getWidth()) {
            resetHighLightIdx();
            return;
        }
        int val = x/mSpace;
        int idx = Math.abs(x - val*mSpace) <= Math.abs(x - (val+1)*mSpace)? val : val+1;
        if(idx != mHighLightIdx) {
            mHighLightIdx = idx;
//            Log.d("DEBUG", "mHighLightIdx = " + mHighLightIdx);
            invalidate();
        }
    }

}
