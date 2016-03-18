package com.angcyo.sample.KeyboardDemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.angcyo.sample.R;

public class KeyboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        findViewById(R.id.root_layout).addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            int oldHeight = oldBottom - oldTop;
            int newHeight = bottom - top;
            int keyboardHeight = oldHeight - newHeight;
            Log.e("-->" + newHeight, "old-->" + oldHeight + "  >  " + keyboardHeight);
        });
    }
}
