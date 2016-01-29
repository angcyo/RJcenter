package com.rsen.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.angcyo.rsen.R;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public abstract class BaseActivity extends AppCompatActivity {

    public static Handler handler;
    public ProgressFragment progressFragment = null;
    public MaterialDialog mMaterialDialog;
    protected LayoutInflater mLayoutInflater;
    protected ViewGroup mActivityLayout;
    protected ViewGroup mAppbarLayout;
    protected ViewGroup mFragmentLayout;
    protected View mEmptyLayout;
    protected View mLoadLayout;
    protected View mNonetLayout;
    protected FrameLayout mContainerLayout;//内容布局
    protected FloatingActionButton mFab;
    protected Toolbar mToolbar;
    protected RBaseViewHolder mViewHolder;

    /**
     * 获取ActionBar的高度
     */
    public static float getActionBarHeight(Context context) {
        TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        float h = actionbarSizeTypedArray.getDimension(0,
                context.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
        return h;
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init();
        initBefore();
        super.onCreate(savedInstanceState);
        initBaseView();
        initBaseViewEvent();
        initView(savedInstanceState);
        initAfter();
        initEvent();
        initViewData();

        initWindow();
    }

    private void initBaseViewEvent() {
        mNonetLayout.findViewById(R.id.nonet_setting).setOnClickListener(new View.OnClickListener() {
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
                BaseActivity.this.startActivity(intent);
            }
        });
        mNonetLayout.findViewById(R.id.nonet_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOverlayRefresh(v);
            }
        });
        mEmptyLayout.findViewById(R.id.empty_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOverlayRefresh(v);
            }
        });
    }

    protected void onOverlayRefresh(View v) {

    }

    private void initBaseView() {
        mLayoutInflater = LayoutInflater.from(this);
        setContentView(R.layout.rsen_base_activity_layout);
        mActivityLayout = (ViewGroup) findViewById(R.id.activity_layout);
        mFragmentLayout = (ViewGroup) findViewById(R.id.fragment_layout);
        mAppbarLayout = (ViewGroup) findViewById(R.id.appbar_layout);
        mLoadLayout = findViewById(R.id.load_layout);
        mContainerLayout = (FrameLayout) findViewById(R.id.container);
        mEmptyLayout = findViewById(R.id.empty_layout);
        mNonetLayout = findViewById(R.id.nonet_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(mToolbar);

        /*设置内容布局*/
        mViewHolder = new RBaseViewHolder(mLayoutInflater.inflate(getContentView(), mContainerLayout, true));
    }

    protected abstract int getContentView();

    //设置窗口动画
    protected void initWindow() {
        getWindow().setWindowAnimations(R.style.WindowAnim);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//状态栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//导航栏

            View stateBarView = new View(this);
            stateBarView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(this)));
            stateBarView.setBackgroundResource(R.color.colorAccent);
            ((ViewGroup) findViewById(android.R.id.content)).addView(stateBarView);
        }
    }

    //初始化
    private void init() {
        handler = new StaticHandler(this);
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
    protected abstract void initAfter();

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

    protected void handMessage(Message msg, int what, Object obj) {

    }

    public void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public void sendMessage(int what, Object obj) {
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    public void sendRunnable(Runnable runnable) {
        handler.post(runnable);
    }

    public void sendDelayRunnable(Runnable runnable, long delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }


    public void removeCallbacks(Runnable runnable) {
        handler.removeCallbacks(runnable);
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
            ((Button) mPositiveButton.get(mMaterialDialog)).setTextColor(getResources().getColor(R.color.colorAccent));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void noNet(EventNoNet event) {
        hideDialogTip();
        PopupTipWindow.showTip(this, "请检查网络连接");
    }

    static class StaticHandler extends Handler {
        BaseActivity context;

        public StaticHandler(BaseActivity context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (context != null && msg != null) {
                context.handMessage(msg, msg.what, msg.obj);
            }
        }
    }

    /**
     * ViewHolder
     */
    public static class RBaseViewHolder {
        private View itemView;

        public RBaseViewHolder(View itemView) {
            this.itemView = itemView;
        }

        public View v(@IdRes int resId) {
            return itemView.findViewById(resId);
        }

        public View view(@IdRes int resId) {
            return v(resId);
        }

        /**
         * 返回 TextView
         */
        public TextView tV(@IdRes int resId) {
            return (TextView) v(resId);
        }

        public TextView textView(@IdRes int resId) {
            return tV(resId);
        }

        /**
         * 返回 ImageView
         */
        public ImageView imgV(@IdRes int resId) {
            return (ImageView) v(resId);
        }

        public ImageView imageView(@IdRes int resId) {
            return imgV(resId);
        }

        /**
         * 返回 ViewGroup
         */
        public ViewGroup groupV(@IdRes int resId) {
            return (ViewGroup) v(resId);
        }

        public ViewGroup viewGroup(@IdRes int resId) {
            return groupV(resId);
        }
    }

    public static class EventNoNet {

    }
}
