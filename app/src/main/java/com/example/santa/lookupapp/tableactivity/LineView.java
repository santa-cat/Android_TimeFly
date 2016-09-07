package com.example.santa.lookupapp.tableactivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.santa.lookupapp.R;

/**
 * Created by santa on 16/7/6.
 */
public class LineView extends View {
    private Paint mPaint = new Paint();
    private float mPercent = 1f;
    private Path mPath = new Path();

    public LineView(Context context) {
        this(context, null);
    }

    public LineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(context, R.color.green));
        mPaint.setStrokeWidth(20);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setPercent(float p) {
        mPercent = p;
    }

    public float getPercent() {
        return mPercent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(0 , getHeight()/2);
        mPath.lineTo(getWidth()*getPercent(), getHeight()/2);
        canvas.drawPath(mPath, mPaint);
    }
}
