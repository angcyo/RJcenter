package com.angcyo.view;

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

    ScrollView upView;
    ScrollView bottomView;
    float downY = 0f;
    boolean canScroll = false;
    float moveLength = 0f;

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
        upView = (ScrollView) getChildAt(0);
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
        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());

        upView = (ScrollView) getChildAt(0);
        bottomView = (ScrollView) getChildAt(1);

        log("upView--height:" + upView.getHeight());
        log("bottomView--height:" + bottomView.getHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        StackTraceElement thisMethodStack = new Exception().getStackTrace()[0];
        log(thisMethodStack.getMethodName());

        log("upView--top:" + upView.getTop());
        log("bottomView--top:" + bottomView.getTop());


        upView.layout(0, (int) moveLength,
                upView.getMeasuredWidth(), (int) (upView.getMeasuredHeight() + moveLength));
        bottomView.layout(0, (int) (upView.getMeasuredHeight() + moveLength),
                bottomView.getMeasuredWidth(), (int) (upView.getMeasuredHeight() + bottomView.getMeasuredHeight() + moveLength));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        log("onInterceptTouchEvent");

        return super.onInterceptTouchEvent(ev);
//        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        log(upView.getScrollY() + "");

        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            downY = ev.getY();

            if ((upView.getScrollY() + upView.getMeasuredHeight()) == upView.getChildAt(0).getMeasuredHeight() &&
                    upView.getTop() == 0) {
                canScroll = true;
            } else {
                canScroll = false;
            }

        } else if (ev.getActionMasked() == MotionEvent.ACTION_MOVE && canScroll) {
            float moveY = ev.getY();
            moveLength += moveY - downY;

            log("moveLength:" + moveLength);

            requestLayout();
            downY = moveY;
            return true;
        } else if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
            canScroll = false;
        }

        return super.dispatchTouchEvent(ev);
    }
}
