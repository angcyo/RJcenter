package com.angcyo.sample.rx;

import android.graphics.Color;
import android.os.Bundle;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class RxActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_rx;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean enableSlidr() {
        return true;
    }

    @Override
    protected int getStateBarColor() {
        return Color.MAGENTA;
    }
}
