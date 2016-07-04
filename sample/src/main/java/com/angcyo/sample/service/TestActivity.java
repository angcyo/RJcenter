package com.angcyo.sample.service;

import android.graphics.Color;
import android.os.Bundle;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class TestActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected int getStateBarColor() {
        return Color.BLUE;
    }

    @Override
    protected boolean enableWindowAnim() {
        return false;
    }
}
