package com.rsen.base;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.angcyo.rsen.R;

/**
 * Created by angcyo on 2016-01-30.
 */
public class RBaseDialogFragment extends DialogFragment {
    protected ViewGroup rootView;
    protected RBaseViewHolder mViewHolder;
    protected Window mWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mWindow = getDialog().getWindow();
        WindowManager.LayoutParams mWindowAttributes = mWindow.getAttributes();
        if (isStatusTranslucent() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (isAnimEnabled()) {
            mWindow.setWindowAnimations(getAnimStyles());
        }
        if (!isDimEnabled()) {
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        if (isNoTitle()) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        getDialog().setCanceledOnTouchOutside(canCanceledOnOutside());
        setCancelable(canCancelable());
        if (canTouchOnOutside()) {
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        }

        mWindowAttributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowAttributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowAttributes.gravity = getGravity();

        if (getWindowColor() != 0) {
            mWindow.setBackgroundDrawable(new ColorDrawable(getWindowColor()));
        }
        rootView = (ViewGroup) inflater.inflate(R.layout.rsen_base_dialog_fragment_layout, null);
        mWindow.setAttributes(mWindowAttributes);
//        rootView = (ViewGroup) inflater.inflate(R.layout.rsen_base_dialog_fragment_layout, container, true);


        return rootView;
    }

    /**
     * 返回中心
     */
    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    /**
     * 返回动画样式
     */
    protected
    @StyleRes
    int getAnimStyles() {
        return R.style.DialogWindowAnim;
    }

    /**
     * 返回窗口背景的颜色, 0为不设置
     */
    protected
    @ColorInt
    int getWindowColor() {
        return 0;
    }

    /**
     * 是否无标题
     */
    protected boolean isNoTitle() {
        return true;
    }

    /**
     * 是否背景变暗
     */
    protected boolean isDimEnabled() {
        return false;
    }

    /**
     * 是否开启动画
     */
    protected boolean isAnimEnabled() {
        return true;
    }

    /**
     * 是否状态栏透明
     */
    protected boolean isStatusTranslucent() {
        return true;
    }

    /**
     * 是否可取消
     */
    protected boolean canCancelable() {
        return true;
    }

    /**
     * ...
     */
    protected boolean canCanceledOnOutside() {
        return true;
    }

    /**
     * 窗口外是否可点击
     */
    protected boolean canTouchOnOutside() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
