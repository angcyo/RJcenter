package com.angcyo.sample.viewdemo;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.view.DuduProgressBar;

public class DuduProgressBarActivity extends RBaseActivity {

    DuduProgressBar duduProgressBar, progressBar2, progressBar3;

    @Override
    protected int getContentView() {
        return R.layout.activity_dudu_progress_bar;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        duduProgressBar = (DuduProgressBar) findViewById(R.id.progressBar);
        progressBar2 = (DuduProgressBar) findViewById(R.id.progressBar2);
        progressBar3 = (DuduProgressBar) findViewById(R.id.progressBar3);

        progressBar3.setAnim(false);
        progressBar2.setAnim(true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        animProgress();
    }

    public void onButton(View view) {
        progressBar2.setProgress(0.5f);
        progressBar3.setProgress(0.5f);
    }

    private void animProgress() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(1000);
        animator.setStartDelay(500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> duduProgressBar.setProgress((Float) animation.getAnimatedValue()));
        animator.start();
    }
}
