package com.angcyo.sample.animation;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.WallpaperManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.util.Rx;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ColorAnimationActivity extends AppCompatActivity {

    static String TAG = "angcyo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_animation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        findViewById(R.id.content_color_animation).setBackground(WallpaperManager.getInstance(this).getDrawable());

        Rx.base(new Func1<String, Object>() {
            @Override
            public Object call(String s) {
                Log.i(TAG, "call 1: " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
                return null;
            }
        }, new Action1<Object>() {
            @Override
            public void call(Object o) {
                Log.i(TAG, "call 2: " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
            }
        });

        Observable.just(1).observeOn(Schedulers.computation())/*.subscribeOn(Schedulers.newThread())*/.map(new Func1<Integer, Object>() {
            @Override
            public Object call(Integer integer) {
                Log.i(TAG, "call 3: " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
                return null;
            }
        }).subscribeOn(Schedulers.newThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                Log.i(TAG, "call 4: " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
            }
        });
    }

    public void startAnimation(View view) {
        View animationView = findViewById(R.id.view);
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.RED, Color.BLUE);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();
                animationView.setBackgroundColor(color);
            }
        });
        colorAnimator.setDuration(700);
        colorAnimator.start();
    }

}
