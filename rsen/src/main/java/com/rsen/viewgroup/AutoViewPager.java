package com.rsen.viewgroup;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：自动轮播的ViewPager
 * 创建人员：Robi
 * 创建时间：2016/11/21 9:45
 * 修改人员：Robi
 * 修改时间：2016/11/21 9:45
 * 修改备注：
 * Version: 1.0.0
 */
public class AutoViewPager extends ViewPager implements Runnable {
    private static final long TIME = 3_000;
    private boolean isAutoStart = true;
    private boolean isStart = false;

    private View mRefreshLayout;

    public AutoViewPager(Context context) {
        super(context);
    }

    public AutoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            startAutoScroll();
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            endAutoScroll();
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            startAutoScroll();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isAutoStart) {
            startAutoScroll();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        endAutoScroll();
    }

    private void startAutoScroll() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnabled(true);
        }

        if (isStart) {
            return;
        }

        isStart = true;
        postDelayed(this, TIME);
    }

    private void endAutoScroll() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnabled(false);
        }

        isStart = false;
        removeCallbacks(this);
    }

    private int getNextIndex() {
        int currentItem = getCurrentItem();
        int count = getAdapter().getCount();

        int nextIndex = currentItem + 1;
        if (nextIndex >= count) {
            nextIndex = 0;
        }
        return nextIndex;
    }

    @Override
    public void run() {
        int nextIndex = getNextIndex();
        setCurrentItem(nextIndex);
        postDelayed(this, TIME);
    }

    public void setupRefreshLayout(View refreshLayout) {
        mRefreshLayout = refreshLayout;
    }
}
