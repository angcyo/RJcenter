package com.rsen.viewgroup;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by angcyo on 16-01-02-002.
 */
public class PullLayout extends RelativeLayout {
    View firstView, secondView;
    int offsetTop = 0;
    boolean interceptEvent = false, handleEvent = false;
    int moveLength = 0;
    float downY = 0, moveY = 0;
    RelativeLayout backgroundLayout;
    TextView textView1, textView2;
    int viewHeight;

    public PullLayout(Context context) {
        super(context);
        init();
    }

    public PullLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public static float pxToDp(Resources resources, float px) {
        float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.getDisplayMetrics());
        return dp;
    }

    private void init() {
        backgroundLayout = new RelativeLayout(getContext());
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());


        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        backgroundLayout.setLayoutParams(layoutParams);

        textView1.setLayoutParams(new ViewGroup.LayoutParams(layoutParams));
        textView1.setGravity(Gravity.CENTER);
        textView1.setId(android.R.id.text1);
        textView1.setTextColor(Color.parseColor("#6C7173"));

        LayoutParams textParams2 = new LayoutParams(layoutParams);
        textParams2.addRule(RelativeLayout.BELOW, android.R.id.text1);
        textView2.setLayoutParams(textParams2);
        textView2.setGravity(Gravity.CENTER);
        textView2.setId(android.R.id.text2);
        textView2.setTextColor(Color.parseColor("#6C7173"));

        textView1.setText("用于测试的文本");
        textView2.setText("Power By RSen");
        backgroundLayout.addView(textView1);
        backgroundLayout.addView(textView2);
        backgroundLayout.setPadding(0, (int) pxToDp(getResources(), 10), 0, (int) pxToDp(getResources(), 15));

        this.addViewInLayout(backgroundLayout, -1, layoutParams);
        this.setBackgroundColor(Color.parseColor("#2E3132"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();
        viewHeight = getMeasuredHeight();
        if (count > 0) {
            firstView = getChildAt(0);
        }
        if (count > 1) {
            secondView = getChildAt(1);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (firstView != null) {
            firstView.layout(0, 0, firstView.getMeasuredWidth(), firstView.getMeasuredHeight());
        }
        if (secondView != null) {
            secondView.layout(0, offsetTop, secondView.getMeasuredWidth(), offsetTop + secondView.getMeasuredHeight());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            downY = ev.getY();
            handleEvent = checkHandleEvent();
        }

        if (handleEvent) {
            if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
                moveY = ev.getY();
                interceptEvent = checkInterceptEvent(downY, moveY);
            }

            if (interceptEvent) {
                moveLength += (moveY - downY) / 3;
//                moveLength += (moveY - downY) * (viewHeight - offsetTop * 5) / viewHeight;
                offsetTop = moveLength;
                downY = moveY;
                checkBorder();
                requestLayout();

                if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
                    smoothTo(offsetTop, 0);
                    moveLength = 0;
                    interceptEvent = false;
                }

                return true;
            }


        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 边界检查
     */
    private void checkBorder() {
        if (offsetTop < 0) {
            offsetTop = 0;
        }
    }

    /**
     * 检查是否需要拦截事件
     */
    private boolean checkInterceptEvent(float downY, float moveY) {
        if (offsetTop == 0 && moveY > downY) {
            return true;
        }

        if (offsetTop > 0) {
            return true;
        }

        return false;
    }

    /**
     * 检查是否需要处理事件
     */
    private boolean checkHandleEvent() {
        if (secondView.getScrollY() == 0 && secondView.getTop() == 0) {
            return true;
        }

        return false;
    }

    /**
     * 平滑滚动
     */
    private void smoothTo(int fromY, int toY) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new TypeEvaluator<Integer>() {
            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return startValue - (int) (fraction * (startValue - endValue));
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
}
