package com.rsen.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.angcyo.ondemand.R;
import com.angcyo.ondemand.util.Util;

/**
 * Created by angcyo on 15-09-04-004.
 */
public class ProgressFragment extends BaseDialogFragment {

    private static String KEY_TIP = "tip";
    private String tip;

    public static ProgressFragment newInstance(@NonNull String tip) {
        ProgressFragment progressFragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TIP, tip);
        progressFragment.setArguments(args);
        return progressFragment;
    }

    @Override
    protected View loadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setBackgroundResource(R.drawable.round_bg);

        ProgressBar progressBar = new ProgressBar(getActivity());
        int barSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, getResources().getDisplayMetrics());

        linearLayout.addView(progressBar, new LinearLayout.LayoutParams(barSize, barSize));

        if (!Util.isEmpty(tip)) {
            TextView textView = new TextView(getActivity());
            textView.setPadding(0, 0, (int) getResources().getDimension(R.dimen.layout_margin), 0);
            textView.setText(tip);
            textView.setSingleLine();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                textView.setTextColor(getResources().getColor(android.R.color.white));
            } else {
                textView.setTextColor(getResources().getColor(android.R.color.white, getActivity().getTheme()));
            }
            linearLayout.addView(textView);
        }
        return linearLayout;
    }

    @Override
    protected void initViewBefore(Bundle savedInstanceState) {
        super.initViewBefore(savedInstanceState);
        tip = getArguments().getString(KEY_TIP, "");
    }

    @Override
    protected void readyShow() {
        super.readyShow();
        getDialog().getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
    }
}
