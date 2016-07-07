package com.angcyo.sample.service;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.angcyo.sample.MediaDemo.TestActivity;
import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.github.common.L;

import java.io.File;
import java.io.IOException;

public class ServiceActivity extends RBaseActivity {

    private static final int a = 1;
    private static final int e = 1;
    public static int c = 3;
    public static int f = 3;

    static {
        int g = 4;
        System.out.print("");
    }

    static {
        int b = 2;
        System.out.print("");
    }

    static {
        int d = 4;
        System.out.print("");
    }

    Intent service;

    @Override
    protected void initAfter() {
        L.i(this.getDir("", MODE_PRIVATE).getAbsolutePath());
        L.i(this.getDir("test", MODE_PRIVATE).getAbsolutePath());
        L.i(this.getPackageCodePath());
        L.i(this.getCacheDir().getAbsolutePath());
        L.i(this.getDatabasePath("db_name").getAbsolutePath());
        L.i(this.getExternalCacheDir().getAbsolutePath());
        L.i(this.getFilesDir().getAbsolutePath());
        L.i(this.getObbDir().getAbsolutePath());
        L.i(this.getPackageResourcePath());

        try {
            new File(getFilesDir(), "2016-7-7.zip").createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

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
