package com.rsen.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angcyo.ondemand.util.Util;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by angcyo on 15-08-31-031.
 */
public abstract class BaseFragment extends Fragment {

    protected BaseActivity mBaseActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = loadView(inflater, container, savedInstanceState);
        initView(view);
        initAfter();
        return view;
    }

    protected abstract void loadData(Bundle savedInstanceState);

    protected abstract View loadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initView(View rootView);

    protected void initAfter() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.e(Util.callClassMethodName());
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.e(Util.callClassMethodName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.e(Util.callClassMethodName());
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.e(Util.callClassMethodName());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.e(Util.callClassMethodName());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.e(Util.callClassMethodName());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseActivity = (BaseActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mBaseActivity = (BaseActivity) activity;
    }
}
