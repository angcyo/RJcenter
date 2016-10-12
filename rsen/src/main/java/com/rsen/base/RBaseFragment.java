package com.rsen.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.angcyo.rsen.R;
import com.rsen.angcyo.MaterialDialog;
import com.rsen.angcyo.ProgressFragment;

import de.greenrobot.event.EventBus;

/**
 * Created by angcyo on 15-08-31-031.
 */
public abstract class RBaseFragment extends Fragment {

    protected RBaseActivity mBaseActivity;
    protected ViewGroup rootView;
    protected boolean isCreate = false;
    protected RBaseViewHolder mViewHolder;
    protected LayoutInflater mLayoutInflater;
    protected ViewGroup mActivityLayout;
    protected ViewGroup mAppbarLayout;
    protected ViewGroup mFragmentLayout;
    protected View mEmptyLayout;
    protected View mLoadLayout;
    protected View mNonetLayout;
    protected FrameLayout mContainerLayout;//内容布局

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData(savedInstanceState, getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        rootView = (ViewGroup) inflater.inflate(R.layout.rsen_base_fragment_layout, container, false);
        initBaseView();
        initBaseViewEvent();

        int contentView = getContentView();
        if (contentView == 0) {
            View view = createContentView();
            if (view != null) {
                rootView.addView(view,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            mViewHolder = new RBaseViewHolder(rootView);
        } else {
            mViewHolder = new RBaseViewHolder(inflater.inflate(contentView, rootView, true));
        }

        initView(rootView);
        initAfter();
        isCreate = true;
        initViewData();
        return rootView;
    }

    protected View createContentView() {
        return null;
    }

    private void initBaseViewEvent() {
        mNonetLayout.findViewById(R.id.base_nonet_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                // 判断手机系统的版本 即API大于10 就是3.0或以上版本及魅族手机
                if (android.os.Build.VERSION.SDK_INT > 10 && !android.os.Build.MANUFACTURER.equals("Meizu")) {
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else if (android.os.Build.VERSION.SDK_INT > 17 && android.os.Build.MANUFACTURER.equals("Meizu")) {
                    //魅族更高版本调转的方式与其它手机型号一致  可能之前的版本有些一样  所以另加条件(tsp)
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                mBaseActivity.startActivity(intent);
            }
        });
        mNonetLayout.findViewById(R.id.base_nonet_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOverlayRefresh(v);
            }
        });
        mEmptyLayout.findViewById(R.id.base_empty_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOverlayRefresh(v);
            }
        });
    }

    protected void onOverlayRefresh(View v) {

    }

    public void showToast(String msg) {
        mBaseActivity.showToast(msg);
    }

    protected void showEmptyLayout() {
//        mContainerLayout.setVisibility(View.GONE);
        mNonetLayout.setVisibility(View.GONE);
        mLoadLayout.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);
    }

    protected void showNonetLayout() {
        //        mContainerLayout.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.GONE);
        mLoadLayout.setVisibility(View.GONE);
        mNonetLayout.setVisibility(View.VISIBLE);
    }

    protected void showLoadLayout() {
        //        mContainerLayout.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.GONE);
        mNonetLayout.setVisibility(View.GONE);
        mLoadLayout.setVisibility(View.VISIBLE);
    }

    protected void hideOverlayLayout() {
        mEmptyLayout.setVisibility(View.GONE);
        mNonetLayout.setVisibility(View.GONE);
        mLoadLayout.setVisibility(View.GONE);
    }

    private void initBaseView() {
        mActivityLayout = (ViewGroup) rootView.findViewById(R.id.base_activity_layout);
        mFragmentLayout = (ViewGroup) rootView.findViewById(R.id.base_fragment_layout);
        mAppbarLayout = (ViewGroup) rootView.findViewById(R.id.base_appbar_layout);
        mLoadLayout = rootView.findViewById(R.id.base_load_layout);
        mContainerLayout = (FrameLayout) rootView.findViewById(R.id.base_container);
        mEmptyLayout = rootView.findViewById(R.id.base_empty_layout);
        mNonetLayout = rootView.findViewById(R.id.base_nonet_layout);
    }

    @LayoutRes
    protected abstract int getContentView();

    protected void initViewData() {
    }

    protected void loadData(Bundle savedInstanceState, Bundle arg) {

    }

    protected abstract void initView(View rootView);

    protected void initAfter() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
    public void onDestroy() {
        super.onDestroy();
        isCreate = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isCreate) {
            if (isVisibleToUser) {
                onShow();
            } else {
                onHide();
            }
        }
    }

    protected void onShow() {

    }

    protected void onHide() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseActivity = (RBaseActivity) context;
    }

    public void showDialogTip(String tip) {
        if (mBaseActivity.progressFragment != null) {
            mBaseActivity.progressFragment.updateText(tip);
            return;
        }
        mBaseActivity.progressFragment = ProgressFragment.newInstance(tip);
        mBaseActivity.progressFragment.show(mBaseActivity.getSupportFragmentManager(), "dialog_tip");
    }

    public void showDialogTip(String tip, boolean cancel) {
        if (mBaseActivity.progressFragment != null) {
            mBaseActivity.progressFragment.updateText(tip);
            return;
        }
        mBaseActivity.progressFragment = ProgressFragment.newInstance(tip);
        mBaseActivity.progressFragment.show(mBaseActivity.getSupportFragmentManager(), "dialog_tip");
    }

    public void hideDialogTip() {
        if (mBaseActivity.progressFragment != null) {
            mBaseActivity.progressFragment.dismiss();
            mBaseActivity.progressFragment = null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mBaseActivity = (RBaseActivity) activity;
    }

    protected void e(String log) {
        Log.e(new Exception().getStackTrace()[0].getClassName(), log);
    }
}
