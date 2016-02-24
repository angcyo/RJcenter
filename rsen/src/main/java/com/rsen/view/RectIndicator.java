package com.rsen.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.rsen.util.ResUtil;

/**
 * ViewPager 圆圈指示器
 * <p/>
 * Created by angcyo on 2016-02-17.
 */
public class RectIndicator extends View implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private float mRectWidth = 16f;//矩形的宽度, dp
    private float mRectHeight = 4f;//矩形的高度, dp
    //    private float mMaxRectWidth;//放大后的宽度
//    private float mMaxRectHeight;//放大后的高度
//    private float mScaleFactor = 0.5f;//圆圈放大的倍数
    private float mRectSpace = 4f;//矩形之间的间隙, dp

    @ColorInt
    private int mDefualtColor = Color.BLUE;//缺省的颜色
    private int mCurrentColor = Color.RED;//当前的颜色

    private int count = 0;//圆圈的数量
    private float positionOffset = 0;
    private int position = 0;

    private Paint mPaint;
    private int viewHeight, viewWidth;

    public RectIndicator(Context context) {
        this(context, null);
    }

    public RectIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mRectWidth = ResUtil.dpToPx(getResources(), mRectWidth);
        mRectHeight = ResUtil.dpToPx(getResources(), mRectHeight);
        mRectSpace = ResUtil.dpToPx(getResources(), mRectSpace);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(mDefualtColor);
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
            viewHeight = 0;
            viewWidth = 0;
        } else {
            count = mViewPager.getAdapter().getCount();
            viewHeight = (int) mRectHeight;
            viewWidth = (int) (mRectWidth * count + mRectSpace * (count - 1));
        }
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < count; i++) {
            if (position == i) {
                mPaint.setColor(mCurrentColor);
            } else {
                mPaint.setColor(mDefualtColor);
            }
            float l = 0f, t = 0f, r, b = mRectHeight;
            if (i > 0) {
                l = mRectWidth * i + mRectSpace * i;
            }
            r = l + mRectWidth;

            canvas.drawRect(l, t, r, b, mPaint);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            this.position = mViewPager.getCurrentItem();
            invalidate();
        }
    }
}
