package com.angcyo.rsen.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by angcyo on 15-12-14-014.
 */
public class CircleView extends View {

    private static final String TAG = CircleView.class.getSimpleName();

    private int mBigCircleRadius = 200;//大圆 半径,像素单位
    private int mLittleCircleRadius = 40;//像素单位

    @ColorInt
    private int mBigCircleColor = Color.RED;//大圆 颜色

    @ColorInt
    private int mLittleCircleColor = Color.BLUE;

    private int mBigCircleWidth = 20;// 大圆的宽度, 因为小圆是填充模式,不提供宽度参数,像素单位
    private Paint mCirclePaint;

    private double mCurrentAngle = 0;//当前的角度,决定小圆在大圆的什么位置
    private double mPauseAngle = 0;//动画暂停时的角度

    private ValueAnimator mAnimator;//动画,让小圆动起来
    private int mDuration = 1800;//动画持续时间,毫秒

    /**
     * Instantiates a new Circle view.
     *
     * @param context the context
     */
    public CircleView(Context context) {
        this(context, null);

    }

    /**
     * Instantiates a new Circle view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new Circle view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化属性参数

        init();
    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width, height;
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        int size = (mBigCircleWidth + mBigCircleRadius + mLittleCircleRadius) << 1;
        if (widthMode == MeasureSpec.AT_MOST) {//wrap_content
            width = size;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = size;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.parseColor("#20000000"));//淡淡的背景,查看区域测试使用
        int width, height;
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        //绘制大圆
        float circleX, circleY;//圆心坐标
        circleX = width / 2;
        circleY = height / 2;
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mBigCircleWidth);
        mCirclePaint.setColor(mBigCircleColor);
        canvas.drawCircle(circleX, circleY, mBigCircleRadius, mCirclePaint);

        //绘制小圆
        double circleX2, circleY2;//圆心坐标

        circleX2 = width / 2 - (float) mBigCircleRadius * Math.cos(applyAngleToRadian(mCurrentAngle));
        circleY2 = height / 2 - (float) mBigCircleRadius * Math.sin(applyAngleToRadian(mCurrentAngle));

        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeWidth(0);
        mCirclePaint.setColor(mLittleCircleColor);
        canvas.drawCircle((float) circleX2, (float) circleY2, mLittleCircleRadius, mCirclePaint);
    }

    /**
     * 角度(0-360) 转换 弧度(0-π)
     */
    private double applyAngleToRadian(double angle) {
        return (angle * Math.PI) / 180f;
    }

    /**
     * stop rotating of the ball
     */
    public void stopAnimation() {
        mPauseAngle = (float) mAnimator.getAnimatedValue();
        mAnimator.removeAllUpdateListeners();
        mAnimator.cancel();
        mAnimator.end();
        postInvalidate();
    }

    /**
     * Start animation.
     */
    public void startAnimation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            return;
        }

        mAnimator = ValueAnimator.ofObject(new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                Log.d(TAG, "1:: " + fraction + " 2:: " + startValue + " 3:: " + endValue);
                return fraction * (endValue - startValue);
            }
        }, 0f, 360f);

        mAnimator.setDuration(mDuration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentAngle = (float) animation.getAnimatedValue() + mPauseAngle;
                postInvalidate();
            }
        });

        mAnimator.setInterpolator(null);

        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
        Log.e(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
        Log.i(TAG, "onDetachedFromWindow");
    }

    @Override
    public void setVisibility(int visibility) {
        Log.d(TAG, "setVisibility  ::: " + visibility);

        if (visibility == INVISIBLE || visibility == GONE) {
            stopAnimation();
        } else {
            clearAnimation();
            startAnimation();
        }
        super.setVisibility(visibility);
    }
}
