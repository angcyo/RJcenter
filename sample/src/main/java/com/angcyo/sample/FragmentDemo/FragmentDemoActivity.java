package com.angcyo.sample.FragmentDemo;

import android.os.Bundle;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class FragmentDemoActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_fragment_demo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        mAppbarLayout.setVisibility(View.GONE);
//        mActivityLayout.removeView(mAppbarLayout);
//
//        setSupportActionBar((Toolbar) mViewHolder.v(R.id.toolbar));
//        mAppbarLayout.setFitsSystemWindows(true);
    }

    @Override
    protected void initAfter() {

    }
}
