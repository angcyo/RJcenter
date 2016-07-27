package com.angcyo.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;

public class BluetoothDemoActivity extends RBaseActivity {

    BluetoothAdapter defaultAdapter;
    String msg;

    @Override
    protected int getContentView() {
        return R.layout.activity_bluetooth_demo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initViewData() {
        defaultAdapter = BluetoothHelper.getDefaultAdapter(this);
    }

    public void supportBluetooth(View view) {
        if (defaultAdapter != null) {
            msg = "支持蓝牙";
        } else {
            msg = "不支持蓝牙";
        }
        showMsg(view);
    }

    public void enableBluetooth(View view) {
        if (defaultAdapter.isEnabled()) {
            msg = "蓝牙已打开";
        } else {
            msg = "蓝牙已关闭";
        }
        showMsg(view);
    }

    public void openBluetooth(View view) {
        if (defaultAdapter.isEnabled()) {
            msg = "断开蓝牙";
            defaultAdapter.disable();
        } else {
            msg = "打开蓝牙";
            defaultAdapter.enable();
        }
        showMsg(view);
    }

    public void showMsg(View view) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }
}
