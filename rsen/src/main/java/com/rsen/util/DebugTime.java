package com.rsen.util;

import android.util.Log;

/**
 * Created by Robi on 2016-03-10 18:23.
 */
public class DebugTime {
    private static long firstTime = 0;

    public static void init() {
        firstTime = System.currentTimeMillis();
    }

    public static void time() {
        time("time ");
    }

    public static void time(String log) {
        if (firstTime == 0) {
            firstTime = System.currentTimeMillis();
        }
        long time = System.currentTimeMillis();

        e("angcyo--> " + log + " " + (time - firstTime) + " 毫秒");
        firstTime = time;
    }

    private static void e(String log) {
        Log.e(new Exception().getStackTrace()[0].getClassName(), log);
    }
}
