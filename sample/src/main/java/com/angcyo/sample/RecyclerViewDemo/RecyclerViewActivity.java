package com.angcyo.sample.RecyclerViewDemo;

import android.os.Bundle;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class RecyclerViewActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_recycler_view;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initAfter() {
//        Slidr.attach(this);
    }
}
