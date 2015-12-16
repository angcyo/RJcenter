package com.rsen.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by angcyo on 15-12-16 016 15:41 下午.
 */
public class Utils {


    /**
     * 去除字符串左右的字符
     *
     * @param des the des
     * @return the string
     */
    public static String trimMarks(String des) {
        return trimMarks(des, 1);
    }

    /**
     * 去除字符串左右指定个数的字符
     *
     * @param des   the des
     * @param count the count
     * @return the string
     */
    public static String trimMarks(String des, int count) {
        if (des == null || count < 0 || des.length() < count + 1) {
            return des;
        }
        return des.substring(count, des.length() - count);
    }

    /**
     * 返回现在的时间,不包含日期
     *
     * @return the now time
     */
    public static String getNowTime() {
        return getNowTime("HH:mm:ss");
    }

    /**
     * Gets now time.
     *
     * @param pattern the pattern
     * @return the now time
     */
    public static String getNowTime(String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }

    /**
     * 判断字符串是否为空
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() < 1);
    }


    /**
     * Gets time.
     *
     * @return 按照HH :mm:ss 返回时间
     */
    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * Gets date.
     *
     * @return 按照yyyy -MM-dd 格式返回日期
     */
    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    /**
     * Gets date and time.
     *
     * @return 按照yyyy -MM-dd HH:mm:ss 的格式返回日期和时间
     */
    public static String getDateAndTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * Gets date.
     *
     * @param pattern 格式
     * @return 返回日期 date
     */
    public static String getDate(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

    /**
     * Gets time.
     *
     * @param pattern 格式
     * @return 按照指定格式返回时间 time
     */
    public static String getTime(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

    /**
     * Gets date and time.
     *
     * @param pattern 指定的格式
     * @return 按照指定格式返回日期和时间 date and time
     */
    public static String getDateAndTime(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

//

    /**
     * @param context 上下文
     * @return 返回手机号码 tel number
     */
    public static String getTelNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }


    /**
     * Gets os version.
     *
     * @return 获取device的os version
     */
    public static String getOsVersion() {
        String string = Build.VERSION.RELEASE;
        return string;
    }

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

    /**
     * Gets os sdk.
     *
     * @return 返回设备sdk版本 os sdk
     */
    public static String getOsSdk() {
        int sdk = Build.VERSION.SDK_INT;
        return String.valueOf(sdk);
    }

    /**
     * Gets random.
     *
     * @return the random
     */
    public static int getRandom() {
        Random random = new Random();
        return random.nextInt();
    }

    /**
     * 获取随机数
     *
     * @param n 最大范围
     * @return random
     */
    public static int getRandom(int n) {
        Random random = new Random();
        return random.nextInt(n);
    }

    /**
     * 获取字符数组中随机的字符串
     *
     * @param context the context
     * @param resId   the res id
     * @return random string
     */
    public static String getRandomString(Context context, int resId) {
        String[] strings;
        strings = context.getResources().getStringArray(resId);

        return strings[getRandom(strings.length)];
    }

    /**
     * 从资源id获取字符串
     *
     * @param context 上下文
     * @param id      资源id
     * @return 字符串 string for res
     */
    public static String getStringForRes(Context context, int id) {
        if (context == null) {
            return "";
        }
        return context.getResources().getString(id);
    }

    /**
     * 返回app的版本名称.
     *
     * @param context the context
     * @return app version name
     */
    public static String getAppVersionName(Context context) {
        String version = "unknown";
// 获取package manager的实例
        PackageManager packageManager = context.getPackageManager();
// getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
// Log.i("版本名称:", version);
        return version;
    }

    /**
     * 返回app的版本代码.
     *
     * @param context the context
     * @return app version code
     */
    public static int getAppVersionCode(Context context) {
// 获取package manager的实例
        PackageManager packageManager = context.getPackageManager();
// getPackageName()是你当前类的包名，0代表是获取版本信息
        int code = 1;
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            code = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
// Log.i("版本代码:", version);
        return code;
    }


    /**
     * 获取屏幕的宽度高度
     *
     * @param context the context
     * @param size    the size
     * @return the display
     */
    public static DisplayMetrics getDisplay(Context context, Point size) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(size);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }


    /**
     * 返回调用行的方法 所在行号
     *
     * @return the string
     */
    public static String callMethodAndLine() {
        StringBuilder result = new StringBuilder();
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        result.append(thisMethodStack.getClassName() + ".");
        result.append(thisMethodStack.getMethodName());
        result.append("(" + thisMethodStack.getFileName());
        result.append(":" + thisMethodStack.getLineNumber() + ")");
        return result.toString();
    }

    /**
     * 返回调用行的类名
     *
     * @return the string
     */
    public static String callClassName() {
        StringBuilder result = new StringBuilder();
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        result.append(thisMethodStack.getClassName());
        return result.toString();
    }

    /**
     * 返回调用行的方法名
     *
     * @return the string
     */
    public static String callMethodName() {
        StringBuilder result = new StringBuilder();
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        result.append(thisMethodStack.getMethodName());
        return result.toString();
    }

    /**
     * 返回调用行的 类名 和 方法名
     *
     * @return the string
     */
    public static String callClassMethodName() {
        StringBuilder result = new StringBuilder();
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        result.append(thisMethodStack.getClassName() + ".");
        result.append(thisMethodStack.getMethodName());
        return result.toString();
    }


    /**
     * 打开网页,调用系统应用
     *
     * @param context the context
     * @param url     the url
     */
    public static void openUrl(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (!url.toLowerCase().startsWith("http:") || !url.toLowerCase().startsWith("https:")) {
            url = "http:".concat(url);
        }

        Uri webPage = Uri.parse(url);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webPage);
        context.startActivity(webIntent);
    }
}
