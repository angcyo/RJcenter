package com.angcyo.sample.FragmentDemo;

import android.os.Bundle;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.base.RBaseDialogFragment;

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

    public void button1(View view) {new RBaseDialogFragment().show(getSupportFragmentManager(), "");
    }

    public void button2(View view) {

    }

    public void button3(View view) {

    }

    public void button4(View view) {

    }
}
