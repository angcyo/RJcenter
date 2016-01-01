package com.angcyo.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Created by angcyo on 15-12-31 031 16:38 下午.
 */
public class NextScrollView extends RelativeLayout {

    ScrollView topView;
    ScrollView bottomView;
    float downY = 0f;
    boolean canScroll = false;
    float moveLength = 0f;
    ValueAnimator valueAnimator;
    boolean isRun = false;
    float eventDownY = 0f;
    boolean isHandled = false;//是否需要处理事件
    boolean canScrollToBottom = false;
    boolean canScrollToUp = false;

    public NextScrollView(Context context) {
        super(context);
        init();
    }

    public NextScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NextScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void log(String log) {
        Log.e(this.getClass().getSimpleName(), log + "");
    }

    private void init() {
        topView = (ScrollView) getChildAt(0);
        bottomView = (ScrollView) getChildAt(1);

        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
//        log(thisMethodStack.getMethodName());

        topView = (ScrollView) getChildAt(0);
        bottomView = (ScrollView) getChildAt(1);

//        log("topView--viewHeight:" + topView.getHeight());
//        log("bottomView--viewHeight:" + bottomView.getHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
//        log(thisMethodStack.getMethodName());

//        log("topView--top:" + topView.getTop());
//        log("bottomView--top:" + bottomView.getTop());


        topView.layout(0, (int) moveLength,
                topView.getMeasuredWidth(), (int) (topView.getMeasuredHeight() + moveLength));
        bottomView.layout(0, (int) (topView.getMeasuredHeight() + moveLength),
                bottomView.getMeasuredWidth(), (int) (topView.getMeasuredHeight() + bottomView.getMeasuredHeight() + moveLength));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        log("onInterceptTouchEvent");

        return super.onInterceptTouchEvent(ev);
//        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        log(topView.getScrollY() + "");

        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            eventDownY = ev.getY();
            moveLength = topView.getTop();

//            log("eventDownY:" + eventDownY + " getRawY:" + ev.getRawY());
            isHandled = checkEventDown();
        }


        if (isHandled) {
            if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
                float eventY = ev.getY();
                float eventMoveY = eventY - eventDownY;
                moveLength += eventMoveY;

//                log("eventDownY:" + eventDownY + " eventY:" + eventY);
//                log("eventMoveY:" + eventMoveY + " moveLength:" + moveLength);

                eventDownY = eventY;
                if (checkEventMove(eventMoveY)) {
                    requestLayout();
                    return true;
                }

//                return true;
//                if (eventMoveY > 0) {//向下滚动
//                    if (canScrollToUp) {
//                        requestLayout();
//                        return true;
//                    }
//                } else {//向上滚动
//                    if (canScrollToBottom) {
//                        requestLayout();
//                        return true;
//                    }
//                }
            }
        }


        if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
            if (isHandled) {
                if (Math.abs(moveLength) >= getMeasuredHeight() / 2) {//滑动大于一半
                    if (moveLength < 0) {//向上滑,滚动到下一个
                        smoothTo(moveLength, -topView.getMeasuredHeight());
                    } else {//滚动到上一个
                        smoothTo(moveLength, 0);
                    }

                } else {//滑动小于一半
                    if (moveLength < 0) {//向上滑,回到上一个底部
                        smoothTo(moveLength, 0);
                    } else {//回到下一个顶部
                        smoothTo(moveLength, -topView.getMeasuredHeight());
                    }
                }
            }
            isHandled = false;
        }


//        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
//            downY = ev.getY();
//            moveLength = 0;
//
//            if (((topView.getScrollY() + topView.getMeasuredHeight()) == topView.getChildAt(0).getMeasuredHeight() && topView.getTop() == 0) //已经到了 topView的底部,并且没有在偏移
//                    || (bottomView.getScrollY() == 0 && bottomView.getTop() == 0)//bottomView已经在顶部, 并且没有在偏移
//                    ) {
//                canScroll = true;
//            } else {
//                canScroll = false;
//            }
//
//        }
//
//        if (canScroll) {
//            if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
//                float moveY = ev.getY();
//                moveLength += moveY - downY;
//
////                log("moveLength:" + moveLength);
//
//                requestLayout();
//                downY = moveY;
//                return true;
//            } else if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
//                if (Math.abs(moveLength) >= getMeasuredHeight() / 2) {
////                showBottmView();
//                    smoothTo(moveLength, -topView.getMeasuredHeight());
//                } else {
////                showUpView();
//                    smoothTo(0f, moveLength);
//                }
//                canScroll = false;
//            }
//
//        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 返回是否需要处理事件
     */
    private boolean checkEventDown() {
        ViewGroup topView = this.topView;
        ViewGroup bottomView = this.bottomView;

        float topViewHeight = topView.getMeasuredHeight();//布局高度
        float topScrollY = topView.getScrollY();//已经滚动的距离
        float topViewMaxHeight = topView.getChildAt(0).getMeasuredHeight();//总共可滚动的距离

        float bottomViewTop = bottomView.getTop();//与顶部的距离
        float bottomScrollY = bottomView.getScrollY();//滚动的距离

        if ((topScrollY + topViewHeight) == topViewMaxHeight) {//说明topView已经在底部
            log("topView已经在底部--可以滚动下一个");
            canScrollToBottom = true;
            canScrollToUp = false;
        } else if (bottomViewTop == 0 && bottomScrollY == 0) {//说明bottomView已经在顶部
            log("bottomView已经在顶部--可以滚动上一个");
            canScrollToUp = true;
            canScrollToBottom = false;
        } else {
            log("无处理");
            canScrollToBottom = false;
            canScrollToUp = false;
            return false;
        }

        return true;
    }

    /**
     * 是否需要消耗事件
     */
    private boolean checkEventMove(float moveY) {
        if (moveY > 0) { //意图:向下滑
            if (topView.getTop() == -getMeasuredHeight() && bottomView.getTop() == 0) {
                return true;
            }
        }
        if (moveY < 0) {//意图: 向上滑
            if (topView.getTop() == 0 && bottomView.getTop() == getMeasuredHeight()) {
                return true;
            }
        }

        return false;
    }


    private void showUpView() {
        moveLength = 0;
        requestLayout();
    }

    private void showBottomView() {
        moveLength = -topView.getMeasuredHeight();
        requestLayout();
    }

    private void smoothTo(float fromY, float toY) {
        log("fromY:" + fromY + "  toY:" + toY);

//        fromY = 1000f;
//        toY = 500f;
//
//        if (isRun) {
//            return;
//        }


        valueAnimator = ValueAnimator.ofObject(new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                return startValue + fraction * (endValue - startValue);
            }

        }, fromY, toY);

        valueAnimator.setInterpolator(null);
        valueAnimator.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moveLength = (float) animation.getAnimatedValue();
//                log("getAnimatedValue:" + animation.getAnimatedValue());
                requestLayout();
            }
        });

        valueAnimator.start();
//        isRun = true;
    }
}
