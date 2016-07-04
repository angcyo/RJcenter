package com.angcyo.sample.service;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.angcyo.sample.MediaDemo.TestActivity;
import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.github.common.L;

public class ServiceActivity extends RBaseActivity {


    Intent service;

    @Override
    protected int getContentView() {
        return R.layout.activity_service;
    }

    @Override
    protected void initBefore() {
        service = new Intent(this, ServiceTest.class);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        hideToolbar();
    }

    public void startService(View view) {
        L.i("开启服务.");
        startService(service);
    }

    public void stopService(View view) {
        L.i("停止服务.");
        stopService(service);
    }

    public void startActivity(View view) {
        L.i("startActivity.");
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
//        overridePendingTransition(com.angcyo.rsen.R.anim.default_window_scale_enter_anim, com.angcyo.rsen.R.anim.default_window_scale_exit_anim);
//        overridePendingTransition(com.angcyo.rsen.R.anim.default_window_scale_enter_anim, com.angcyo.rsen.R.anim.default_window_scale_exit_anim);
//        overridePendingTransition(com.angcyo.rsen.R.anim.default_window_scale_enter_anim, R.anim.anim_fix_bug);
    }

    @Override
    protected boolean enableStatusColor() {
        return true;
    }

    @Override
    protected boolean enableWindowAnim() {
        return false;
    }

    @Override
    protected boolean moveToBack() {
        return true;
    }

    @Override
    protected boolean enableStatusTranslucent() {
        return super.enableStatusTranslucent();
    }

    @Override
    protected int getStateBarColor() {
        return Color.RED;
    }
}
