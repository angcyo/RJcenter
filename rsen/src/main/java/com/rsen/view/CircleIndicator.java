package com.rsen.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * ViewPager 圆圈指示器
 * <p/>
 * Created by angcyo on 2016-02-17.
 */
public class CircleIndicator extends View implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private float mCircleRadius = 10f;//圆圈默认的大小, 像素
    private float mCircleMaxRadius;//放大后的半径
    private float mCircleFactor = 0.5f;//圆圈放大的倍数
    private float mCircleSpace = 4f;//两个圆圈之间的间隙, 像素

    @ColorInt
    private int mCircleColor = Color.WHITE;//圆圈的颜色

    private int count = 0;//圆圈的数量
    private float positionOffset = 0;
    private int position = 0;

    private Paint mPaint;
    private int viewHieht, viewWidth;

    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(mCircleColor);
    }

    public void setViewPager(ViewPager viewPager) {
        if (viewPager != null) {
            mViewPager = viewPager;
            mViewPager.addOnPageChangeListener(this);
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mViewPager == null || mViewPager.getAdapter() == null || mViewPager.getAdapter().getCount() == 0) {
            viewHieht = 0;
            viewWidth = 0;
        } else {
            count = mViewPager.getAdapter().getCount();
            mCircleMaxRadius = mCircleRadius * (1 + mCircleFactor);
            viewHieht = (int) (mCircleMaxRadius * 2);
            viewWidth = (int) (viewHieht * count + mCircleSpace * (count - 1));
        }
        setMeasuredDimension(viewWidth, viewHieht);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawColor(Color.RED);
        float cx = viewHieht / 2;
        float cy = viewHieht / 2;
        float radius;
        for (int i = 0; i < count; i++) {
            if (position == i) {
                radius = mCircleRadius + (1 - positionOffset) * (mCircleMaxRadius - mCircleRadius);
            } else if (i == position + 1) {
                radius = mCircleRadius + positionOffset * (mCircleMaxRadius - mCircleRadius);
            } else {
                radius = mCircleRadius;
            }
            canvas.drawCircle(cx, cy, radius, mPaint);
            cx += mCircleSpace + viewHieht;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            this.position = position;
            this.positionOffset = positionOffset;
            invalidate();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
