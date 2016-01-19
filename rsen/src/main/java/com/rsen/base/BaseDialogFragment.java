package com.rsen.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by angcyo on 15-09-04-004.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewBefore(savedInstanceState);
    }

    protected void initViewBefore(Bundle savedInstanceState) {
        setCancelable(false);
        setStyle(STYLE_NO_TITLE, getTheme());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = loadView(inflater, container, savedInstanceState);
        initView(view);
        return view;
    }

    protected void initView(View view) {

    }

    protected abstract View loadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onStart() {
        super.onStart();
        readyShow();
    }

    protected void readyShow() {
        //        getDialog().getWindow().setDimAmount(0f);
//        getDialog().setCanceledOnTouchOutside(true);
    }
}
