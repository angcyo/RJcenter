package com.rsen.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by angcyo on 15-12-17 017 13:30 下午.
 */
public class T {

    private static Toast toast;

    /**
     * 短时间显示toast.
     *
     * @param content the content
     * @param text    the text
     */
    public static void show(Context content, CharSequence text) {
        initToast(content, text);
        toast.setText(text);
        toast.show();
    }

    /**
     * 长时间显示 Toast .
     *
     * @param content the content
     * @param text    the text
     */
    public static void showL(Context content, CharSequence text) {
        initToast(content, text);
        toast.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * 返回一个唯一的toast
     *
     * @param content the content
     * @param text    the text
     * @return the toast
     */
    public static Toast initToast(Context content, CharSequence text) {
        if (toast == null) {
            toast = Toast.makeText(content, text, Toast.LENGTH_SHORT);
        }

        return toast;
    }
}
