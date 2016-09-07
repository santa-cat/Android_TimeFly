package com.example.santa.lookupapp.tableactivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.example.santa.lookupapp.R;

import java.util.ArrayList;

/**
 * Created by santa on 16/7/10.
 */
public class SlidingTagView extends HorizontalScrollView {
    private int mScrollX = 0;
    private String DEBUG = "SlidingTagView";
    private int mTagTextSize = 16;
    private int mTagTextColor = 0xFF121212;
    private int mTagCurTextColor = 0xFFF8DB01;
    private int mDividerPadding = 50;
    private Context mContext;
    private LinearLayout mLinearLayout;
    private float mDensity = 1.0f;
    Paint paint = new Paint();
    private Scroller mScroller;
    private int mCurScrollX;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    //scroll
    private float mLastPosition = -1;
    private int mActivePointerId = -1;
    private VelocityTracker mVelocityTracker;
    private int IDEL_STATE = 0;
    private int FLING_STATE = 1;
    private int mTouchState = IDEL_STATE;
    private int mCurentIdx = Integer.MAX_VALUE;

    private TagChangeListener mListener;

    public SlidingTagView(Context context) {
        this(context, null);
    }

    public SlidingTagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SlidingTagView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
//        setSmoothScro/lingEnabled(true);

        mContext = context;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingTagView, defStyleAttr, defStyleRes);
        mDividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerPadding, dm);
        mTagTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTagTextSize, dm);

        mTagTextSize = a.getDimensionPixelSize(R.styleable.SlidingTagView_stvTextSize, mTagTextSize);
        mTagTextColor = a.getColor(R.styleable.SlidingTagView_stvTextColor, mTagTextColor);
        mDividerPadding = a.getDimensionPixelSize(R.styleable.SlidingTagView_stvDividerPadding, mDividerPadding);
        mTagCurTextColor = a.getColor(R.styleable.SlidingTagView_stvCurTextColor, mTagCurTextColor);
        a.recycle();

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mDensity = metrics.density;

        mLinearLayout = new LinearLayout(context);
        LayoutParams layoutParams= new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        mLinearLayout.setBackgroundColor(Color.RED);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setLayoutParams(layoutParams);
        addView(mLinearLayout);


        mScroller = new Scroller(context, new DecelerateInterpolator());
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mLinearLayout != null && mLinearLayout.getChildCount() > mCurentIdx) {
            scrollToChild(mLinearLayout.getChildAt(mCurentIdx));
            mCurentIdx = Integer.MAX_VALUE;
        }

    }

    public void addExpand() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();

        TextView v = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(width/2, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);
        mLinearLayout.addView(v);

    }

    public void addTag(ArrayList<String> array, int index) {
        addExpand();
        for (int i = 0 ; i<array.size(); i++) {
            addTag(array.get(i));
        }
        addExpand();
        mCurentIdx = index + 1;//因为有一个空子view

    }

    private void addTag(String text) {
        TextView tv = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        tv.setLayoutParams(layoutParams);
//        int padding = getPxFromDp(mDividerPadding);
        tv.setPadding(mDividerPadding,getPxFromDp(5),mDividerPadding,getPxFromDp(5));
        tv.setText(text);
//        tv.setBackgroundColor(Color.BLUE);
        tv.setTextColor(mTagTextColor);
        tv.setTextSize(mTagTextSize);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                scrollToChild(view);
//                Toast.makeText(mContext, ((TextView)view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mLinearLayout.addView(tv);

    }

    private int getPxFromDp(int dp) {
        return (int) (mDensity*dp);
    }

    private void obtainVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                Log.d(DEBUG ,"releaseVelocityTracker");
                break;
            case MotionEvent.ACTION_DOWN:
                mScroller.abortAnimation();
                mLastPosition = ev.getX();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);
                Log.d(DEBUG ,"initOrResetVelocityTracker");

                break;
            case MotionEvent.ACTION_MOVE:
                obtainVelocityTracker(ev);
                Log.d(DEBUG ,"obtainVelocityTracker");

                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker,
                        mActivePointerId);

                Log.d(DEBUG, "2---initialVelocity is = " + initialVelocity + "mMinimumVelocity = "+mMinimumVelocity);

                if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                    fling(-initialVelocity);
                } else {
                    release();
                }
                releaseVelocityTracker();
                mActivePointerId = -1;
//                releaseVelocityTracker();

                break;
            case MotionEvent.ACTION_DOWN:
