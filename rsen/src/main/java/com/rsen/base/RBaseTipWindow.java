package com.rsen.base;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by robi on 2016-07-18 22:04.
 */
public abstract class RBaseTipWindow {
    protected Context mContext;
    protected RBaseViewHolder mBaseViewHolder;
    protected WindowManager mWindowManager;
    protected WindowManager.LayoutParams mLayoutParams;
    protected boolean isShow = false;

    public RBaseTipWindow(Context context) {
        mContext = context.getApplicationContext();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        initWindowParams();
    }

    /**
     * 显示窗口
     */
    public void show() {
        if (isShow) {
            return;
        }

        FrameLayout frame = new FrameLayout(mContext);
        frame.setId(View.generateViewId());
        frame.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBaseViewHolder = new RBaseViewHolder(getView(frame));
        mWindowManager.addView(mBaseViewHolder.itemView, mLayoutParams);

        initView();
        isShow = true;
    }

    protected abstract void initView();

    /**
     * 初始化View
     */
    protected abstract View getView(ViewGroup viewGroup);

    /**
     * 隐藏窗口
     */
    public void hide() {
        if (isShow) {
            mWindowManager.removeView(mBaseViewHolder.itemView);
            isShow = false;
        }
    }

    /**
     * 创建浮窗参数
     */
    protected WindowManager.LayoutParams initWindowParams() {
        if (mLayoutParams == null) {
            mLayoutParams = new WindowManager.LayoutParams();
            mLayoutParams.width = getWindowWidth();
            mLayoutParams.height = getWindowHeight();
            mLayoutParams.gravity = getGravity();
            mLayoutParams.x = getOffsetX();
            mLayoutParams.y = getOffsetY();
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;//窗口类型
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN// 覆盖状态栏
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL//窗口外可以点击
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE//不监听按键事件
//                    | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
//                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS//突破窗口限制
//                    | WindowManager.LayoutParams.FLAG_FULLSCREEN
            ;
        }
        return mLayoutParams;
    }

    public boolean isShow() {
        return isShow;
    }

    protected int getOffsetX() {
        return 0;
    }

    protected int getOffsetY() {
        return 0;
    }

    protected int getWindowWidth() {
        return -2;
    }

    protected int getWindowHeight() {
        return -2;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }
}
