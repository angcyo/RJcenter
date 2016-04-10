package com.angcyo.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;

import com.angcyo.bmob.BmobHelper;
import com.angcyo.sample.R;
import com.orhanobut.hawk.Hawk;
import com.rsen.base.RBaseActivity;
import com.rsen.util.T;

import java.util.List;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RAccessibilityActivity extends RBaseActivity implements AccessibilityManager.AccessibilityStateChangeListener {

    private AccessibilityManager accessibilityManager;
    public static final String KEY_OBJID = "objid";//保存注册码在bmob上记录的id
    public static final String KEY_CODE = "code";//保存注册码在bmob上记录的id
    public static final String KEY_DEBUG = "debug";//是否是debug的key

    @Override
    protected int getContentView() {
        return R.layout.activity_raccessibility;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        CrashUtil.init();

        BmobHelper.initBmob(this);

        mToolbar.setTitle(getString(R.string.name_rsen_weixin));
        mToolbar.setLayoutParams(new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));


        //监听AccessibilityService 变化
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(this);

        mViewHolder.v("enable").setOnClickListener(v -> {
            onEnableClick();
        });

        mViewHolder.v("enable").setOnLongClickListener(v -> {
            cleanCodeInfo();
            updateButton();
            return true;
        });

        mViewHolder.v("register").setOnClickListener(v -> {
            onRegisterClick();
        });

        updateServiceStatus();

        BmobUtil.increment(this, Hawk.get(KEY_OBJID));
    }

    /**
     * 单击按钮之后
     */
    public void onEnableClick() {
        CharSequence code = mViewHolder.tV(R.id.codeEdit).getText();

        if (TextUtils.isEmpty(code)) {
            gotoSetting();
        } else {
            //添加注册码,如果是D开头的注册码,那么就是Debug Key
            String[] strings = code.toString().split(":");
            if (strings.length == 2 && strings[0].equalsIgnoreCase("add") && !TextUtils.isEmpty(strings[1])) {
                showDialogTip("请稍等...");
                BmobUtil.saveRegisterCode(this, strings[1], new SaveListener() {
                    @Override
                    public void onSuccess() {
                        hideDialogTip();
                        showToast("注册码:" + strings[1] + " 添加成功.");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        hideDialogTip();
                        showToast("失败:" + s);
                    }
                });
            } else {
                gotoSetting();
            }
        }
    }

    private void gotoSetting() {
        try {
            Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(accessibleIntent);
        } catch (Exception e) {
            T.show(RAccessibilityActivity.this, "遇到一些问题,请手动打开系统设置>辅助服务>Rsen微信助手");
            e.printStackTrace();
        }
    }

    public void onRegisterClick() {
        CharSequence code = mViewHolder.tV(R.id.codeEdit).getText();

        if (TextUtils.isEmpty(code)) {
            mViewHolder.eV(R.id.codeEdit).setError("请输入有效的注册码!");
        } else {
            showDialogTip("正在验证...");
            BmobUtil.registerCode(this, code.toString(), new FindListener<BmobUtil.DeviceCodeInfo>() {
                @Override
                public void onSuccess(List<BmobUtil.DeviceCodeInfo> list) {
                    hideDialogTip();
                    showToast("恭喜注册成功");
                    Hawk.put(KEY_OBJID, list.get(0).objectId);
                    Hawk.put(KEY_CODE, list.get(0).code);
                    Hawk.put(KEY_DEBUG, list.get(0).isDebug);

                    updateButton();
                }

                @Override
                public void onError(int i, String s) {
                    hideDialogTip();
                    mViewHolder.eV(R.id.codeEdit).setError(s);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateButton();
    }

    private void updateButton() {
        String code = Hawk.get(KEY_CODE, "");

        if (TextUtils.isEmpty(code)) {
            if (!mViewHolder.tV(R.id.codeEdit).isEnabled()) {
                mViewHolder.tV(R.id.codeEdit).setText(code);
            }
            mViewHolder.tV(R.id.codeEdit).setEnabled(true);
            mViewHolder.tV(R.id.register).setText("立即注册");
            mViewHolder.v(R.id.register).setEnabled(true);
        } else {
            String debug = "(只能绑定一台设备)";
            if (Hawk.get(KEY_DEBUG, true)) {
                debug = "(测试注册码只能使用一次)";
            }
            mViewHolder.tV(R.id.codeEdit).setText(code);
            mViewHolder.tV(R.id.codeEdit).setEnabled(false);
            mViewHolder.tV(R.id.register).setText(debug);
            mViewHolder.v(R.id.register).setEnabled(false);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    /**
     * 更新当前 HongbaoService 显示状态
     */
    private void updateServiceStatus() {
        if (isServiceEnabled()) {
            mViewHolder.tV("enable").setText("插件已开启, 前往-->关闭");
        } else {
            mViewHolder.tV("enable").setText("插件已关闭, 前往-->开启");
        }
    }

    /**
     * 获取 Service 是否启用状态
     *
     * @return
     */
    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().contains("com.angcyo.accessibility.RsenAccessibilityService")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否 注册了
     */
    public static boolean isDeviceRegister() {
        String code = Hawk.get(KEY_CODE, "");
        if (TextUtils.isEmpty(code)) {
            return false;
        }

        return true;
    }

    /**
     * 是否 是Debug Key
     */
    public static boolean isDebugKey() {
        return Hawk.get(KEY_DEBUG, true);
    }

    public static void cleanCodeInfo() {
        Hawk.put(KEY_OBJID, "");
        Hawk.put(KEY_CODE, "");
        Hawk.put(KEY_DEBUG, true);
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        updateServiceStatus();
    }
}
