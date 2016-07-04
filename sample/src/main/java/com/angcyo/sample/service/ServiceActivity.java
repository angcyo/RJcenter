package com.angcyo.sample.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.github.common.L;

public class ServiceActivity extends Activity {


    Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new Intent(this, ServiceTest.class);
        setContentView(R.layout.activity_service);
    }

    public void startService(View view) {
        L.i("开启服务.");
        startService(service);
    }

    public void stopService(View view) {
        L.i("停止服务.");
        stopService(service);
    }
}
