package com.angcyo.sample.launchmode;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.angcyo.sample.R;

public class SingleActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single2);
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

//        Slidr.attach(this);
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
        Log.e("angcyo-->2-->" + getTaskId(), log);
    }

    public void button3(View view) {
        startActivity(new Intent(this, SingleActivity3.class));
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

    @Override
    public void onAttachedToWindow() {
        e("onAttachedToWindow");


        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = 500;
        attributes.height = 500;
//        attributes.alpha = 0.5f;

        getWindow().setAttributes(attributes);

        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        ((ViewGroup) getWindow().getDecorView()).getChildAt(0).setLayoutParams(new FrameLayout.LayoutParams(360,360));
        ((ViewGroup) getWindow().getDecorView()).getChildAt(0).setX(100);
        super.onAttachedToWindow();

    }
}
