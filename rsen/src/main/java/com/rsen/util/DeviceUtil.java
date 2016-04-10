package com.rsen.util;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by angcyo on 2016-04-10 21:28.
 */
public class DeviceUtil {
    /**
     * Gets device name.
     *
     * @return 返回设备型号 device name
     */
    public static String getDeviceName() {
        String string = Build.MODEL;
        return string;
    }

    /**
     * 返回设备制造商,和型号
     * Taken from: http://stackoverflow.com/a/12707479/1254846
     *
     * @return The device model name (i.e., "LGE Nexus 5")
     */
    public static String getDeviceModelName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    /**
     * 返回首字母大写
     *
     * @param s The string to capitalize
     * @return The capitalized string
     */
    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getIMEI(Context context) {
        return ((TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    public static String getAndroidId(Context context) {
        return android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
    }

    public static String getSimSerialNumber(Context context) {
        return ((TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE)).getSimSerialNumber();
    }

    public static String getSerialNumber1() {
        return android.os.Build.SERIAL;
    }
}
