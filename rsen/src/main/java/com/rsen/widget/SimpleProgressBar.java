package com.rsen.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.angcyo.rsen.R;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/11/28 17:57
 * 修改人员：Robi
 * 修改时间：2016/11/28 17:57
 * 修改备注：
 * Version: 1.0.0
 */
public class SimpleProgressBar extends View {

    int mProgress = 0;//当前的进度

    int mProgressColor;

    Paint mPaint;
    Rect mRect;

    public SimpleProgressBar(Context context) {
        super(context);
        init();
    }

    public SimpleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mProgressColor = getResources().getColor(R.color.main_color);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mProgressColor);
        mPaint.setStyle(Paint.Style.FILL);
        mRect = new Rect();
    }

    public void setProgress(int progress) {
        if (progress >= 100 || progress <= 0) {
            setVisibility(GONE);
        } else {
            mProgress = progress;
            postInvalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect.set(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, mRect.width() * (mProgress / 100f), mRect.height(), mPaint);
    }
}
