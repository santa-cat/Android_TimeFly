package com.example.santa.lookupapp.mainactivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by santa on 16/7/14.
 */
public class TimeLineIdxView extends View {
    private Paint mPaint = new Paint();
    private int mColorLine = Color.DKGRAY;
    private int mColorText = 0xff666666;
    private int mTextSize = 14;
    private String text="";
    private int radius;
    public static enum STATE{TOP, COMM, BOTTOM};
    private STATE mState = STATE.COMM;

    public TimeLineIdxView(Context context) {
        this(context, null);
    }

    public TimeLineIdxView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLineIdxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final float density = context.getResources().getDisplayMetrics().density;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, dm);

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2*density);
        mPaint.setColor(mColorLine);
        mPaint.setTextSize(mTextSize);

        radius = (int)(density*5);
    }

    public void setText(String string) {
        text = string;
        invalidate();
    }

    public void setState(STATE state) {
        mState = state;
        invalidate();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        if (mState == STATE.TOP) {
            canvas.drawLine(width / 6, height/2, width / 6, height, mPaint);
        } else if (mState == STATE.BOTTOM){
            canvas.drawLine(width / 6, 0, width / 6, height/2, mPaint);
        } else {
            canvas.drawLine(width / 6, 0, width / 6, height, mPaint);
        }
        canvas.drawLine(width/6, height/2, width*3/4, height/2, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width/6, height/2, radius*3/2, mPaint);
        canvas.drawCircle(width*3/4, height/2, radius*2/3, mPaint);
        mPaint.setColor(mColorText);
        canvas.drawText(text, 0, height/2, mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(width/6, height/2, radius, mPaint);
        mPaint.setColor(mColorLine);

    }


}
