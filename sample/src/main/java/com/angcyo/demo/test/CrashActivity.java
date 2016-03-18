package com.angcyo.demo.test;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.angcyo.sample.R;
import com.rsen.exception.RCrashHandler;

public class CrashActivity extends AppCompatActivity {

    private int clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final TextView textView = (TextView) findViewById(R.id.text);
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
