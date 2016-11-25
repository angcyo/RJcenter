package com.rsen.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * Created by angcyo on 15-12-17 017 13:30 下午.
 */
public class T {

    public static int T_HEIGHT = 40;//dp 默认的高度
    public static int T_OFFSET_Y = 65;//dp y轴偏移量
    public static int T_GRAVITY = Gravity.TOP;//默认的对齐方式
    public static Handler mainHandler = new Handler(Looper.getMainLooper());
    static long lastTime = 0;
    private static Toast toast;

    /**
     * 短时间显示toast.
     *
     * @param content the content
     * @param text    the text
     */
    public static void show(final Context content, final CharSequence text) {
        final String safeText;
        if (TextUtils.isEmpty(text) || text.toString().contains("son")) {
            safeText = "服务器异常,请稍后重试!";
        } else {
            safeText = text.toString();
        }

        if (checkMainThread()) {
            initToast(content.getApplicationContext(), safeText);
            toast.show();
        } else {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(content, safeText);
                }
            });
        }
    }

    /**
     * 长时间显示 Toast .
     *
     * @param content the content
     * @param text    the text
     */
    public static void showL(final Context content, final CharSequence text) {
        if (checkMainThread()) {
            initToast(content.getApplicationContext(), text);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        } else {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    showL(content, text);
                }
            });
        }
    }

    /**
     * 返回一个唯一的toast
     *
     * @param content the content
     * @param text    the text
     * @return the toast
     */
    private static Toast initToast(Context content, CharSequence text) {
        if (toast == null) {
            synchronized (T.class) {
                if (toast == null) {
                    toast = Toast.makeText(content, text, Toast.LENGTH_SHORT);
                    makeToastFullscreen(content, toast);
                    toast.setView(createToastView(content));
                    toast.setGravity(T_GRAVITY, 0, (int) dpToPx(content, T_OFFSET_Y));
                }
            }
        }
        final View rootView = toast.getView().findViewWithTag("root");
        TextView textView = (TextView) toast.getView().findViewWithTag("text");
        textView.setText(text);
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTime < 2000) {
            textView.post(new Runnable() {
                @Override
                public void run() {
//                ViewCompat.setScaleY(rootView, 0);
                    if (rootView != null) {
                        ViewCompat.setTranslationY(rootView, rootView.getMeasuredHeight());
                        ViewCompat.animate(rootView).setInterpolator(new LinearInterpolator()).translationY(0)/*.scaleY(1)*/.setDuration(300).start();
                    }
                }
            });
        }
        lastTime = currentTimeMillis;
        return toast;
    }

    private static View createToastView(Context context) {
        RelativeLayout root = new RelativeLayout(context);
        root.setBackgroundResource(context.getResources().getIdentifier("colorAccent", "color", context.getPackageName()));
        root.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setBackgroundResource(context.getResources().getIdentifier("colorAccent", "color", context.getPackageName()));
//        layout.setBackgroundResource(android.R.color.holo_red_dark);
//        layout.setVerticalGravity(Gravity.VERTICAL_GRAVITY_MASK);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -1);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.setLayoutParams(params);

        ImageView imageView = new ImageView(context);
        imageView.setTag("image");
        imageView.setVisibility(View.GONE);

        TextView textView = new TextView(context);
        textView.setTag("text");
        textView.setTextColor(Color.WHITE);

        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins((int) dpToPx(context, 10), 0, 0, 0);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        layout.addView(imageView, layoutParams);
        layout.addView(textView, layoutParams);
        root.addView(layout);
        return root;
    }

    private static float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    private static void makeToastFullscreen(Context context, Toast toast) {
        try {
            Field mTN = toast.getClass().getDeclaredField("mTN");
            mTN.setAccessible(true);
            Object mTNObj = mTN.get(toast);

            Field mParams = mTNObj.getClass().getDeclaredField("mParams");
            mParams.setAccessible(true);
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams.get(mTNObj);
            params.width = -1;
            params.height = (int) dpToPx(context, T_HEIGHT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
