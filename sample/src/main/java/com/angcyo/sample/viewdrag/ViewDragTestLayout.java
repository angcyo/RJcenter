package com.angcyo.sample.viewdrag;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import org.slf4j.Logger;

/**
 * Created by angcyo on 2016-07-30.
 */
public class ViewDragTestLayout extends RelativeLayout {

    Logger log = ViewDragLayout.log;

    ViewDragHelper mViewDragHelper;

    public ViewDragTestLayout(Context context) {
        super(context);
        init();
    }

    public ViewDragTestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragCallback());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mViewDragHelper.cancel();
            return false;
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        //固定写法
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            log.info("left:{} dx:{}", left, dx);
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            log.info("top:{} dy:{}", top, dy);
            return top;
        }

//        @Override
//        public void onViewReleased(View releasedChild, float xvel, float yvel) {
//            super.onViewReleased(releasedChild, xvel, yvel);
//            log.info("xvel:{} yvel:{}", xvel, yvel);
//        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return child.getMeasuredWidth();
        }
//
//        @Override
//        public int getViewVerticalDragRange(View child) {
//            return 0;
//        }
    }
}
