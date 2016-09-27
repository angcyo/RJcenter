package com.rsen.viewgroup;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by angcyo on 2016-09-26 10:45.
 */
public class RectTransitionLayout extends RelativeLayout implements Runnable {

    static final int animTime = 500;

    /**
     * 是否激活剪切
     */
    boolean enableClip;
    /**
     * 通过改变Path,动态改变剪切形状
     */
    Path clipPath;
    /**
     * 开始剪切时的宽度
     */
    float startClipWidth;

    ValueAnimator clipAnimator;

    ValueAnimator clipExitAnimator;

    public RectTransitionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public RectTransitionLayout(Context context) {
        super(context);
        initLayout();
    }


    //-----------------------公有方法-----------------------------//

    public void startClip() {
        setStartClipWidth(0f);
    }

    public void startClip(float width) {
        setStartClipWidth(width);
    }

    //-----------------------私有方法-----------------------------//


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (clipAnimator != null) {
            clipAnimator.cancel();
            clipAnimator = null;
        }
        if (clipExitAnimator != null) {
            clipExitAnimator.cancel();
            clipExitAnimator = null;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (enableClip) {
            canvas.clipPath(clipPath);
        }
        super.draw(canvas);
    }

    private void initLayout() {
        startClipWidth = 0f;
        enableClip = false;
        clipPath = new Path();
    }

    private void setStartClipWidth(float startClipWidth) {
        this.startClipWidth = startClipWidth;
        enableClip = true;
        start();
    }

    private void start() {
        post(this);
    }

    private void calcPath(float width) {
        float w = getMeasuredWidth();
        float h = getMeasuredHeight();
        float left = (w - width) / 2;
        float top = 0;
        float right = w / 2 + width / 2;
        float bottom = h;
        clipPath.reset();
        clipPath.addRect(left, top, right, bottom, Path.Direction.CW);
    }

    private void initAnimator(float startWidth) {
        if (clipAnimator == null) {
            clipAnimator = ValueAnimator.ofFloat(startWidth, getMeasuredWidth());
            clipAnimator.setInterpolator(new AccelerateInterpolator());
            clipAnimator.setDuration(animTime);
            clipAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float width = (float) animation.getAnimatedValue();
                    calcPath(width);
                    invalidate();
                }
            });
        }
        if (clipExitAnimator == null) {
            clipExitAnimator = ValueAnimator.ofFloat(getMeasuredWidth(), startWidth);
            clipExitAnimator.setInterpolator(new AccelerateInterpolator());
            clipExitAnimator.setDuration(animTime);
            clipExitAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float width = (float) animation.getAnimatedValue();
                    calcPath(width);
                    invalidate();
                }
            });
        }
    }

    @Override
    public void run() {
        initAnimator(startClipWidth);
        clipAnimator.start();
    }
}
