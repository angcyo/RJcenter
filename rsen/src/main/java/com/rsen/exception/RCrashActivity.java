package com.rsen.exception;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.angcyo.rsen.R;
import com.rsen.base.RBaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RCrashActivity extends RBaseActivity {

    private int clickCount = 0;
    private boolean isExpand = false;
    private TextView mTextView;

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
        mTextView = mViewHolder.tV(R.id.crashTextView);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAlphaAnimation();
                clickCount++;
                expandText();
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
//            mTextView.setText(msg);
//            copyErrorToClipboard(msg);
//        }
        mTextView.setText(getAppInfo());
        playScaleAnimation();
    }

    private void playScaleAnimation() {
        float scaleEnd = 1.2f;
        float scaleStart = 0.2f;
        mTextView.setScaleX(scaleStart);
        mTextView.setScaleY(scaleStart);
        mTextView.animate().scaleX(scaleEnd).scaleY(scaleEnd).setDuration(200)
                .setInterpolator(new AccelerateInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {
                mTextView.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
            }
        }).start();
    }

    private void playAlphaAnimation() {
        mTextView.setAlpha(0.6f);
        mTextView.animate().alpha(1f).setDuration(300)
                .setInterpolator(new AccelerateInterpolator()).start();
    }

    private void expandText() {
        if (isExpand) {
            return;
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String msg = extras.getString("msg");
            mTextView.setText(msg);
            isExpand = true;
            copyErrorToClipboard(msg);
            playScaleAnimation();
        }
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
