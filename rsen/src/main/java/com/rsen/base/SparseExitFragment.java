package com.rsen.base;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.angcyo.rsen.R;

import de.greenrobot.event.EventBus;

/**
 * Created by angcyo on 15-07-27-027.
 */
public class SparseExitFragment extends DialogFragment {
    private static final int EXIT_DELAY_TIME = 400;
    private static SparseExitFragment sparseExitFragment;
    RelativeLayout mExitLayout;
    View mExitView1;
    View mExitView2;
    View mExitView3;
    Handler handler = new Handler(Looper.getMainLooper());
    private int code = 0;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mExitLayout == null) return;
            switch (code) {
                case 0:
                    mExitView3.setVisibility(View.INVISIBLE);
                    ++code;
                    handler.postDelayed(runnable, EXIT_DELAY_TIME);
                    break;
                case 1:
                    mExitView2.setVisibility(View.INVISIBLE);
                    ++code;
                    handler.postDelayed(runnable, EXIT_DELAY_TIME);
                    break;
                case 2:
                    mExitView1.setVisibility(View.INVISIBLE);
                    ++code;
                    handler.postDelayed(runnable, EXIT_DELAY_TIME);
                    break;
                default:
                    code = 0;
                    sparseExitFragment.dismiss();
                    sparseExitFragment.getActivity().getSupportFragmentManager().beginTransaction().remove(SparseExitFragment.this);
                    sparseExitFragment = null;
                    break;
            }
        }
    };

    public static void show(AppCompatActivity activity) {
        if (sparseExitFragment == null || activity.getSupportFragmentManager().findFragmentByTag("exit_fragment") == null) {
            sparseExitFragment = new SparseExitFragment();
            sparseExitFragment.show(activity.getSupportFragmentManager(), "exit_fragment");
        } else {
            EventBus.getDefault().post(new ExitEvent());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(STYLE_NO_TITLE);
        dialog.getWindow()
                .getDecorView()
                .setBackgroundColor(
                        getResources().getColor(android.R.color.transparent));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);//重点
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (Build.VERSION.SDK_INT >= 14) {
            dialog.getWindow().setDimAmount(0f);//设置窗口暗淡的程度
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        View view = getActivity().getLayoutInflater().inflate(R.layout.exit_layout, null);
        mExitView1 = view.findViewById(R.id.exit1);
        mExitView2 = view.findViewById(R.id.exit2);
        mExitView3 = view.findViewById(R.id.exit3);

        dialog.setContentView(view);
        handler.postDelayed(runnable, EXIT_DELAY_TIME);
        return dialog;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeCallbacks(runnable);
    }

    public static class ExitEvent {
        @Override
        public String toString() {
            return "请求退出";
        }
    }
}
