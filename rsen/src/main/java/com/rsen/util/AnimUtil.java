package com.rsen.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by angcyo on 2016-10-02 20:54.
 */
public class AnimUtil {
    public static void startArgb(final View targetView, int startColor, int endColor) {
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();//之后就可以得到动画的颜色了
                targetView.setBackgroundColor(color);//设置一下, 就可以看到效果.
            }
        });
        colorAnimator.setDuration(700);
        colorAnimator.start();
    }
}
