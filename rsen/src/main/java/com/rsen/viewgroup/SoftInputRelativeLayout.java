package com.rsen.viewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.RelativeLayout;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：当你调用 setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) 之后
 * 修复键盘弹出不会回调 {@link android.view.View.OnLayoutChangeListener}, 此时可以使用 {@link OnWindowInsetsListener} 监听键盘弹出事件
 * 需要 adjustResize(可选) 和 android:fitsSystemWindows="true"(必须)
 * 创建人员：Robi
 * 创建时间：2016/11/22 9:54
 * 修改人员：Robi
 * 修改时间：2016/11/22 9:54
 * 修改备注：
 * Version: 1.0.0
 */
public class SoftInputRelativeLayout extends RelativeLayout {

    private OnWindowInsetsListener mOnWindowInsetsListener;

    public SoftInputRelativeLayout(Context context) {
        super(context);
    }

    public SoftInputRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoftInputRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SoftInputRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SoftInputRelativeLayout setOnWindowInsetsListener(OnWindowInsetsListener listener) {
        this.mOnWindowInsetsListener = listener;
        return this;
    }

    @Override
    public final WindowInsets onApplyWindowInsets(WindowInsets insets) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mOnWindowInsetsListener != null) {
            mOnWindowInsetsListener.onWindowInsets(insets.getSystemWindowInsetLeft(),
                    insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(),
                    insets.getSystemWindowInsetBottom());
        }

        return super.onApplyWindowInsets(insets);
    }

    /**
     * 当窗口需要插入装饰空间时,回调. 比如显示键盘,显示状态栏的时候.
     */
    public interface OnWindowInsetsListener {
        void onWindowInsets(int insetLeft, int insetTop, int insetRight, int insetBottom);
    }
}
