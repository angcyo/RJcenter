package com.rsen.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.orhanobut.hawk.Hawk;

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
    private static final String KEY_KEYBOARD_HEIGHT = "keyboard_height";

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

    public static void showSoftInput(Context context, View editText) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInput(editText, 0);
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
        mContentLayout.setVisibility(VISIBLE);
        mEmojiLayout.setVisibility(GONE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //拿到第一次的高度, 一般这个时候都是布局的真是高度.之后resize会改变这个高度
        if (oldh == 0) {
            mRawLayoutHeight = h;
        }
        if (mRawLayoutHeight == h) {
            isSoftInputShow = false;
        } else {
            //键盘弹出的时候,高度会变化, 需要隐藏表情布局
            if (mCurrentContentHeight != h) {
                fixContentLayoutHeight(mCurrentContentHeight, h);
            }
            mCurrentContentHeight = h;
            isSoftInputShow = true;
            hideEmojiLayout();
        }
        if (mOnSoftInputChangeListener != null) {
            mOnSoftInputChangeListener.onSoftInputChange(isSoftInputShow, mRawLayoutHeight, h);
        }
//        Log.i(TAG, "onSizeChanged: w:" + w + " h:" + h + " oldw:" + oldw + " oldh:" + oldh);
    }

    /**
     * 修改第一次打开表情之前, 键盘没有弹出之前的BUG.
     */
    private void fixContentLayoutHeight(final int oldH, final int newH) {
        Hawk.put(KEY_KEYBOARD_HEIGHT, mRawLayoutHeight - newH);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                LayoutParams params = (LayoutParams) mContentLayout.getLayoutParams();
                params.height = newH;
                params.weight = 0;
                requestLayout();
                unlockContentLayoutHeight();
            }
        }, 100);
    }

    /**
     * 此方法的作用就是在键盘弹出后,然后按下了键盘上的关闭键盘按钮, 布局能自适应.
     */
    private void unlockContentLayoutHeight() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                LayoutParams params = (LayoutParams) mContentLayout.getLayoutParams();
                params.height = 0;
                params.weight = 1;
                requestLayout();
            }
        }, 300);
    }

    /**
     * 强制内容和表情布局的高度,这样在键盘弹出的时候,就不会导致布局跳动了
     */
    public void showEmojiLayout() {
        hideSoftInput(getContext(), this);
        int keyboardHeight;
        if (mCurrentContentHeight == 0) {
            keyboardHeight = getDefaultEmojiHeight();
            mCurrentContentHeight = mRawLayoutHeight - keyboardHeight;
        } else {
            keyboardHeight = Math.max(getDefaultEmojiHeight(), mRawLayoutHeight - mCurrentContentHeight);
        }

        LayoutParams contentParams = (LayoutParams) mContentLayout.getLayoutParams();
        contentParams.height = mRawLayoutHeight - keyboardHeight;
        contentParams.weight = 0;
        LayoutParams emojiParams = (LayoutParams) mEmojiLayout.getLayoutParams();
        emojiParams.height = keyboardHeight;
        emojiParams.weight = 0;
        mEmojiLayout.setVisibility(VISIBLE);
        requestLayout();
        if (!isEmojiLayoutShow) {
            if (mOnSoftInputChangeListener != null) {
                mOnSoftInputChangeListener.onEmojiLayoutChange(true, mRawLayoutHeight, keyboardHeight);
            }
        }
        isEmojiLayoutShow = true;
    }

    /**
     * 恢复成默认就行了
     */
    public void hideEmojiLayout() {
        LayoutParams params = (LayoutParams) mContentLayout.getLayoutParams();
        params.height = 0;
        params.weight = 1;
        LayoutParams params2 = (LayoutParams) mEmojiLayout.getLayoutParams();
        params2.height = 0;
        params2.weight = 0;
        mEmojiLayout.setVisibility(GONE);
        if (isEmojiLayoutShow) {
            if (mOnSoftInputChangeListener != null) {
                mOnSoftInputChangeListener.onEmojiLayoutChange(false, mRawLayoutHeight, 0);
            }
        }
        isEmojiLayoutShow = false;
        requestLayout();
    }

    /**
     * 当表情显示的时候, 按下返回键,优先隐藏表情, 然后再finish activity
     */
    public boolean handleBack() {
        if (isEmojiLayoutShow) {
            int rawLayoutHeight = mRawLayoutHeight;
            int height = mEmojiLayout.getMeasuredHeight();
            hideEmojiLayout();
            if (mOnSoftInputChangeListener != null) {
                mOnSoftInputChangeListener.onEmojiLayoutChange(false, rawLayoutHeight, height);
            }
            return true;
        }
        return false;
    }

    public boolean isSoftInputShow() {
        return isSoftInputShow;
    }

    public boolean isEmojiLayoutShow() {
        return isEmojiLayoutShow;
    }

    public void setOnSoftInputChangeListener(OnSoftInputChangeListener onSoftInputChangeListener) {
        mOnSoftInputChangeListener = onSoftInputChangeListener;
    }

    private void logWH() {
        Log.i(TAG, "logWH: W:" + getMeasuredWidth() + " H:" + getMeasuredHeight());
    }

    /**
     * 默认的表情布局高度
     */
    private int getDefaultEmojiHeight() {
        int height = Hawk.get(KEY_KEYBOARD_HEIGHT, -1);
        if (height > 50) {
            return height;
        }
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
    }

    public interface OnSoftInputChangeListener {
        void onSoftInputChange(boolean show, int layoutHeight, int contentHeight);

        void onEmojiLayoutChange(boolean show, int layoutHeight, int emojiHeight);
    }
}
