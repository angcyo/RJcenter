package com.angcyo.sample.viewdrag;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by robi on 2016-07-30 16:32.
 */
public class ViewDragLayout extends FrameLayout {

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

        mViewDragHelper = ViewDragHelper.create(this, 1f, new DragCallback());
        //激活4个方向的边缘触摸
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
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
            log.info("state:{}", state);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            log.debug("left:{} top:{} dx:{} dy:{}", left, top, dx, dy);
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            log.info(null);
            capturedChild.setScaleX(1.2f);
            capturedChild.setScaleY(1.2f);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            log.info("xvel:{} yvel:{}", xvel, yvel);
            releasedChild.setScaleX(1.f);
            releasedChild.setScaleY(1.f);

            mViewDragHelper.settleCapturedViewAt(0, 100);
            ViewCompat.postInvalidateOnAnimation(ViewDragLayout.this);

            mViewDragHelper.smoothSlideViewTo(getChildAt(1), 300, 200);
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            log.info("edgeFlags:{} pointerId:{}", edgeFlags, pointerId);
            mViewDragHelper.captureChildView(getChildAt(1), pointerId);
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
            log.debug(String.valueOf(index));
            return super.getOrderedChildIndex(index);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            int range = child.getMeasuredWidth();
            log.info("{} {}", child.getId(), range);
            return range;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            int range = child.getMeasuredHeight();
            log.info("{} {}", child.getId(), range);
            return -range;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            log.debug("left:{} dx:{}", left, dx);
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            log.debug("top:{} dy:{}", top, dy);
            return top;
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            log.info("tryCaptureView {} {}", child.getId(), pointerId);
            return false;
        }
    }

}
