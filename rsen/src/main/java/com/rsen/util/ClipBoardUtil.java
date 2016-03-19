package com.rsen.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

/**
 * Created by angcyo on 15-12-16 016 15:51 下午.
 */
public class ClipBoardUtil {
    /**
     * 将文本复制到剪切板
     *
     * @param context the context
     * @param text    the text
     */
    public static void to(Context context, CharSequence text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(text, text);
            clipboard.setPrimaryClip(clip);
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        }
    }

    /**
     * 从剪切板获取文本
     *
     * @param context the context
     * @return the char sequence
     */
    public static CharSequence from(Context context) {
        CharSequence clip;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clip = clipboard.getPrimaryClip().getItemAt(0).getText();
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clip = clipboard.getText();
        }
        return clip;
    }
}
