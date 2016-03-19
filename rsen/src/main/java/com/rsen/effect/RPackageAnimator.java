package com.rsen.effect;

import android.support.v7.widget.RecyclerView;

public class RPackageAnimator extends RItemAnimator {
    private SegmentAnimator mInAnimator;
    private SegmentAnimator mOutAnimator;

    public RPackageAnimator(SegmentAnimator in, SegmentAnimator out) {
        super();
        mInAnimator = in;
        mOutAnimator = out;
        setAddDuration(800);
        setMoveDuration(800);
        setRemoveDuration(800);
    }

    @Override
    protected void prepareAnimateAdd(RecyclerView.ViewHolder holder) {
        if (mInAnimator != null) {
            mInAnimator.resetDelayCount();
            mInAnimator.animationPrepare(holder);
        }
    }

    @Override
    protected void animateAddImpl(RecyclerView.ViewHolder holder) {
        if (mInAnimator != null) {
            mInAnimator.nextDelay();
            mInAnimator.startAnimation(holder, getAddDuration(), this);
        }
    }

    @Override
    protected void animateRemoveImpl(RecyclerView.ViewHolder holder) {
        if (mOutAnimator != null) {
            mOutAnimator.nextDelay();
            mOutAnimator.startAnimation(holder, getRemoveDuration(), this);
        }
    }

    @Override
    protected void prepareAnimateRemove(RecyclerView.ViewHolder holder) {
        if (mOutAnimator != null) {
            mOutAnimator.resetDelayCount();
            mOutAnimator.animationPrepare(holder);
        }
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        return false;
    }
}
