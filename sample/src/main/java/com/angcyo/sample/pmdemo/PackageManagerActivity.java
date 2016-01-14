package com.angcyo.sample.pmdemo;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.angcyo.sample.BuildConfig;
import com.angcyo.sample.R;

public class PackageManagerActivity extends AppCompatActivity {

    TextView textView;

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

    private void initView() {
        textView = (TextView) findViewById(R.id.text);
        String ac = "----------所有的Activitys";
        SpannableStringBuilder acSpanBuilder = new SpannableStringBuilder(ac + getAllActivitys());
        acSpanBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, ac.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        String sr = "----------所有的Services";
        SpannableStringBuilder srSpanBuilder = new SpannableStringBuilder(sr + getAllServices());
        srSpanBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, sr.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        String re = "----------所有的Receivers";
        SpannableStringBuilder reSpanBuilder = new SpannableStringBuilder(re + getAllReceivers());
        reSpanBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, re.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        String bs = "----------编译信息";
        SpannableStringBuilder bsSpanBuilder = new SpannableStringBuilder(bs + getBuildInfo());
        bsSpanBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, bs.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        String ot = "----------其他信息";
        SpannableStringBuilder otSpanBuilder = new SpannableStringBuilder(ot + getOtherInfo());
        otSpanBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, ot.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        textView.setText(acSpanBuilder.append(srSpanBuilder).append(reSpanBuilder).append(bsSpanBuilder).append(otSpanBuilder));
    }

    private CharSequence getBuildInfo() {
        StringBuilder builder = new StringBuilder();
        line(builder);
        builder.append("编译时间:" + getString(R.string.build_time));
        line(builder);
        builder.append("编译主机:" + getString(R.string.os_name));
        line(builder);
        builder.append("编译用户:" + getString(R.string.build_host));
        line(builder);
        builder.append("编译类型:" + BuildConfig.buildType);
        line(builder);
        return builder.toString();
    }

    private CharSequence getOtherInfo() {
        StringBuilder builder = new StringBuilder();
        line(builder);
        builder.append("Java版本:" + getString(R.string.java_version));
        line(builder);
        builder.append("Git版本:" + getString(R.string.build_revision));
        line(builder);
        return builder.toString();
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
        ActivityInfo[] activities = getPackInfo(PackageManager.GET_ACTIVITIES).activities;
        for (ActivityInfo info : activities) {
            line(builder);
            builder.append(info.name);
        }
        line(builder);

        return builder.toString();
    }

    private String getAllServices() {
        StringBuilder builder = new StringBuilder();
        ServiceInfo[] services = getPackInfo(PackageManager.GET_SERVICES).services;
        for (ServiceInfo info : services) {
            line(builder);
            builder.append(info.name);
        }
        line(builder);

        return builder.toString();
    }

    private String getAllReceivers() {
        StringBuilder builder = new StringBuilder();

        ActivityInfo[] receivers = getPackInfo(PackageManager.GET_RECEIVERS).receivers;
        for (ActivityInfo info : receivers) {
            line(builder);
            builder.append(info.name);
        }
        line(builder);

        return builder.toString();
    }

    private void line(StringBuilder builder) {
        builder.append("\n");
    }
}
