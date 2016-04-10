package com.rsen.util;

import com.orhanobut.hawk.Hawk;

/**
 * Created by angcyo on 2016-04-10 17:34.
 */
public class CrashUtil {
    public static final String KEY_RUN = "key_run";
    public static final long run_count = 10l;

    public static void init() {
        Long run = Hawk.get(KEY_RUN, 0l);
        if (run >= run_count) {
            throw new RuntimeException("出错啦,请联系作者解决...");
        }

        Hawk.put(KEY_RUN, ++run);
    }
}
