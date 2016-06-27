package com.rsen.util;

import android.content.Context;

import com.angcyo.rsen.BuildConfig;
import com.rsen.angcyo.RDebugWindow;


/**
 * Created by robi on 2016-06-23 21:20.
 */
public class DebugUtil {
    public static void show(Context context, String msg, int color) {
        if (BuildConfig.DEBUG) {
            RDebugWindow.instance(context).addText(Thread.currentThread().getId() + " " + msg, color);
        }
    }

    public static void show(Context context, String msg) {
        if (BuildConfig.DEBUG) {
            RDebugWindow.instance(context.getApplicationContext()).addText(Thread.currentThread().getId() + " " + msg);
        }
    }
}
