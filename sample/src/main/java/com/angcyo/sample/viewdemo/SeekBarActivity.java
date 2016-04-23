package com.angcyo.sample.viewdemo;

import android.os.Bundle;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class SeekBarActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_seek_bar;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("SeekBar自定义样式");
    }
}
