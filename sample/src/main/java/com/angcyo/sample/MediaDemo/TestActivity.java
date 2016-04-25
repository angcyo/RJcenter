package com.angcyo.sample.MediaDemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
}
