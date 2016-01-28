package com.rsen.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.angcyo.rsen.R;

public abstract class BaseActivity extends AppCompatActivity {

    public static Handler handler;
    private ProgressFragment progressFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        init();
        initBefore();
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
        initAfter();
        initEvent();
        initViewData();

        initWindowAnim();
    }

    //设置窗口动画
    private void initWindowAnim() {
        getWindow().setWindowAnimations(R.style.WindowAnim);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//状态栏
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
//        progressFragment = ProgressFragment.newInstance(tip);
//        progressFragment.show(getSupportFragmentManager(), "dialog_tip");
    }

    public void hideDialogTip() {
        if (progressFragment != null) {
            progressFragment.dismiss();
            progressFragment = null;
        }
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
        MaterialDialog mMaterialDialog = new MaterialDialog(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确认", positiveListener)
                .setNegativeButton("取消", negativeListener)
                .setOnDismissListener(onDismissListener);
        mMaterialDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//        ButterKnife.unbind(this);
    }

//    @Subscribe(threadMode = ThreadMode.MainThread)
//    public void noNet(EventNoNet event) {
//        hideDialogTip();
//        PopupTipWindow.showTip(this, "请检查网络连接");
//    }

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
}
