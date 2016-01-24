package com.rsen.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.Button;

import java.util.ArrayList;

/**
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
     * path 的颜色
     */
    @ColorInt
    protected int mPathColor = Color.RED;

    /**
     * path 的宽度
     */
    protected float mPathWidth = 2;//dp
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

    /**
     * 是否到了一个周期
     */
    protected boolean isOneCircle = false;

    /**
     * 保存path中,所有用到的点坐标
     */
    protected ArrayList<PointF> mPathList;//

    public BasePathButton(Context context) {
        super(context);
    }

    public BasePathButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePathButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
