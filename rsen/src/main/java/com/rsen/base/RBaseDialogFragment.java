package com.rsen.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.angcyo.rsen.R;

/**
 * 对话框DialogFragment的基类
 * Created by angcyo on 2016-01-30.
 */
public abstract class RBaseDialogFragment extends DialogFragment {
    public static final String KEY_TITLE = "title";
    public static final String KEY_MSG = "msg";
    static final String TAG = "RBaseDialogFragment";
    protected ViewGroup mRootView;
    protected RBaseViewHolder mViewHolder;
    protected Window mWindow;
    protected AppCompatActivity mBaseActivity;
    protected String mDialogTitle;
    protected String mDialogMsg;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mBaseActivity = (AppCompatActivity) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog: ");
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        initArguments(getArguments());
        final Dialog dialog = getDialog();
        mWindow = dialog.getWindow();
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (isNoTitle()) {
            mWindow.requestFeature(Window.FEATURE_NO_TITLE);//必须放在setContextView之前调用
        }
        mRootView = (ViewGroup) inflater.inflate(getContentView(), (ViewGroup) mWindow.findViewById(android.R.id.content), false);
        mViewHolder = new RBaseViewHolder(mRootView);

        //此段代码,会出现软键盘覆盖界面的BUG
        if (isStatusTranslucent() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && getGravity() != Gravity.BOTTOM) {
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (isAnimEnabled()) {
            mWindow.setWindowAnimations(getAnimStyles());
        }

        if (!isDimEnabled()) {
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        dialog.setCanceledOnTouchOutside(canCanceledOnOutside());
        setCancelable(canCancelable());
        if (canTouchOnOutside()) {
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        }

        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams mWindowAttributes = mWindow.getAttributes();
        mWindowAttributes.width = getWindowWidth();//这个属性需要配合透明背景颜色,才会真正的 MATCH_PARENT
        mWindowAttributes.height = getWindowHeight();
        mWindowAttributes.gravity = getGravity();
        mWindow.setAttributes(mWindowAttributes);

//        mViewHolder = new RBaseViewHolder(inflater.inflate(getContentView(), mRootView));
        initView(savedInstanceState);
        initViewEvent();
        setShowsDialog(true);
        return mRootView;
    }

    public RBaseActivity getBaseActivity() {
        if (mBaseActivity instanceof RBaseActivity) {
            return ((RBaseActivity) mBaseActivity);
        }
        return null;
    }

    protected void initArguments(Bundle arguments) {
        if (arguments != null) {
            mDialogTitle = arguments.getString(KEY_TITLE);
            mDialogMsg = arguments.getString(KEY_MSG);
        }

    }

    protected int getWindowHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    protected int getWindowWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    protected void initView(Bundle savedInstanceState) {
    }

    protected void initViewEvent() {
    }

    protected abstract int getContentView();

    /**
     * 返回中心
     */
    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    /**
     * 返回动画样式
     */
    @StyleRes
    protected int getAnimStyles() {
        return R.style.DialogWindowAnim;
    }

    /**
     * 是否无标题
     */
    protected boolean isNoTitle() {
        return true;
    }

    /**
     * 是否全屏
     */
    protected boolean isFullScreen() {
        return false;
    }

    /**
     * 是否背景变暗
     */
    protected boolean isDimEnabled() {
        return true;
    }

    /**
     * 是否开启动画
     */
    protected boolean isAnimEnabled() {
        return true;
    }

    /**
     * 是否状态栏透明
     */
    protected boolean isStatusTranslucent() {
        return true;
    }

    /**
     * 是否可取消
     */
    protected boolean canCancelable() {
        return true;
    }

    /**
     * ...
     */
    protected boolean canCanceledOnOutside() {
        return true;
    }

    /**
     * 窗口外是否可点击
     */
    protected boolean canTouchOnOutside() {
        return false;
    }

}
