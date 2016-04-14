package com.rsen.exception;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.angcyo.rsen.R;
import com.rsen.base.RBaseActivity;

public class RCrashActivity extends RBaseActivity {

    private int clickCount = 0;

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
    protected int getStateBarColor() {
        return Color.WHITE;
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
}
