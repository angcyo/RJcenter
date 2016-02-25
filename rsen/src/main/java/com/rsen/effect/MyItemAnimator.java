package com.rsen.effect;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angcyo on 16-02-25-025.
 */
public class MyItemAnimator extends RecyclerView.ItemAnimator {

    List<RecyclerView.ViewHolder> mAnimationAddViewHolders = new ArrayList<RecyclerView.ViewHolder>();
    List<RecyclerView.ViewHolder> mAnimationRemoveViewHolders = new ArrayList<RecyclerView.ViewHolder>();

    @Override
    public boolean animateDisappearance(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @Nullable ItemHolderInfo postLayoutInfo) {
        return false;
    }

    @Override
    public boolean animateAppearance(@NonNull RecyclerView.ViewHolder viewHolder, @Nullable ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        return false;
    }

    @Override
    public boolean animatePersistence(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        return true;
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        return false;
    }

    //需要执行动画时会系统会调用，用户无需手动调用
    @Override
    public void runPendingAnimations() {
        if (!mAnimationAddViewHolders.isEmpty()) {

            AnimatorSet animator;
            View target;
            for (final RecyclerView.ViewHolder viewHolder : mAnimationAddViewHolders) {
                target = viewHolder.itemView;
                animator = new AnimatorSet();

                animator.playTogether(
                        ObjectAnimator.ofFloat(target, "translationX", -target.getMeasuredWidth(), 0.0f),
                        ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 1.0f)
                );

                animator.setTarget(target);
                animator.setDuration(100);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mAnimationAddViewHolders.remove(viewHolder);
                        if (!isRunning()) {
                            dispatchAnimationsFinished();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
            }
        }
        else if(!mAnimationRemoveViewHolders.isEmpty()){
        }
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder viewHolder) {
        return;
    }

    @Override
    public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
        super.onAnimationFinished(viewHolder);
    }

    @Override
    public void onAnimationStarted(RecyclerView.ViewHolder viewHolder) {
        super.onAnimationStarted(viewHolder);
    }

    @Override
    public void endAnimations() {
        return;
    }

    @Override
    public boolean isRunning() {
        return !(mAnimationAddViewHolders.isEmpty()&&mAnimationRemoveViewHolders.isEmpty());
    }

}
