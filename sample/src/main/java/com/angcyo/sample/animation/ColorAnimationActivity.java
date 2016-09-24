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
import android.view.View;

import com.angcyo.sample.R;

public class ColorAnimationActivity extends AppCompatActivity {

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
