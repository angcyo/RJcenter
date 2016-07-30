package com.angcyo.sample.viewdrag;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by robi on 2016-07-30 16:32.
 */
public class ViewDragLayout extends RelativeLayout {

    public static Logger log = LoggerFactory.getLogger(ViewDragLayout.class);
    private ViewDragHelper mViewDragHelper;

    public ViewDragLayout(Context context) {
        super(context);
        log.info(null);
    }

    public ViewDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        log.info(null);
    }

    public ViewDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        log.info(null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        log.info(String.valueOf(getChildCount()));

        mViewDragHelper = ViewDragHelper.create(this, 0.2f, new DragCallback());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        log.info(String.valueOf(getChildCount()));
    }

    @Override
    public void computeScroll() {
        //固定写法
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //固定写法
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //固定写法
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    /**
     * 最主要的滑动处理
     */
    private class DragCallback extends ViewDragHelper.Callback {

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            log.info(null);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            log.info(null);
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            log.info(null);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            log.info(null);
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            log.info(null);
        }

        @Override
        public boolean onEdgeLock(int edgeFlags) {
            log.info(null);
            return super.onEdgeLock(edgeFlags);
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
            log.info(null);
        }

        @Override
        public int getOrderedChildIndex(int index) {
            log.info(null);
            return super.getOrderedChildIndex(index);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            log.info(null);
            return super.getViewHorizontalDragRange(child);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            log.info(null);
            return super.getViewVerticalDragRange(child);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            log.info(null);
            return super.clampViewPositionHorizontal(child, left, dx);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            log.info(null);
            return super.clampViewPositionVertical(child, top, dy);
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            log.info(null);
            return false;
        }
    }

}
