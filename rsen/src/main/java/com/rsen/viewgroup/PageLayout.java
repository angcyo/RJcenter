package com.rsen.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ViewAnimator;

/**
 * Created by angcyo on 2016-04-17 13:21.
 */
public class PageLayout extends ViewAnimator {

    private SparseArray<View> viewSparseArray;
    private int curPageIndex = 0;//当前显示页面的索引
    public static final long ANIM_TIME = 300;
    private Animation toRightAnim;
    private Animation toLeftAnim;
    private Animation toTopAnim;

    public PageLayout(Context context) {
        this(context, null);
    }

    public PageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        viewSparseArray = new SparseArray<>();

        AnimationSet toRightAnim = new AnimationSet(true);
        toRightAnim.setDuration(ANIM_TIME);
        toRightAnim.setInterpolator(new AccelerateInterpolator());
        toRightAnim.setFillAfter(true);

        AnimationSet toLeftAnim = new AnimationSet(true);
        toLeftAnim.setDuration(ANIM_TIME);
        toLeftAnim.setInterpolator(new AccelerateInterpolator());
        toLeftAnim.setFillAfter(true);

        AnimationSet toTopAnim = new AnimationSet(true);
        toTopAnim.setDuration(ANIM_TIME);
        toTopAnim.setInterpolator(new AccelerateInterpolator());
        toTopAnim.setFillAfter(true);


        TranslateAnimation rightAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 1f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f);
        TranslateAnimation leftAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, -1f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f);
        TranslateAnimation topAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 1f, Animation.RELATIVE_TO_PARENT, 0f);
        AlphaAnimation exitAlpha = new AlphaAnimation(1f, 0.2f);
        AlphaAnimation enterAlpha = new AlphaAnimation(0.2f, 1f);

        toRightAnim.addAnimation(rightAnimation);
        toRightAnim.addAnimation(exitAlpha);

        toLeftAnim.addAnimation(leftAnimation);
        toLeftAnim.addAnimation(exitAlpha);

        toTopAnim.addAnimation(topAnimation);
        toTopAnim.addAnimation(enterAlpha);

        this.toRightAnim = toRightAnim;
        this.toLeftAnim = toLeftAnim;
        this.toTopAnim = toTopAnim;


        setInAnimation(toTopAnim);
        setOutAnimation(toLeftAnim);
    }

    public int getCurPageIndex() {
        return curPageIndex;
    }
}
