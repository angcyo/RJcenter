package com.rsen.effect;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

/**
 * Created by angcyo on 16-02-25-025.
 */
public class MyItemAnimator2 extends SimpleItemAnimator {
    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        return false;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        return false;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        return false;
    }

    @Override
    public void runPendingAnimations() {
        return;
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        return;
    }

    @Override
    public void endAnimations() {
        return;
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
