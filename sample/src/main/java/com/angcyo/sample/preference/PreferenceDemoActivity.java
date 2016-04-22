package com.angcyo.sample.preference;

import android.graphics.Color;
import android.os.Bundle;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class PreferenceDemoActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_preference_demo;
    }

    @Override
    protected int getStateBarColor() {
        return Color.RED;
    }

    @Override
    protected boolean enableStatusColor() {
        return true;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        hideToolbar();
    }
}
