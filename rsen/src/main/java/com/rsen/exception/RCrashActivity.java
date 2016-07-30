package com.rsen.exception;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.angcyo.rsen.R;
import com.rsen.base.RBaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RCrashActivity extends RBaseActivity {

    private int clickCount = 0;

    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        return df.format(new Date());
    }

    @Override
    protected int getContentView() {
        return R.layout.rsen_base_crash_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        final TextView textView = mViewHolder.textView(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String msg = extras.getString("msg");
                    textView.setText(msg);
                    copyErrorToClipboard(msg);
                }
                if (clickCount == 4) {
                    restartApp();
                }
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            String msg = extras.getString("msg");
//            mViewHolder.textView(R.id.text).setText(msg);
//            copyErrorToClipboard(msg);
//        }
        mViewHolder.textView(R.id.text).setText(getAppInfo());
    }

    @Override
    protected int getStateBarColor() {
        return Color.parseColor("#FAFAFA");
    }

    @Override
    protected void initAfter() {
        hideToolbar();
    }

    private void restartApp() {
        RCrashHandler.restartApplicationWithIntent(this,
                new Intent(this, RCrashHandler.getLauncherActivity(this)));
    }

    @SuppressWarnings("deprecation")
    private void copyErrorToClipboard(String msg) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(msg, msg);
            clipboard.setPrimaryClip(clip);
        } else {
            //noinspection deprecation
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboard.setText(msg);
        }
    }

    private String getAppInfo() {
        PackageManager pm = getPackageManager();
        StringBuilder stringBuilder = new StringBuilder(getDataTime("yyyy-MM-dd HH:mm:ss SSS")).append("\n");
        try {
            //程序名
            stringBuilder.append(pm.getApplicationInfo(getPackageName(), 0).loadLabel(getPackageManager())).append(" ");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            stringBuilder.append(pm.getPackageInfo(getPackageName(), 0).versionName)
                    .append("--")
                    .append(pm.getPackageInfo(getPackageName(), 0).versionCode)
                    .append("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        stringBuilder.append(Build.VERSION.RELEASE).append(" ").append(Build.VERSION.SDK_INT)
                .append(" ").append(Build.MANUFACTURER).append(" ").append(Build.MODEL).append(" ").append(Build.CPU_ABI);

        return stringBuilder.toString();
    }

}
