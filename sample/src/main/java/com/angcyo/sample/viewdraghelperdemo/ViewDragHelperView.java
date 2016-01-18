package com.angcyo.sample.viewdraghelperdemo;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by angcyo on 2016-01-18.
 */
public class ViewDragHelperView extends ViewGroup {

    private ViewDragHelper viewDragHelper;


    public ViewDragHelperView(Context context) {
        super(context);
    }

    public ViewDragHelperView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewDragHelperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void initView() {
        viewDragHelper = ViewDragHelper.create(this, new DragCallback());
    }

    @Override
    public void computeScroll() {
        //固定写法
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //固定写法
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //固定写法
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    public class DragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //是否可以 抓起 这个child
            return false;
        }
    }
}