//                initOrResetVelocityTracker();
                mScroller.abortAnimation();
                mVelocityTracker.addMovement(event);
                mLastPosition = event.getX();
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);

                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                if (activePointerIndex == -1) {
                    Log.e(DEBUG, "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
                    break;
                }
//                obtainVelocityTracker(event);

                float curPosition = event.getX();
                scrollBy((int)(mLastPosition - curPosition), 0);

                mLastPosition = curPosition;
                requestLayout();
                break;

        }
        return true;
    }
    @Override
    public void computeScroll() {
        // 先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            // 必须调用该方法，否则不一定能看到滚动效果

            int curX = mScroller.getCurrX();
            int delteX = mCurScrollX - curX;
//            Log.d(DEBUG, "mCurScrollX = " + mCurScrollX);
//            Log.d(DEBUG, "mScroller.getCurrX() = " + mScroller.getCurrX());
//            Log.d(DEBUG, "delteX = " + delteX);
            scrollBy(delteX, 0);
            mCurScrollX = curX;
            postInvalidate();
        }else {
            if (mTouchState == FLING_STATE) {
                release();
            }
            changeTagFinish();
        }

    }

    private void changeTagFinish() {
        setCurTextColor();

    }

    private void setCurTextColor() {
        View cur = null;
        int min = Integer.MAX_VALUE;
        final int midPosition = getWidth()/2 +getScrollX();
        Log.d(DEBUG, "midPosition is = " + midPosition);
        final int childCount = mLinearLayout.getChildCount();
        for (int i = 0 ; i<childCount; i++) {
            View v = mLinearLayout.getChildAt(i);
            ((TextView)v).setTextColor(mTagTextColor);
            int midchild = (v.getLeft() + v.getRight()) / 2;
//            Log.d(DEBUG, "midchild is = " + midchild);
            if(Math.abs(midPosition - midchild) < min ) {
                min = midPosition - midchild;
                cur = v;
            }
        }
        if (cur != null) {
            ((TextView) cur).setTextColor(mTagCurTextColor);
            if (mListener != null) {
                mListener.changeTag(((TextView) cur).getText().toString());
            }
        }

    }


    private void scrollToChild(View v) {
        final int midPosition = getWidth()/2 +getScrollX();
        int midchild = (v.getLeft() + v.getRight()) / 2;

        slowScrollBy(midPosition - midchild, 0);
    }

    private int[] getCurChildInfo() {
        int curIdx = 0;
        int min = Integer.MAX_VALUE;
        final int midPosition = getWidth()/2 +getScrollX();
        Log.d(DEBUG, "midPosition is = " + midPosition);
        final int childCount = mLinearLayout.getChildCount();
        for (int i = 0 ; i<childCount; i++) {
            View v = mLinearLayout.getChildAt(i);
            int midchild = (v.getLeft() + v.getRight()) / 2;
//            Log.d(DEBUG, "midchild is = " + midchild);
            if(Math.abs(midPosition - midchild) < min ) {
                min = midPosition - midchild;
                curIdx = i;
            }
        }
        return new int[]{min, curIdx};
    }

    private void release() {
        mTouchState = IDEL_STATE;
        slowScrollBy(getCurChildInfo()[0], 0);
//        Log.d(DEBUG, "min is = " + min);
    }

    public void slowScrollBy(int offsetX, int offsetY) {
        mCurScrollX = mScroller.getCurrX();
        //start 入参意思是从哪个位置开始,滚动多少偏移,然后computeScroll中每次得到getCurrY
        mScroller.startScroll(mCurScrollX, 0, offsetX, 0, 200);
        invalidate();
    }


    @Override
    public void fling(int velocityX) {
        mTouchState = FLING_STATE;
        Log.d(DEBUG, "velocityX = "+velocityX);
        mScroller.abortAnimation();
        int curScrollX = mScroller.getCurrX();
        int count = mLinearLayout.getChildCount();
        int maxScrollX = mLinearLayout.getWidth();
        int minScrollX = 0;
        mScroller.fling(curScrollX, 0, -velocityX, 0, minScrollX,  Integer.MAX_VALUE, 0, 0);
        postInvalidate();
    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        final int midPosition = getWidth()/2 +getScrollX();
//        paint.setColor(Color.BLACK);;
//        paint.setStrokeWidth(5);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeCap(Paint.Cap.ROUND);
//        paint.setStrokeJoin(Paint.Join.ROUND);
//
//        canvas.drawLine(midPosition, 0, midPosition, getPxFromDp(200), paint);
//    }

    public void setListener(TagChangeListener listener) {
        mListener = listener;
    }

    public interface TagChangeListener{
        void changeTag(String string);
    }

}
