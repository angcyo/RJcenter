package com.rsen.effect.in;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView.ViewHolder;

import com.rsen.effect.RItemAnimator;
import com.rsen.effect.SegmentAnimator;


public class StandUpIn extends SegmentAnimator {

    @Override
    public void animationPrepare(ViewHolder holder) {

        ViewCompat.setRotationX(holder.itemView, 90);
        System.out.println("animationPrepare");
    }

    @Override
    public void startAnimation(final ViewHolder holder, long duration, final RItemAnimator animator) {

        System.out.println("startAnimation");
        
        float x = (holder.itemView.getWidth() - holder.itemView.getPaddingLeft() - holder.itemView.getPaddingRight()) / 2 + holder.itemView.getPaddingLeft();
        float y = holder.itemView.getHeight() - holder.itemView.getPaddingBottom();

        AnimatorSet set = new AnimatorSet();

        set.playTogether(ObjectAnimator.ofFloat(holder.itemView, "pivotX", x, x, x, x, x), ObjectAnimator.ofFloat(holder.itemView, "pivotY", y, y, y, y, y),
                ObjectAnimator.ofFloat(holder.itemView, "rotationX",90, 55, -30, 15, -15, 0));
        set.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator.dispatchAnimationFinished(holder);
                animator.mAddAnimations.remove(holder);
                animator.dispatchFinishedWhenDone();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });
        set.setStartDelay(mDelay * mDelayCount);
        set.setDuration(animator.getAddDuration());
        set.start();

        animator.mAddAnimations.add(holder);

    }

}
