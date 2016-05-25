package com.angcyo.sample.viewdemo;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.angcyo.sample.R;
import com.rsen.animation.RotateAnimation;
import com.rsen.base.RBaseActivity;
import com.rsen.view.DuduProgressBar;

public class DuduProgressBarActivity extends RBaseActivity {

    DuduProgressBar duduProgressBar, progressBar2, progressBar3;

    public static void e(String log) {
        Log.e("angcyo " + Thread.currentThread().getId(), "" + log);
    }

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

    class THandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            e("Handler handleMessage" + msg.what);
        }
    }

    @Override
    protected void initViewData() {
//        Handler mainHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                e("Activity handleMessage" + msg.what);
//                return false;
//            }
//        });
        Handler mainHandler = new THandler();

        new Thread() {
            @Override
            public void run() {
                Handler handler = new Handler(getMainLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        e("Thread handleMessage" + msg.what);
                        return false;
                    }
                });

                handler.sendEmptyMessage(100);
                mainHandler.sendEmptyMessage(200);
            }
        }.start();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        animProgress();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        View imageView = mViewHolder.v("imageView");
        RotateAnimation rotateAnimation = new RotateAnimation(imageView.getMeasuredWidth() / 2, imageView.getMeasuredHeight() / 2, RotateAnimation.ROTATE_INCREASE);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(RotateAnimation.INFINITE);
        imageView.startAnimation(rotateAnimation);
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
