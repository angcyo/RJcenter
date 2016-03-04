package com.angcyo.sample.RecyclerViewDemo;

import android.os.Bundle;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class RecyclerViewActivity2 extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_recycler_view2;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    public void button(View view) {
        throw new IllegalArgumentException("错误");
//
//        Intent intent = new Intent(RCrashHandler.INTENT_ACTION_RESTART_ACTIVITY);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);//去掉动画效果
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        Bundle args = new Bundle();
//        args.putString("msg", "ceshi");
//        intent.putExtras(args);
//        startActivity(intent);
    }

    @Override
    protected void initAfter() {
//        Slidr.attach(this);
    }
}
