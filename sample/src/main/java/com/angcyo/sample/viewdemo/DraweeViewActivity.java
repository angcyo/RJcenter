package com.angcyo.sample.viewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class DraweeViewActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_drawee_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
