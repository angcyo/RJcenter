package com.angcyo.sample.MediaDemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.angcyo.sample.R;

public class TestActivity extends Activity {

    public static final void show(Context context) {
        Intent intent = new Intent(context, TestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.anim_fix_bug, com.angcyo.rsen.R.anim.default_window_scale_exit_anim);
    }

    public void button(View view) {
        show(this);
    }
}
