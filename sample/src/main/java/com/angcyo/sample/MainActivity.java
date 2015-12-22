package com.angcyo.sample;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.angcyo.demo.qq.QqDemo;
import com.rsen.util.FileUtil;
import com.rsen.util.T;
import com.rsen.util.Zip;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

//                rootLayout.scrollTo(0,500);
//                testZip();

                T.show(MainActivity.this, SystemClock.elapsedRealtime() + " -- "+ SystemClock.currentThreadTimeMillis());

//                RetrofitDemo.demo();

                QqDemo.demo();

            }
        });

//        testKeyboard();
    }

    private void testZip() {
        try {
            Zip.zip(FileUtil.getSDPath() + "/test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testKeyboard() {
        rootLayout   = findViewById(R.id.root_layout);

        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
                if (height > 100) {//显示键盘了
                    Log.e("onGlobalLayout", "显示键盘");
                    rootLayout.scrollTo(0, height);
                } else {
                    Log.e("onGlobalLayout", "隐藏键盘");

                    rootLayout.scrollTo(0, 0);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
