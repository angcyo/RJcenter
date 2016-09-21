package com.rsen.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.angcyo.rsen.R;
import com.rsen.angcyo.MaterialDialog;
import com.rsen.angcyo.PopupTipWindow;
import com.rsen.angcyo.ProgressFragment;
import com.rsen.slidr.Slidr;
import com.rsen.util.ResUtil;
import com.rsen.util.T;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public abstract class RBaseActivity extends AppCompatActivity {

    public ProgressFragment progressFragment = null;
    public MaterialDialog mMaterialDialog;
    protected LayoutInflater mLayoutInflater;
    protected ViewGroup mActivityLayout;//Activity的根布局
    protected ViewGroup mAppbarLayout;//toolbar 包裹布局
    protected ViewGroup mFragmentLayout;//重要的包裹布局,包含空布局,无网络布局,加载布局
    protected View mEmptyLayout;//空布局
    protected View mLoadLayout;//加载布局
    protected View mNonetLayout;//无网络布局
    protected FrameLayout mContainerLayout;//内容包裹布局
    protected FloatingActionButton mFab;
    protected Toolbar mToolbar;
    protected RBaseViewHolder mViewHolder;
    private View stateBarView;//状态栏背景View

    /**
     * 获取ActionBar的高度
     */
    public static float getActionBarHeight(Context context) {
        TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        float h = actionbarSizeTypedArray.getDimension(0,
                context.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
        actionbarSizeTypedArray.recycle();
        return h;
    }

    /**
     * 获取状态栏的高度
     */
    public static float getStatusBarHeight(Context context) {
        float result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimension(resourceId);
//            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 返回屏幕宽度(像素)
     */
    public int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 返回屏幕高度(像素, 包含了状态栏的高度)
     */
    public int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*空操作*/
        init();
        /*空操作*/
        initBefore();
        super.onCreate(savedInstanceState);
        mLayoutInflater = LayoutInflater.from(this);
        /*初始化view*/
        initBaseView();
        /*初始化view事件*/
        initBaseViewEvent();
        /*初始化窗口特性*/
        initWindow();

        /*重载方法*/
        initView(savedInstanceState);
        initAfter();
        initEvent();
        initViewData();
    }

    protected void initBaseView() {
        setContentView(R.layout.rsen_base_activity_layout);
        mActivityLayout = (ViewGroup) findViewById(R.id.base_activity_layout);
        mFragmentLayout = (ViewGroup) findViewById(R.id.base_fragment_layout);
        mAppbarLayout = (ViewGroup) findViewById(R.id.base_appbar_layout);
        mLoadLayout = findViewById(R.id.base_load_layout);
        mContainerLayout = (FrameLayout) findViewById(R.id.base_container);
        mEmptyLayout = findViewById(R.id.base_empty_layout);
        mNonetLayout = findViewById(R.id.base_nonet_layout);
        mToolbar = (Toolbar) findViewById(R.id.base_toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.base_fab);
        setSupportActionBar(mToolbar);

        /*设置内容布局*/
        int contentView = getContentView();
        if (contentView == 0) {
            View view = createContentView();
            if (view != null) {
                mFragmentLayout.addView(view,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            mViewHolder = new RBaseViewHolder(mFragmentLayout);
        } else {
            mViewHolder = new RBaseViewHolder(mLayoutInflater.inflate(contentView, mContainerLayout, true));
        }
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
                if (Build.VERSION.SDK_INT > 10 && !Build.MANUFACTURER.equals("Meizu")) {
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else if (Build.VERSION.SDK_INT > 17 && Build.MANUFACTURER.equals("Meizu")) {
                    //魅族更高版本调转的方式与其它手机型号一致  可能之前的版本有些一样  所以另加条件(tsp)
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                RBaseActivity.this.startActivity(intent);
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


    @LayoutRes
    protected abstract int getContentView();

    //设置窗口动画
    protected void initWindow() {

        /*窗口动画*/
        if (enableWindowAnim()) {
            getWindow().setWindowAnimations(getWindowAnimStyle());
        }

        /*透明状态栏*/
        if (enableStatusTranslucent()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//状态栏

                if (enableStatusColor()) {
                    int statusBarHeight = (int) Math.ceil(getStatusBarHeight(this));
                    stateBarView = new View(this);
                    stateBarView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight));
//                    stateBarView.setBackgroundResource(ResUtil.getThemeColorAccent(this));
                    stateBarView.setBackgroundColor(getStateBarColor());
                    ViewGroup viewGroup = ((ViewGroup) findViewById(android.R.id.content));//Window.ID_ANDROID_CONTENT
                    mActivityLayout.setFitsSystemWindows(true);
                    viewGroup.addView(stateBarView);
                    //  mActivityLayout.addView(stateBarView);
                }
            }
        }
        /*透明导航栏*/
        if (enableNavigationTranslucent()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//导航栏

                if (enableNavigationColor()) {

                }
            }
        }
        /*滑动删除*/
        if (enableSlidr()) {
            Slidr.attach(this);
            Drawable background = mActivityLayout.getBackground();
            if (background == null) {
                mActivityLayout.setBackgroundColor(getBackgroundColor());
            }
        }
    }

    protected int getBackgroundColor() {
        return Color.WHITE;
    }

    protected int getStateBarColor() {
        return getResources().getColor(ResUtil.getThemeColorAccent(this));
    }

    /**
     * 激活状态栏半透明
     */
    protected boolean enableStatusTranslucent() {
        return true;
    }

    /**
     * 激活滑动删除
     */
    protected boolean enableSlidr() {
        return false;
    }

    /**
     * 启动状态栏颜色, 默认使用 ColorAccent 颜色, rootView 添加padding
     */
    protected boolean enableStatusColor() {
        return true;
    }

    /**
     * 启动虚拟导航栏颜色, 默认使用 ColorAccent 颜色
     */
    protected boolean enableNavigationColor() {
        return false;
    }

    protected boolean enableNavigationTranslucent() {
        return false;
    }

    /**
     * 启动窗口动画
     */
    protected boolean enableWindowAnim() {
        return false;
    }

    /**
     * 窗口动画样式
     */
    @StyleRes
    protected int getWindowAnimStyle() {
//        return R.style.WindowTranAnim;
        return R.style.WindowScaleAnim;
    }

    //初始化
    private void init() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * Init before _.
     */
    protected void initBefore() {

    }

    protected void initEvent() {

    }

    protected void initViewData() {

    }

    /**
     * Init view.
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * Init after.
     */
    protected void initAfter() {

    }

    protected void hideToolbar() {
//        mAppbarLayout.setVisibility(View.GONE);
        mActivityLayout.removeView(mAppbarLayout);
//        mActivityLayout.requestLayout();
    }

    public void launchActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    public void launchActivity(Class c, Bundle args) {
        Intent intent = new Intent(this, c);
        intent.putExtras(args);
        startActivity(intent);
    }

    public void showDialogTip(String tip) {
        if (progressFragment != null) {
            progressFragment.updateText(tip);
            return;
        }
        progressFragment = ProgressFragment.newInstance(tip);
        progressFragment.show(getSupportFragmentManager(), "dialog_tip");
    }

    public void showDialogTip(String tip, boolean cancel) {
        if (progressFragment != null) {
            progressFragment.updateText(tip);
            return;
        }
        progressFragment = ProgressFragment.newInstance(tip);
        progressFragment.show(getSupportFragmentManager(), "dialog_tip");
    }

    public void hideDialogTip() {
        if (progressFragment != null) {
            progressFragment.dismiss();
            progressFragment = null;
        }
    }

    public void showToast(String msg) {
        T.show(this, msg);
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

    protected void showMaterialDialog(String title, String message,
                                      final View.OnClickListener positiveListener, final View.OnClickListener negativeListener,
                                      DialogInterface.OnDismissListener onDismissListener) {
        mMaterialDialog = new MaterialDialog(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确认", positiveListener)
                .setNegativeButton("取消", negativeListener)
                .setOnDismissListener(onDismissListener);
        mMaterialDialog.setCanceledOnTouchOutside(false);
        mMaterialDialog.show();
        try {
            Field mPositiveButton = mMaterialDialog.getClass().getDeclaredField("mPositiveButton");
            mPositiveButton.setAccessible(true);
            ((Button) mPositiveButton.get(mMaterialDialog)).setTextColor(getResources().getColor(ResUtil.getThemeColorAccent(this)));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    protected void addFragment(Fragment fragment) {
        addFragment(fragment, true);
    }

    protected void addFragment(Fragment fragment, boolean toBack) {
        addFragment(R.id.base_container, fragment, toBack);
    }

    protected void addFragment(@IdRes int viewId, Fragment fragment) {
        addFragment(viewId, fragment, true);
    }

    protected void addFragment(@IdRes int viewId, Fragment fragment, boolean toBack) {
        changeFragment(viewId, fragment, toBack, new OnChangeFragment() {
            @Override
            public void onChangeFragment(FragmentTransaction fragmentTransaction, @IdRes int viewId, Fragment fragment, boolean toBack) {
                fragmentTransaction.add(viewId, fragment, String.valueOf(fragment.hashCode()));
            }
        });
    }

    protected void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, false);
    }

    protected void replaceFragment(Fragment fragment, boolean toBack) {
        replaceFragment(R.id.base_container, fragment, toBack);
    }

    protected void replaceFragment(@IdRes int viewId, Fragment fragment) {
        replaceFragment(viewId, fragment, false);
    }

    protected void replaceFragment(@IdRes int viewId, Fragment fragment, boolean toBack) {
        changeFragment(viewId, fragment, toBack, new OnChangeFragment() {
            @Override
            public void onChangeFragment(FragmentTransaction fragmentTransaction, @IdRes int viewId, Fragment fragment, boolean toBack) {
                fragmentTransaction.replace(viewId, fragment, String.valueOf(fragment.hashCode()));
            }
        });
    }

    private void changeFragment(@IdRes int viewId, Fragment fragment, boolean toBack, OnChangeFragment listener) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_tran_enter_h, R.anim.fragment_tran_exit_h,
                R.anim.fragment_tran_enter_h, R.anim.fragment_tran_exit_h);//动画效果
        listener.onChangeFragment(fragmentTransaction, viewId, fragment, toBack);
        if (toBack) {
            fragmentTransaction.addToBackStack(String.valueOf(fragment.hashCode()));
        }
        fragmentTransaction.commit();
    }

    protected void showFragment(Fragment fragment) {
        if (fragment != null) {
            if (!fragment.isAdded()) {
                addFragment(fragment);
            } else if (!fragment.isVisible()) {
                final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.show(fragment);
                fragmentTransaction.commit();
            }
        }
    }

    protected void showFragment(String tag) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        showFragment(fragment);
    }

    protected void showFragment(@IdRes int resId, Fragment fragment) {
        if (fragment != null) {
            if (!fragment.isAdded()) {
                addFragment(resId, fragment);
            } else if (!fragment.isVisible()) {
                final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.show(fragment);
                fragmentTransaction.commit();
            }
        }
    }

    protected void showFragment(@IdRes int resId, String tag) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        showFragment(resId, fragment);
    }

    protected void removeFragment(String tag) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        removeFragment(fragment);
    }

    protected void removeFragment(Fragment fragment) {
        if (fragment != null && fragment.isAdded() && !fragment.isRemoving()) {
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    }

    protected void hideFragment(Fragment fragment) {
        if (fragment != null && fragment.isVisible()) {
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
        }
    }

    protected void hideFragment(String tag) {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        hideFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        if (moveToBack()) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }

    protected boolean moveToBack() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void noNet(EventNoNet event) {
        hideDialogTip();
        PopupTipWindow.showTip(this, "请检查网络连接.");
    }

    protected void start(Class<?> cls) {
        start(cls, null);
    }

    protected void start(Class<?> cls, Bundle arg) {
        final Intent intent = new Intent(this, cls);
        intent.putExtras(arg);
        startActivity(intent);
    }

    private interface OnChangeFragment {
        void onChangeFragment(FragmentTransaction fragmentTransaction, @IdRes int viewId, Fragment fragment, boolean toBack);
    }

    public static class EventNoNet {

    }
}
