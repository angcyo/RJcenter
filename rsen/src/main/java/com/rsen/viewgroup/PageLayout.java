package com.rsen.viewgroup;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

/**
 * Created by angcyo on 2016-04-17 13:21.
 */
public class PageLayout extends FrameLayout {

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

    }

    public int getCurPageIndex() {
        return curPageIndex;
    }

    public void showNextView() {
        if (getChildCount() > 1) {
            int index = curPageIndex + 1;
            if (index >= getChildCount()) {
                showIndex(0);
            } else {
                showIndex(index);
            }
        }
    }

    public void showPrevView() {
        if (getChildCount() > 1) {
            int index = curPageIndex - 1;
            if (index < 0) {
                showIndex(getChildCount() - 1);
            } else {
                showIndex(index);
            }
        }
    }

    /**
     * 显示相对应位置的View
     *
     * @param index 位置
     */
    public void showIndex(int index) {
        if (index > getChildCount()) {
            return;
        }
        showView(getChildAt(index), index);
    }

    public void showId(@IdRes int viewId) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getId() == viewId) {
                showView(childAt, i);
            }
        }
    }

    private void showView(View view, int index) {
        getChildAt(curPageIndex).setVisibility(View.GONE);
//        hideAllView();
        view.setVisibility(View.VISIBLE);
        animShowView(curPageIndex, index);
        curPageIndex = index;
    }

    private void animShowView(int from, int to) {
        if (from == to) {
            return;
        }
        toRightAnim.cancel();
        toLeftAnim.cancel();
        toTopAnim.cancel();
        if (from > to) {
            //倒退
            getChildAt(from).startAnimation(toRightAnim);
            getChildAt(to).startAnimation(toTopAnim);
        } else {
            //前进
            getChildAt(from).startAnimation(toLeftAnim);
            getChildAt(to).startAnimation(toTopAnim);
        }
    }

    private void hideAllView() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        hideAllView();
        showIndex(curPageIndex);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
