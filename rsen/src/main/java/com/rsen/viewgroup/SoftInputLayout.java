package com.rsen.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/10/19 9:01
 * 修改人员：Robi
 * 修改时间：2016/10/19 9:01
 * 修改备注：
 * Version: 1.0.0
 */
public class SoftInputLayout extends LinearLayout {
    private static final String TAG = SoftInputLayout.class.getSimpleName();

    /**
     * 内容+键盘的实际最大允许的高度
     */
    private int mRawLayoutHeight = 0;

    /**
     * 当前内容的高度
     */
    private int mCurrentContentHeight = 0;

    /**
     * 内容布局
     */
    private View mContentLayout;

    /**
     * 表情布局
     */
    private View mEmojiLayout;

    private boolean isSoftInputShow = false;

    private boolean isEmojiLayoutShow = false;

    private OnSoftInputChangeListener mOnSoftInputChangeListener;

    public SoftInputLayout(Context context) {
        super(context);
    }

    public SoftInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static void hideSoftInput(Context context, View editText) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        logWH();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new IllegalArgumentException("只能包含2个子view");
        }
        mContentLayout = getChildAt(0);
        mEmojiLayout = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldh == 0) {
            mRawLayoutHeight = h;
        }
        if (mRawLayoutHeight == h) {
            isSoftInputShow = false;
        } else {
            mCurrentContentHeight = h;
            isSoftInputShow = true;
            hideEmojiLayout();
        }
        if (mOnSoftInputChangeListener != null) {
            mOnSoftInputChangeListener.onSoftInputChange(isSoftInputShow, mRawLayoutHeight, h);
        }
        Log.i(TAG, "onSizeChanged: w:" + w + " h:" + h + " oldw:" + oldw + " oldh:" + oldh);
    }

    public void showEmojiLayout() {
        hideSoftInput(getContext(), this);
        LayoutParams params = (LayoutParams) mContentLayout.getLayoutParams();
        params.height = mCurrentContentHeight;
        params.weight = 0;

        LayoutParams params2 = (LayoutParams) mEmojiLayout.getLayoutParams();
//        params2.height = mCurrentContentHeight;
        params2.weight = 1;
        mEmojiLayout.setVisibility(VISIBLE);
        isEmojiLayoutShow = true;
    }

    public void hideEmojiLayout() {
        LayoutParams params = (LayoutParams) mContentLayout.getLayoutParams();
        params.height = 0;
        params.weight = 1;

        LayoutParams params2 = (LayoutParams) mEmojiLayout.getLayoutParams();
        params2.height = 0;
        params2.weight = 0;
        mEmojiLayout.setVisibility(GONE);
        isEmojiLayoutShow = false;
    }

    public boolean handleBack() {
        if (isEmojiLayoutShow) {
            hideEmojiLayout();
            return true;
        }
        return false;
    }

    public void setOnSoftInputChangeListener(OnSoftInputChangeListener onSoftInputChangeListener) {
        mOnSoftInputChangeListener = onSoftInputChangeListener;
    }

    private void logWH() {
        Log.i(TAG, "logWH: W:" + getMeasuredWidth() + " H:" + getMeasuredHeight());
    }

    public interface OnSoftInputChangeListener {
        void onSoftInputChange(boolean show, int layoutHeight, int contentHeight);
    }
}
