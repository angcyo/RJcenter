package com.rsen.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by angcyo on 2016-09-26 16:32.
 */
public class PointLoadView extends View {
    static final String TAG = "point_load_view";
    Paint mPaint;
    ValueAnimator mValueAnimator;
    float mCurrentY;

    public PointLoadView(Context context) {
        super(context);
        initView();
    }

    public PointLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    //------------------公有的方法----------------------------//

    //------------------重载的方法---------------------------//

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(getMeasuredWidth() / 2, mCurrentY, 10, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            width = 100;
            Log.d(TAG, "onMeasure: width");
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            height = 100;
            Log.d(TAG, "onMeasure: height");
        }

        setMeasuredDimension(width, height);
        mCurrentY = height / 2;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG, "onDetachedFromWindow: ");
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout: ");
//        start();
//        start();
//        start();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG, "onFinishInflate: " + getMeasuredWidth() + getMeasuredHeight() + " - " + getWidth() + getHeight());
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        Log.i(TAG, "onWindowVisibilityChanged: " + visibility + " - " + getMeasuredWidth() + getMeasuredHeight() + " - " + getWidth() + getHeight());
        if (visibility == VISIBLE) {
            start();
        } else {
            stop();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "onAttachedToWindow: " + getMeasuredWidth() + getMeasuredHeight() + " - " + getWidth() + getHeight());
    }

    //--------------------私有的方法------------------------//

    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        createAnimator();
    }

    private void createAnimator() {
        mValueAnimator = ValueAnimator.ofInt(-180, 180);
        mValueAnimator.setInterpolator(new LinearInterpolator());
//        mValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setDuration(700);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float sin = (float) Math.sin(Math.toRadians((Integer) animation.getAnimatedValue()));
                Log.d(TAG, "onAnimationUpdate: " + sin);
                mCurrentY = getPointY(sin);
                postInvalidate();
            }
        });
    }

    //获取圆圈的cy值
    private float getPointY(float sin) {
        float cy;
        float height = getMeasuredHeight();
        if (sin > 0) {
            cy = sin * (height / 2);
        } else {
            cy = height / 2 + Math.abs(sin) * (height / 2);
        }
        return cy;
    }

    private void start() {
        if (mValueAnimator != null) {
            mValueAnimator.start();
        }
    }

    private void stop() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
    }
}
