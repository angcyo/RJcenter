package com.rsen.util;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by angcyo on 2016-10-02 20:54.
 */
public class AnimUtil {

    /**
     * 默认颜色渐变动画
     */
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

    /**
     * 属性值动画
     */
    public static ValueAnimator startValue(int from, int to, long duration, ValueAnimator.AnimatorUpdateListener listener) {
        final ValueAnimator valueAnimator = ObjectAnimator.ofInt(from, to);
        valueAnimator.setDuration(duration).addUpdateListener(listener);
        valueAnimator.start();
        return valueAnimator;
    }

    /**
     * 应用一个布局动画
     */
    public static void applyLayoutAnimation(ViewGroup viewGroup) {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f);
        translateAnimation.setDuration(300);
        final LayoutAnimationController layoutAnimationController = new LayoutAnimationController(translateAnimation);
        viewGroup.setLayoutAnimation(layoutAnimationController);
    }
}
