package com.angcyo.bmob;

import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by angcyo on 2016-04-10 21:09.
 */
public class BmobHelper {
    public static final String APPID = "6b375d12467d182a75b73bdf5a351ebb";

    public static void initBmob(Context context) {
        Bmob.initialize(context, APPID);
    }
}
