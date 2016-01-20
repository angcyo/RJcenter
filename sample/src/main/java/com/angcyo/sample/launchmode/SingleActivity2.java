package com.angcyo.sample.LaunchMode;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.angcyo.sample.R;

import de.greenrobot.event.EventBus;

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
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                EventBus.getDefault().post(new SingleTaskActivity.UpdateUIEvent());
            }
        });
        e("onCreate");

//        Slidr.attach(this);
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
        Window window = getWindow();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        View decorView = window.getDecorView();
        attributes.width = 460;
        attributes.height = 700;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        attributes.alpha = 0.5f;

        attributes.y = 100;
        attributes.gravity = Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
        getWindow().setAttributes(attributes);

//        decorView.setBackgroundColor(Color.TRANSPARENT);
//        ((ViewGroup)decorView).getChildAt(0).setLayoutParams(new FrameLayout.LayoutParams(460,660));
//        ((ViewGroup)decorView).getChildAt(0).setX(100);
        super.onAttachedToWindow();

    }
}
