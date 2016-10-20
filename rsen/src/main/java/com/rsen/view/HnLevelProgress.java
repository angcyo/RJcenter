package com.rsen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.angcyo.rsen.R;


/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：等级进度条
 * 创建人员：Robi
 * 创建时间：2016/10/20 14:37
 * 修改人员：Robi
 * 修改时间：2016/10/20 14:37
 * 修改备注：
 * Version: 1.0.0
 */
public class HnLevelProgress extends View {

    /**
     * 进度条的颜色, 同样也是边框的颜色
     */
    @ColorInt
    int mProgressColor = Color.parseColor("#FFB000");

    /**
     * 进度条背景的颜色
     */
    @ColorInt
    int mBackgroundColor = Color.parseColor("#F0F0F0");

    /**
     * AT_MOST 时, 默认的宽高
     */
    int minHeight = 10, minWidth = 180;

    /**
     * 边框的宽度
     */
    int mBorderWidth = 1;

    /**
     * 边框空隙的宽度
     */
    int mBorderSpaceWidth = 2;

    /**
     * 圆角的大小
     */
    int mRoundSize = 6;

    /**
     * 当前的进度(0-1)
     */
    float mProgress = 0.8f;

    /**
     * 当前绘制的进度, 用来动画
     */
    float mDrawProgress = 0f;

    /**
     * 动画绘制的步长
     */
    float mDrawStep = 0.01f;

    /**
     * 绘图, 怎么可能没有画笔呢?
     **/
    Paint mPaint;

    /**
     * 框框的绘制范围
     * 进度条绘制的范围
     * 当前进度的绘制范围
     */
    RectF outRect, innerRect, drawRect;

    private boolean mAutoAnim = true;

    public HnLevelProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public HnLevelProgress(Context context) {
        this(context, null);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HnLevelProgress);
        mProgressColor = array.getColor(R.styleable.HnLevelProgress_progress_color, mProgressColor);
        mBackgroundColor = array.getColor(R.styleable.HnLevelProgress_background_color, mBackgroundColor);
        minHeight = (int) array.getDimension(R.styleable.HnLevelProgress_min_height, dp(minHeight));
        minWidth = (int) array.getDimension(R.styleable.HnLevelProgress_min_width, dp(minWidth));
        mBorderWidth = (int) array.getDimension(R.styleable.HnLevelProgress_border_width, dp(mBorderWidth));
        mBorderSpaceWidth = (int) array.getDimension(R.styleable.HnLevelProgress_border_space_width, dp(mBorderSpaceWidth));
        mRoundSize = (int) array.getDimension(R.styleable.HnLevelProgress_round_size, dp(mRoundSize));
        mProgress = (int) array.getFloat(R.styleable.HnLevelProgress_progress, 0f);
        array.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outRect = new RectF();
        innerRect = new RectF();
        drawRect = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize, heightSize;
        int widthMode, heightMode;
        widthMode = MeasureSpec.getMode(widthMeasureSpec);
        heightMode = MeasureSpec.getMode(heightMeasureSpec);

        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);


        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = dp(minWidth);
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = dp(minHeight);
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        outRect.set(mBorderWidth, mBorderWidth, w - mBorderWidth, h - mBorderWidth);
        int left = mBorderWidth + mBorderSpaceWidth;
        innerRect.set(left, left, w - left, h - left);
        drawRect.set(left, left, left + getRawWidth(w) * mProgress, h - left);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        //绘制外边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mProgressColor);
        canvas.drawRoundRect(outRect, mRoundSize, mRoundSize, mPaint);

        //绘制背景
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroundColor);
        canvas.drawRoundRect(innerRect, mRoundSize, mRoundSize, mPaint);

        //绘制进度
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mProgressColor);
        if (mAutoAnim) {
            resetDrawRect();
        }
        canvas.drawRoundRect(drawRect, mRoundSize, mRoundSize, mPaint);

    }

    private void resetDrawRect() {
        if (mDrawProgress <= mProgress) {
            mDrawProgress += mDrawStep;
            float right = getRawWidth() * Math.min(mDrawProgress, mProgress);
            drawRect.set(drawRect.left, drawRect.top, drawRect.left + right, drawRect.bottom);
            postInvalidateDelayed(16);
        }
    }


    private int dp(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    //去掉框框, 空隙, 真实的有效宽度
    private int getRawWidth(int w) {
        int left = mBorderWidth + mBorderSpaceWidth;
        return w - 2 * left;
    }

    private int getRawWidth() {
        return getRawWidth(getMeasuredWidth());
    }
}
