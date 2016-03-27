package com.angcyo.sample.LaunchMode;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.angcyo.sample.R;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class SingleTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_task);
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
        e("onCreate");

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUpdateUI(UpdateUIEvent event) {
        e(" onUpdateUI ..");
        ((Button) findViewById(R.id.button2)).setText("onEvent");

//        Window window = getWindow();
//        WindowManager.LayoutParams attributes = getWindow().getAttributes();
//        View decorView = window.getDecorView();
////        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
////        attributes.height = 600;
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
////        attributes.alpha = 0.5f;
//
//        attributes.y = 300;
//        attributes.X = 300;
//        attributes.gravity = Gravity.BOTTOM;
//        getWindow().setAttributes(attributes);
//
//        decorView.setBackgroundColor(Color.TRANSPARENT);
//        ((ViewGroup) decorView).getChildAt(0).setLayoutParams(new FrameLayout.LayoutParams(660, 860));
//        ((ViewGroup) decorView).getChildAt(0).setX(100);

        startScroll();
    }

    private void startScroll() {
        Window window = getWindow();
        final View decorView = ((ViewGroup) window.getDecorView()).getChildAt(0);
        final float width = decorView.getWidth();
        decorView.setX(-width);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                e(value + "");
                decorView.setX(-width + width * value);
            }
        });

        valueAnimator.setDuration(400);
        valueAnimator.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        e("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        e("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        e("onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        e("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        e("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        e("onDestroy");
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        e("onNewIntent");

        boolean exit = intent.getBooleanExtra("exit", false);
        if (exit) {
            finish();
        }
    }

    private void e(String log) {
        Log.e("angcyo-->main-->" + getTaskId(), log);
    }

    public void button2(View view) {
        startActivity(new Intent(this, SingleActivity2.class));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        e("dispatchTouchEvent " + ev.getActionMasked());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        e("onTouchEvent " + event.getActionMasked());
        return super.onTouchEvent(event);
    }

    public static class UpdateUIEvent {

    }
}
