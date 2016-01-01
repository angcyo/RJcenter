package com.angcyo.view;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
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

    public static final int NORMAL = 0x0001;
    public static final int REFRESH = NORMAL << 1;
    public static final int FULFILL = NORMAL << 2;
    public static final int FINISH = NORMAL << 3;
    View firstView, secondView;
    int offsetTop = 0;
    boolean interceptEvent = false, handleEvent = false;
    int moveLength = 0;
    float downY = 0, moveY = 0;
    int STATE = NORMAL;
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

    private void init() {
        backgroundLayout = new RelativeLayout(getContext());
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());


        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        backgroundLayout.setLayoutParams(layoutParams);

        textView1.setLayoutParams(new ViewGroup.LayoutParams(layoutParams));
        textView1.setGravity(Gravity.CENTER);
        textView1.setId(android.R.id.text1);
        textView1.setTextColor(Color.WHITE);

        RelativeLayout.LayoutParams textParams2 = new RelativeLayout.LayoutParams(layoutParams);
        textParams2.addRule(RelativeLayout.BELOW, android.R.id.text1);
        textView2.setLayoutParams(textParams2);
        textView2.setGravity(Gravity.CENTER);
        textView2.setId(android.R.id.text2);
        textView2.setTextColor(Color.WHITE);

        textView1.setText("下拉刷新");
        textView2.setText("Power By RSen");
        backgroundLayout.addView(textView1);
        backgroundLayout.addView(textView2);

        addView(backgroundLayout);
        this.setBackgroundColor(Color.parseColor("#879393"));
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
            firstView.layout(0, offsetTop - firstView.getMeasuredHeight(), firstView.getMeasuredWidth(), offsetTop + firstView.getMeasuredHeight());
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
                moveLength += (moveY - downY) * (viewHeight - offsetTop * 5) / viewHeight;
                offsetTop = moveLength;
                downY = moveY;
                updateState();//更新刷新的状态
                requestLayout();

                if (ev.getActionMasked() == MotionEvent.ACTION_UP) {

                    if (STATE == FULFILL) {
                        STATE = REFRESH;
                        updateState();

                        smoothTo(offsetTop, backgroundLayout.getMeasuredHeight());
                        startRefresh();//模拟刷新
                    } else {
                        smoothTo(offsetTop, 0);
                    }

                    moveLength = 0;
                    interceptEvent = false;
                }

                return true;
            }


        }

        return super.dispatchTouchEvent(ev);
    }

    private void startRefresh() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                STATE = FINISH;
                updateState();

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        STATE = NORMAL;
                        smoothTo(offsetTop, 0);
                    }
                }, 500);
            }
        }, 2000);
    }

    private void updateState() {
        if (STATE == REFRESH) {
            textView1.setText("刷新中...");
            return;
        }

        if (STATE == FINISH) {
            textView1.setText("刷新完成");
            return;
        }

        if (offsetTop >= backgroundLayout.getMeasuredHeight()) {
            textView1.setText("松开刷新");
            STATE = FULFILL;
        } else {
            textView1.setText("下拉刷新");
            STATE = NORMAL;
        }
    }

    private boolean checkInterceptEvent(float downY, float moveY) {
        if (secondView.getTop() != 0 || moveY > downY) {
            return true;
        }

        return false;
    }

    private boolean checkHandleEvent() {
        if (secondView.getScrollY() == 0 && secondView.getTop() == 0) {
            return true;
        }

        return false;
    }

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
