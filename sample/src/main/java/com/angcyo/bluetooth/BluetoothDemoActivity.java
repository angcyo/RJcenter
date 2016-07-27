package com.angcyo.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.angcyo.sample.R;
import com.rsen.base.RBaseActivity;
import com.rsen.base.RBaseAdapter;
import com.rsen.base.RBaseViewHolder;

import java.util.Set;

public class BluetoothDemoActivity extends RBaseActivity {

    BluetoothAdapter defaultAdapter;
    String msg;
    long scanMode = 0;

    BluetoothDeviceAdapter leftAdapter;
    BluetoothDeviceAdapter rightAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_bluetooth_demo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        leftAdapter = new BluetoothDeviceAdapter(this);
        rightAdapter = new BluetoothDeviceAdapter(this);
        mViewHolder.r(R.id.leftRecyclerView).setAdapter(leftAdapter);
        mViewHolder.r(R.id.rightRecyclerView).setAdapter(rightAdapter);
    }

    @Override
    protected void initViewData() {
        defaultAdapter = BluetoothHelper.getDefaultAdapter(this);

        //已经匹配过的设备
        final Set<BluetoothDevice> bondedDevices = defaultAdapter.getBondedDevices();
        for (BluetoothDevice device : bondedDevices) {
            rightAdapter.addLastItem(new BluetoothDeviceBean(device.getName(), device.getAddress()));
        }
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

            //使用系统交互的方式打开蓝牙
            //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivity(enableBtIntent);
        }
        showMsg(view);
    }

    public void addressBluetooth(View view) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(defaultAdapter.getName());//蓝牙名称
        stringBuilder.append(" ");
        stringBuilder.append(defaultAdapter.getAddress());//蓝牙地址
        stringBuilder.append("\nMode:");
        stringBuilder.append(defaultAdapter.getScanMode());//扫描模式
        stringBuilder.append(" State:");
        stringBuilder.append(defaultAdapter.getState());//当前状态
        stringBuilder.append(" ");
        stringBuilder.append(defaultAdapter.isDiscovering());//是否正在扫描
        msg = stringBuilder.toString();
        showMsg(view);
    }

    public void startDiscovery(View view) {
        defaultAdapter.startDiscovery();
        //defaultAdapter.cancelDiscovery();
        msg = "开始扫描...";
        showMsg(view);
    }

    public void openBluetoothSettingActivity(View view) {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intent);
    }

    public void setScanMode(View view) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);//可被发现的持续时间
        startActivity(intent);

        scanMode++;
    }

    public void showMsg(View view) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    static class BluetoothDeviceBean {
        public String name;
        public String address;

        public BluetoothDeviceBean(String name, String address) {
            this.name = name;
            this.address = address;
        }
    }

    class BluetoothDeviceAdapter extends RBaseAdapter<BluetoothDeviceBean> {

        public BluetoothDeviceAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutId(int viewType) {
            return R.layout.activity_bluetooth_adapter_item;
        }

        @Override
        protected void onBindView(RBaseViewHolder holder, int position, BluetoothDeviceBean bean) {
            holder.tV(R.id.nameView).setText(bean.name);
            holder.tV(R.id.addressView).setText(bean.address);
        }
    }
}
