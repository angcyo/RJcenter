package com.rsen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：60秒倒计时的按钮
 * 创建人员：Robi
 * 创建时间：2016/10/20 16:52
 * 修改人员：Robi
 * 修改时间：2016/10/20 16:52
 * 修改备注：
 * Version: 1.0.0
 */
public class VerifyButton extends TextView implements View.OnClickListener, Runnable {

    public static final int DEFAULT_COUNT = 60;

    OnClickListener mOnClickListener;
    boolean isCountDownStart = false;
    int countDown = DEFAULT_COUNT;
    String oldText = "验证";

    public VerifyButton(Context context) {
        super(context);
    }

    public VerifyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mOnClickListener = l;
        oldText = getText().toString();
        super.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isCountDownStart) {
            return;
        }
        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
        }
        run();
    }

    private void startCountDown() {
        isCountDownStart = true;
        countDown--;
        setEnabled(false);
        postDelayed(this, 1000);
    }

    private void endCountDown() {
        isCountDownStart = false;
        countDown = DEFAULT_COUNT;
        setEnabled(true);
        setText(oldText);
        removeCallbacks(this);
    }

    @Override
    public void run() {
        setText(countDown + "s");

        if (countDown <= 0) {
            endCountDown();
        } else {
            startCountDown();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        endCountDown();
    }
}
