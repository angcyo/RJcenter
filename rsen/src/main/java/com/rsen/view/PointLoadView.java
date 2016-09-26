package com.rsen.view;

import android.animation.Animator;
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
    float mCurrentDegrees;
    boolean needStop = false;//循环一周后, 需要让动画停下来
    /**
     * 点的数量
     */
    int mPointCount = 5;
    /**
     * 点的半径
     */
    float mPointRadius = 10;

    /**
     * 点之间的距离
     */
    float mPointSpace = 10;

    /**
     * 点之间, 动画差值角度
     */
    int mPointOffsetDegrees = 45;

    /**
     * 动画结束的点的索引,必须小于等于点的数量
     */
    int mPointEndIndex = 0;

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
        boolean set = true;
        for (int i = 0; i < mPointCount; i++) {
            final float cy = getPointYWidthDegres(mCurrentDegrees - i * mPointOffsetDegrees);

            if (i < mPointEndIndex) {
                //点的动画已经结束了
                drawCircle(canvas, mPointRadius + i * (2 * mPointRadius + mPointSpace), getMeasuredHeight() / 2, Color.RED);
            } else {
                if (needStop && set && i == (mPointEndIndex + 1) && cy >= getMeasuredHeight() / 2) {
                    drawCircle(canvas, mPointRadius + i * (2 * mPointRadius + mPointSpace), getMeasuredHeight() / 2, Color.RED);
                    mPointEndIndex++;
                    set = false;
                } else {
                    drawCircle(canvas, mPointRadius + i * (2 * mPointRadius + mPointSpace), cy, Color.GREEN);
                }
            }

            if (mPointEndIndex == mPointCount) {
                //所有动画结束
                Log.i(TAG, "onDraw: 结束一周.");
                stop();
            }
        }
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            width = 600;
            Log.d(TAG, "onMeasure: width");
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            height = 100;
            Log.d(TAG, "onMeasure: height");
        }
//        height += mPointRadius * 2;
//        width += mPointRadius * 2;
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

    private void drawCircle(Canvas canvas, float cx, float cy, int color) {
        mPaint.setColor(color);
        canvas.drawCircle(cx, cy, mPointRadius, mPaint);
    }

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
        mValueAnimator.setDuration(2000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentDegrees = (Integer) animation.getAnimatedValue();
                mCurrentY = getPointYWidthDegres(mCurrentDegrees);
//                Log.d(TAG, "onAnimationUpdate: " + mCurrentY + " -- " + mCurrentDegrees);
                postInvalidate();
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                needStop = true;
            }
        });
    }

    //获取圆圈的cy值
    private float getPointY(float sin) {
        float cy;
        float rawHeight = getMeasuredHeight();
        float height = rawHeight - 2 * mPointRadius;
//        if (sin > 0) {
//            cy = sin * (height / 2);
//        } else {
//            cy = height / 2 + Math.abs(sin) * (height / 2);
//        }
        cy = rawHeight / 2 - sin * (height / 2);
        return cy;
    }

    private float getPointYWidthDegres(float degrees) {
        float sin = (float) Math.sin(Math.toRadians(degrees));
        return getPointY(sin);
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
