package com.rsen.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by angcyo on 2016-10-02 20:54.
 */
public class AnimUtil {
    public static ValueAnimator startArgb(final View targetView, int startColor, int endColor) {
        return startArgb(targetView, startColor, endColor, 700);
    }

    public static ValueAnimator startArgb(final View targetView, int startColor, int endColor, long duration) {
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        colorAnimator.setInterpolator(new LinearInterpolator());
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();//之后就可以得到动画的颜色了.
                targetView.setBackgroundColor(color);//设置一下, 就可以看到效果..
            }
        });
        colorAnimator.setDuration(duration);
        colorAnimator.start();
        return colorAnimator;
    }
}
