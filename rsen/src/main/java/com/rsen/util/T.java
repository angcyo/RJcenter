package com.rsen.util;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * Created by angcyo on 15-12-17 017 13:30 下午.
 */
public class T {

    private static Toast toast;
    public static int T_HEIGHT = 40;//dp

    /**
     * 短时间显示toast.
     *
     * @param content the content
     * @param text    the text
     */
    public static void show(Context content, CharSequence text) {
        initToast(content, text);
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
            synchronized (T.class) {
                if (toast == null) {
                    toast = Toast.makeText(content, text, Toast.LENGTH_SHORT);
                    makeToastFullscreen(content, toast);
                    toast.setView(createToastView(content));
                    toast.setGravity(Gravity.BOTTOM, 0, (int) dpToPx(content, T_HEIGHT));
                }
            }
        }
        ((TextView) toast.getView().findViewWithTag("text")).setText(text);
        return toast;
    }

    private static View createToastView(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setBackgroundResource(context.getResources().getIdentifier("colorAccent", "color", context.getPackageName()));
//        layout.setBackgroundResource(android.R.color.holo_red_dark);
//        layout.setVerticalGravity(Gravity.VERTICAL_GRAVITY_MASK);
        layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

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
        return layout;
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
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
