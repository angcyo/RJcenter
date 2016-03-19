package com.rsen.viewgroup;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Created by angcyo on 16-01-01-001.
 */
public class NextScrollView extends RelativeLayout {
    View firstView, secondView;
    int firstViewHeight, firstViewMaxHeight, firstViewTop;
    int viewWidth, viewHeight;
    int offsetTop = 0;
    boolean interceptEvent = false, handleEvent = false;
    float eventDownY = 0f, eventMoveY = 0f;
    int moveLength = 0;//滑动开始,共滑动的距离
    int curItem = 1;//当前显示,first :1 ,second:2;

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

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();

        int count = getChildCount();
        if (count > 0) {
            firstView = getChildAt(0);
            firstViewTop = firstView.getTop();
            firstViewHeight = firstView.getMeasuredHeight();
            if (firstView instanceof ScrollView) {
                firstViewMaxHeight = ((ScrollView) firstView).getChildAt(0).getMeasuredHeight();
            } else {
                firstViewMaxHeight = firstViewHeight;
            }
        }
        if (count > 1) {
            secondView = getChildAt(1);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (firstView != null) {
            firstView.layout(0, offsetTop, firstView.getMeasuredWidth(), offsetTop + firstViewHeight);
        }
        if (secondView != null) {
            secondView.layout(0, offsetTop + firstViewHeight, secondView.getMeasuredWidth(), offsetTop + firstViewHeight + secondView.getMeasuredHeight());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            eventDownY = ev.getY();
            moveLength = curItem == 1 ? 0 : -firstViewHeight;
            handleEvent = checkHandleEvent();//按下之后,是否要处理move 和 up 事件
        }

        //处理move 和 up 事件
        if (handleEvent) {
            if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
                eventMoveY = ev.getY();
                interceptEvent = checkInterceptEvent(eventDownY, eventMoveY);

                //拦截之后的move 和 up 事件
                if (interceptEvent) {
                    moveLength += eventMoveY - eventDownY;
                    eventDownY = eventMoveY;
                    offsetTop = moveLength;

                    checkBorder();
                    requestLayout();

                    return true;
                }
            }

            //处理up事件,自动滑动
            if (interceptEvent) {
                if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
                    if (curItem == 1) {
                        if (Math.abs(moveLength) > viewHeight / 3) {//移动超过了一半
                            smoothTo(2);
                        } else {
                            smoothTo(1);
                        }
                    } else {
                        if (Math.abs(moveLength + firstViewHeight) > viewHeight / 3) {//移动超过了一半
                            smoothTo(1);
                        } else {
                            smoothTo(2);
                        }

                    }
                    interceptEvent = false;
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 边界检查
     */
    private void checkBorder() {
        if (offsetTop > 0) {
            offsetTop = 0;
        } else if (offsetTop < -firstViewHeight) {
            offsetTop = -firstViewHeight;
        }
    }

    private void smoothTo(int item) {//1 或者 2
        int fromValue = offsetTop;
        int toValue;

        if (item == 1) {
            toValue = 0;
        } else {
            toValue = -firstViewHeight;
        }

        smoothTo(fromValue, toValue);
        curItem = item;
    }

    /**
     * 检查是否需要处理事件
     */
    private boolean checkHandleEvent() {
        if (firstView == null || secondView == null) {
            return false;
        }

        View firstView = this.firstView;
        View secondView = this.secondView;

        float firstScrollY = firstView.getScrollY();//已经滚动的距离
        float secondScrollY = secondView.getScrollY();//滚动的距离

        if ((firstScrollY + firstViewHeight) == firstViewMaxHeight && offsetTop == 0) {
            return true;
        }
        if (Math.abs(offsetTop) == firstViewHeight && secondScrollY == 0) {
            return true;
        }

        return false;
    }

    /**
     * 检查是否需要拦截事件
     */
    private boolean checkInterceptEvent(float downY, float moveY) {
        if (offsetTop == 0 && moveY < downY) {//first view 完全显示,并试图向上滑动
            return true;
        }

        if (Math.abs(offsetTop) == firstViewHeight && moveY > downY) {//second view 完全显示,并试图向下滑动
            return true;
        }

        if (offsetTop < 0 && Math.abs(offsetTop) < firstViewHeight) {//显示都不完全的时候
            return true;
        }

        return false;
    }

    private void smoothTo(int fromY, int toY) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new TypeEvaluator<Integer>() {
            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return startValue + (int) (fraction * (endValue - startValue));
            }
        }, fromY, toY);

        valueAnimator.setInterpolator(null);
        valueAnimator.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offsetTop = (int) animation.getAnimatedValue();
                requestLayout();
            }
        });

        valueAnimator.start();
    }

    private void log(String log) {
        Log.e(this.getClass().getSimpleName(), log + "");
    }

}
