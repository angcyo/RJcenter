package com.rsen.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
        CharSequence clip = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData primaryClip = clipboard.getPrimaryClip();
            if (primaryClip != null && primaryClip.getItemCount() > 0) {
                clip = primaryClip.getItemAt(0).getText();
            }
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clip = clipboard.getText();
        }
        return clip;
    }

    public static void initListener(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    T.show(context.getApplicationContext(), from(context.getApplicationContext()));
                }
            });
        }
    }

    /**
     * 打开QQ
     */
    public static void openQQ(Context context) {
        openApp(context, "com.tencent.mobileqq");
    }

    /**
     * 打开微信
     */
    public static void openWX(Context context) {
        openApp(context, "com.tencent.mm");
    }

    /**
     * 根据包名,打开程序
     */
    public static void openApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(packageName);
        launchIntentForPackage.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launchIntentForPackage);
    }
}
