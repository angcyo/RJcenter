package com.angcyo.sample.SoftInputDemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class SoftInputActivity extends RBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_soft_input;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final EditText editText = (EditText) mViewHolder.v(R.id.edit);
        mViewHolder.v(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        });

        mViewHolder.v(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        mViewHolder.v(R.id.hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                }
                return false;
            }
        });

        View edit5 = mViewHolder.v(R.id.edit5);
        edit5.requestFocus();
        inputMethodManager.showSoftInput(edit5, InputMethodManager.SHOW_IMPLICIT, new ResultReceiver(new Handler(getMainLooper())) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
            }
        });

        setSoftInput();
    }

    @Override
    protected void initAfter() {
        setTitle("软键盘测试");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void setSoftInput() {
        final View activityRootView = ((ViewGroup) this.findViewById(
                android.R.id.content)).getChildAt(0);

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                        if (heightDiff > 100) { // 如果高度差超过100像素，就很有可能是有软键盘...
                            // ToastTool.getCentertToast(getActivity(),
                            // "软键盘弹出");
                        } else {
                            // ToastTool.getCentertToast(getActivity(),
                            // "软键盘隐藏");
                        }
                    }
                });
    }
}
