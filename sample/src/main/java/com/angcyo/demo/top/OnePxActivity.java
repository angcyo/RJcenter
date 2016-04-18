package com.angcyo.demo.top;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by angcyo on 15-12-24 024 12:04 下午.
 */
public class OnePxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        window.setType(WindowManager.LayoutParams.TYPE_TOAST);

        TextView textView = new TextView(this);
        textView.setText("one px");

        setContentView(textView);


//        showOnePxWindow();
    }

    public void showOnePxWindow() {
        TextView textView = new TextView(this);
        textView.setText("one px");
        textView.setBackgroundColor(Color.BLUE);

        WindowManager wm = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.format = PixelFormat.RGBA_8888;
        params.width = 1;
        params.height = 1;

        params.x = 1;
        params.y = 1;

        wm.addView(textView, params);
    }

    private void showTopWindow() {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;// LayoutParams.TYPE_PHONE;//
        // LayoutParams.TYPE_SYSTEM_OVERLAY;//
        wmParams.format = 1;

        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

//        wmParams.XUtil = XUtil;
//        wmParams.y = y;
    }
}
