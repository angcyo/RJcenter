package com.angcyo.sample.service;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

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
