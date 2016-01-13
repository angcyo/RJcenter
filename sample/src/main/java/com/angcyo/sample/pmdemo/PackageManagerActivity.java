package com.angcyo.sample.pmdemo;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.angcyo.sample.R;

public class PackageManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initView();
    }

    TextView textView;
    private void initView() {
        textView = (TextView) findViewById(R.id.text);

        StringBuilder builder = new StringBuilder();
        builder.append(getAllActivitys());
        builder.append(getAllServices());
        builder.append(getAllReceivers());
        textView.setText(builder.toString());
    }

    private PackageInfo getPackInfo(int flag) {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), flag);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAllActivitys() {
        StringBuilder builder = new StringBuilder();
        line(builder);
        builder.append("----------所有的Activity");
        ActivityInfo[] activities = getPackInfo(PackageManager.GET_ACTIVITIES).activities;
        for (ActivityInfo info : activities) {
            line(builder);
            builder.append(info.loadLabel(getPackageManager()));
        }

        return builder.toString();
    }

    private String getAllServices() {
        StringBuilder builder = new StringBuilder();
        line(builder);
        builder.append("----------所有的Services");
        ServiceInfo[] services = getPackInfo(PackageManager.GET_SERVICES).services;
        for (ServiceInfo info : services) {
            line(builder);
            builder.append(info.loadLabel(getPackageManager()));
        }

        return builder.toString();
    }

    private String getAllReceivers() {
        StringBuilder builder = new StringBuilder();

        line(builder);
        builder.append("----------所有的Receivers");
        ActivityInfo[] receivers = getPackInfo(PackageManager.GET_RECEIVERS).receivers;
        for (ActivityInfo info : receivers) {
            line(builder);
            builder.append(info.loadLabel(getPackageManager()));
        }

        return builder.toString();
    }

    private void line(StringBuilder builder) {
        builder.append("\n");
    }
}
