package com.angcyo.sample.RecyclerViewDemo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class RecyclerViewActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_recycler_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        getTheme().applyStyle(R.style.Test1, true);
        setTheme(com.angcyo.rsen.R.style.Test1);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20ffff00")));
    }

    @Override
    protected void initAfter() {
//        Slidr.attach(this);
        Intent intent = new Intent(this, RecyclerViewActivity2.class);
        startActivity(intent);
    }

    @Override
    protected boolean enableSlidr() {
        return true;
    }
}
