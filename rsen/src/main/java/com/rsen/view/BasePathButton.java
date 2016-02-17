package com.rsen.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.rsen.util.ResUtil;

import java.util.ArrayList;

/**
 * 公共path的基类
 *
 * Created by angcyo on 16-01-24-024.
 */
public abstract class BasePathButton extends Button {
    /**
     * 用来绘制路径,最重要的成员
     */
    protected Path mPath;

    /**
     * 画笔
     */
    protected Paint mPaint;

    /**
     * path 的宽度
     */
    protected int mViewWidth, mViewHeight;

    /**
     * 多少毫秒绘制一次
     */
    protected int mDrawDelay = 40;
    /**
     * 当前..
     */
    protected int mCurDrawDelay;

    /**
     * 保存当前绘制到了那个点, =mPathList.size 表示到了一周
     */
    protected int mCurListIndex = 0;
    /**
     * 每次取多少个点
     */
    protected int mListStep = 8;
    protected int mCurListStep = mListStep;

    /**
     * 是否到了一个周期
     */
    protected boolean isOneCircle = false;
    protected ArrayList<PointF> mPathList;//
    @ColorInt
    protected int mPathColor = Color.RED;
    /**
     * path 的宽度
     */
    protected float mPathWidth = 2;//dp
    protected boolean isCancel = false;
    /**
     * 保存path中,所有用到的点坐标
     */

    float mMinSize = 40;//dp 最小的大小
    boolean isDown = false;
    boolean isUp = false;
    int mDrawPause = mDrawDelay;//绘制一次之后,暂停多少毫秒

    public BasePathButton(Context context) {
        super(context);
    }

    public BasePathButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePathButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMinSize = ResUtil.dpToPx(getResources(), mMinSize);
        mPathWidth = ResUtil.dpToPx(getResources(), mPathWidth);

        setWillNotDraw(false);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(mPathWidth);
        mPaint.setColor(mPathColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = h;
        mViewWidth = w;
        mPathList = new ArrayList<>();
        fillList(getDrawRectF(), mPathList);
    }

    protected abstract void fillList(RectF drawRect, ArrayList<PointF> mPathList);

    /**
     * 去除padding 的 可绘制区域
     */
    protected RectF getDrawRectF() {
        RectF rectF = new RectF();
        rectF.set(getPaddingLeft(), getPaddingTop(), mViewWidth - getPaddingRight(), mViewHeight - getPaddingBottom());
        return rectF;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (isCancel) {
            if (action == MotionEvent.ACTION_UP) {
                isCancel = false;
            }
            return true;
        }
        if (action == MotionEvent.ACTION_MOVE && !getDrawRectF().contains(event.getX(), event.getY())) {
            action = MotionEvent.ACTION_CANCEL;
            isCancel = true;
        }

        if (action == MotionEvent.ACTION_DOWN) {
            isCancel = false;
            onTouchDown();
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            onTouchUp();
        }
        return true;
    }

    private void onTouchDown() {
        isDown = true;
        isUp = false;
        mCurDrawDelay = mDrawDelay;
        mCurListIndex = 0;
        mCurListStep = mListStep;
        mPath.reset();
        onDrawPathStart();
    }

    /**
     * touch down 开始绘制path时调用
     */
    protected abstract void onDrawPathStart();


    private void onTouchUp() {
        isDown = false;
        isUp = true;

        mCurDrawDelay = 10;//抬手之后, 快速画完
//        mCurListStep = 2 * mListStep;
        mCurListStep = mPathList.size() * mCurDrawDelay / 100;
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawRect(getDrawRectF(), mPaint);fr

        if (isDown || isUp) {
            canvas.drawPath(mPath, mPaint);

            for (int i = 0; i < mCurListStep; i++) {
                PointF pointF = mPathList.get(mCurListIndex);

                if (mCurListIndex == 0) {
                    mPath.reset();
                    mPath.moveTo(pointF.x, pointF.y);
                    isOneCircle = false;
                }

                mCurListIndex++;
                mPath.lineTo(pointF.x, pointF.y);

                if (mCurListIndex >= mPathList.size()) {
                    mCurListIndex = 0;
                    isOneCircle = true;
                    mPath.close();
                    canvas.drawPath(mPath, mPaint);

                    if (isUp) {
                        isUp = false;
                        onDrawPathEnd(canvas);
                    } else {
                        onOneCircle();
                    }
                    return;
                }
            }
            postInvalidateDelayed(mCurDrawDelay);
        } else {
            needDraw(canvas);
        }
    }

    protected abstract void needDraw(Canvas canvas);

    /**
     * 手指抬起, 并绘制到了终点
     */
    protected abstract void onDrawPathEnd(Canvas canvas);

    /**
     * 绘制了一圈
     */
    protected void onOneCircle() {
        postInvalidateDelayed(mDrawPause);
    }
}
