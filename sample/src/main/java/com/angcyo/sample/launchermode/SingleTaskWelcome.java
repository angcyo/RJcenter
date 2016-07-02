package com.angcyo.sample.launchermode;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.angcyo.sample.R;

public class SingleTaskWelcome extends AppCompatActivity {
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = findViewById(android.R.id.content);

        setContentView(R.layout.activity_single_task_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.base_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        e("onCreate");

        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SingleTaskWelcome.this, SingleTaskActivity.class));
//                finish();
            }
        }, 1000);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        e("onRestart");
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

    @Override
    protected void onResume() {
        super.onResume();
        e("onResume");

//        rootView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SingleTaskWelcome.this, SingleTaskActivity.class));
//                finish();
//            }
//        }, 1000);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        e("onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        e("onRestoreInstanceState");
    }

    private void e(String log) {
        Log.e("angcyo-->wel-->" + getTaskId(), log);
    }
}
