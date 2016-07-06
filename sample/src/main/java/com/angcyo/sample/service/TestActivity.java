package com.angcyo.sample.service;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class TestActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(android.R.id.content).setBackground(new ColorDrawable(Color.BLUE));
    }

    @Override
    protected int getStateBarColor() {
        return Color.BLUE;
    }

    @Override
    protected boolean enableWindowAnim() {
        return false;
    }


    @Override
    protected boolean enableSlidr() {
        return true;
    }

    @Override
    protected void initBefore() {
        super.initBefore();
//        setTheme(R.style.AppTheme);
//        setTheme(com.angcyo.rsen.R.style.ActivityTranslucent);
    }

    @Override
    protected void initBaseView() {
//        setTheme(R.style.AppTheme);
        super.initBaseView();
    }

    @Override
    public void onAttachedToWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = 460;
        attributes.height = 700;
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
//        attributes.flags != WindowManager.LayoutParams.TRAN
//        window.setDimAmount(0.5f);
//        window.setBackgroundDrawable(new ColorDrawable(Color.RED));//注意此处
        getWindow().setAttributes(attributes);
        super.onAttachedToWindow();
    }
}
