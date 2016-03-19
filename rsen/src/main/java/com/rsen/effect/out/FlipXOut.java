package com.rsen.effect.out;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import com.rsen.effect.RItemAnimator;
import com.rsen.effect.SegmentAnimator;


public class FlipXOut extends SegmentAnimator {

    @Override
    public void animationPrepare(ViewHolder holder) {

    }

    @Override
    public void startAnimation(final ViewHolder holder, long duration, final RItemAnimator animator) {
        ViewCompat.animate(holder.itemView).cancel();
        ViewCompat.animate(holder.itemView).rotationX(90).alpha(0).setDuration(duration).
                setStartDelay(mDelayCount * mDelay).setListener(new RItemAnimator.VpaListenerAdapter() {
            @Override
            public void onAnimationCancel(View view) {
                ViewCompat.setTranslationY(view, 0);
            }

            @Override
            public void onAnimationEnd(View view) {
                animator.dispatchAnimationFinished(holder);
                animator.mAddAnimations.remove(holder);
                animator.dispatchFinishedWhenDone();
            }
        }).start();
    }
}
