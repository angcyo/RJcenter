package com.rsen.base;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.angcyo.rsen.R;


/**
 * Created by angcyo on 15-09-01-001.
 */
public class PopupTipWindow {
    public static final int ICO_TYPE_NOICO = 0;//无图标
    public static final int ICO_TYPE_SUCCEED = 1;//打钩图标
    public static final int ICO_TYPE_FAILED = 2;//打叉图标
    public static final int ICO_TYPE_INFO = 3;//感叹号图标
    public static int POPTIP_TIME = 3000;//提示窗口消失的时间
    public static int POPTIP_OFFSET_Y = 200;//提示窗口消失的时间
    private static long START_SHOW_TIME = 0l;//窗口开始显示的时间
    private static long END_SHOW_TIME = 0l;//隐藏的时间
    private static PopupTipWindow popupTipWindow;
    static Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                END_SHOW_TIME = System.currentTimeMillis();
                if ((END_SHOW_TIME - START_SHOW_TIME) >= POPTIP_TIME) {
                    if (popupTipWindow != null) {
                        popupTipWindow.remove();
                        popupTipWindow = null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                popupTipWindow = null;
            }
        }
    };
    private WindowManager wManager;
    private TextView rootLayout;
    private String tip;

    public PopupTipWindow(Context context, String tip) {
        this.tip = tip;
        wManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        rootLayout = new TextView(context);
        rootLayout.setText(tip);
        rootLayout.setPadding(20, 10, 10, 10);
        rootLayout.setGravity(Gravity.CENTER_VERTICAL);
        rootLayout.setSingleLine();
        rootLayout.setBackgroundResource(R.color.colorAccent);
    }

    public static void showTip(Context context, String tip) {
        showTip(context, ICO_TYPE_NOICO, tip);
    }

    public static void showTip(Context context, int icoType, String tip) {
        try {
            if (popupTipWindow == null) {
                popupTipWindow = new PopupTipWindow(context, tip);
                popupTipWindow.show(icoType);
            } else {
                popupTipWindow.update(icoType, tip);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void show(int icoType) {
        //更新view
        switch (icoType) {
            case ICO_TYPE_SUCCEED:
                rootLayout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gou, 0, 0, 0);
                rootLayout.setTextColor(Color.YELLOW);
                break;
            case ICO_TYPE_FAILED:
                rootLayout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cha, 0, 0, 0);
                rootLayout.setTextColor(rootLayout.getContext().getResources().getColor(android.R.color.holo_red_dark));
                break;
            case ICO_TYPE_INFO:
                rootLayout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.info, 0, 0, 0);
                rootLayout.setTextColor(Color.WHITE);
                break;
            case ICO_TYPE_NOICO:
                rootLayout.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                rootLayout.setTextColor(Color.WHITE);
                break;
            default:
                rootLayout.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                rootLayout.setTextColor(Color.WHITE);
                break;
        }

        rootLayout.setText(" " + tip);
        wManager.addView(rootLayout, createParams());
        autoHide();
    }

    private void update(int icoType, String tip) {
        //更新view
        switch (icoType) {
            case ICO_TYPE_SUCCEED:
                rootLayout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gou, 0, 0, 0);
                rootLayout.setTextColor(Color.YELLOW);
                break;
            case ICO_TYPE_FAILED:
                rootLayout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cha, 0, 0, 0);
                rootLayout.setTextColor(Color.RED);
                break;
            case ICO_TYPE_INFO:
                rootLayout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.info, 0, 0, 0);
                rootLayout.setTextColor(Color.WHITE);
                break;
            case ICO_TYPE_NOICO:
                rootLayout.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                rootLayout.setTextColor(Color.WHITE);
            default:
                break;
        }
        rootLayout.setText(" " + tip);
        wManager.updateViewLayout(rootLayout, createParams());
        autoHide();
    }

    private WindowManager.LayoutParams createParams() {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
//            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        wmParams.format = 1;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.RIGHT | Gravity.TOP;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.x = 0;
        wmParams.y = POPTIP_OFFSET_Y;
        wmParams.windowAnimations = R.style.PopupTipWindowAnim;
        return wmParams;
    }

    private void autoHide() {
        //回调
        rootLayout.removeCallbacks(hideRunnable);
        START_SHOW_TIME = System.currentTimeMillis();
        rootLayout.postDelayed(hideRunnable, POPTIP_TIME);
//        rootLayout.setText(" " + tip);
    }

    private void remove() {
        if (wManager != null) {
            if (rootLayout != null) {
                rootLayout.removeCallbacks(hideRunnable);
                wManager.removeViewImmediate(rootLayout);
            }
        }
    }
}
