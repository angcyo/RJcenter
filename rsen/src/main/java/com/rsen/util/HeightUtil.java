package com.rsen.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.Window;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2016/10/18 17:56
 * 修改人员：Robi
 * 修改时间：2016/10/18 17:56
 * 修改备注：
 * Version: 1.0.0
 */
public class HeightUtil {

    /**
     * 屏幕高度, 包括状态栏的高度,和底部虚拟导航栏的高度
     */
    public static int getScreenHeight(Activity activity) {
        Rect out = new Rect();
        activity.getWindow().getDecorView().getHitRect(out);
        return out.height();
    }

    /**
     * 窗口的高度,包括状态栏的高度
     */
    public static int getWindowHeight(Activity activity) {
        Rect out = new Rect();
        activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHitRect(out);
        return out.height();
    }

    /**
     * 导航栏的高度
     */
    public static int getNavigationHeight(Activity activity) {
        int screenHeight = getScreenHeight(activity);
        int windowHeight = getWindowHeight(activity);
        return screenHeight - windowHeight;
    }

    /**
     * 状态栏的高度
     */
    public static int getStatusHeight(Activity activity) {
        Rect out = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(out);
        return out.top;
    }
}
