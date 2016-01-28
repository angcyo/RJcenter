package com.rsen.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
//        Logger.e(Util.callClassMethodName());
    }

    @Override
    public void onResume() {
        super.onResume();
//        Logger.e(Util.callClassMethodName());
    }

    @Override
    public void onPause() {
        super.onPause();
//        Logger.e(Util.callClassMethodName());
    }

    @Override
    public void onStop() {
        super.onStop();
//        Logger.e(Util.callClassMethodName());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Logger.e(Util.callClassMethodName());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Logger.e(Util.callClassMethodName());
    }

    protected void showMaterialDialog(String title, String message,
                                      final View.OnClickListener positiveListener, final View.OnClickListener negativeListener,
                                      DialogInterface.OnDismissListener onDismissListener) {
        MaterialDialog mMaterialDialog = new MaterialDialog(mBaseActivity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确认", positiveListener)
                .setNegativeButton("取消", negativeListener)
                .setOnDismissListener(onDismissListener);
        mMaterialDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.unbind(this);
//        EventBus.getDefault().unregister(this);
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

    protected void e(String log) {
        Log.e(new Exception().getStackTrace()[0].getClassName(), log);
    }
}
