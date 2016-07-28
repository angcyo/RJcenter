package com.angcyo.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 蓝牙相关广播接收
 * Created by robi on 2016-07-27 15:43.
 */
public class BluetoothReceiver extends BroadcastReceiver {
    public static Logger log = LoggerFactory.getLogger("bluetooth.receiver");

    public static BluetoothDevice getDeviceFromIntent(Intent intent) {
        return intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
    }

    public static int getBondStateFromIntent(Intent intent) {
        return intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
    }

    public static String getBondStateString(int bondState) {
        String result = "";
        switch (bondState) {
            case BluetoothDevice.BOND_NONE:
                result = "BOND_NONE";
                break;
            case BluetoothDevice.BOND_BONDING:
                result = "BOND_BONDING";
                break;
            case BluetoothDevice.BOND_BONDED:
                result = "BOND_BONDED";
                break;
        }
        return result;
    }

    public static String getTypeString(int type) {
        String result = "DEVICE_TYPE_UNKNOWN";
        switch (type) {
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                result = "DEVICE_TYPE_CLASSIC";
                break;
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                result = "DEVICE_TYPE_DUAL";
                break;
            case BluetoothDevice.DEVICE_TYPE_LE:
                result = "DEVICE_TYPE_LE";
                break;
        }
        return result;
    }

    //扫描蓝牙设备时的广播顺序
    /*收到广播:android.bluetooth.device.action.NAME_CHANGED
    收到广播:android.bluetooth.device.action.CLASS_CHANGED
    收到广播:android.bluetooth.device.action.FOUND*/
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        log.debug("收到广播:{}", action);
        BluetoothDevice bluetoothDevice = getDeviceFromIntent(intent);
        if (TextUtils.equals(BluetoothDevice.ACTION_ACL_CONNECTED, action)) {
            showDeviceInfo(bluetoothDevice, "已连接");
        } else if (TextUtils.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED, action)) {
            showDeviceInfo(bluetoothDevice, "已断开");
        } else if (TextUtils.equals(BluetoothDevice.ACTION_FOUND, action)) {
            actionFound(intent);
        } else if (TextUtils.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED, action)) {
            bondStateChanged(intent);
        }
    }

    /**
     * 处理扫描发现的蓝牙设备
     */
    private void actionFound(Intent intent) {
        BluetoothDevice bluetoothDevice = getDeviceFromIntent(intent);
        log.info("蓝牙名称:{} 地址:{} 设备归类:{} 设备类型:{} 配对状态:{}",
                bluetoothDevice.getName(), bluetoothDevice.getAddress(),
                bluetoothDevice.getBluetoothClass().getDeviceClass(), getTypeString(bluetoothDevice.getType()),
                getBondStateString(bluetoothDevice.getBondState()));
        BluetoothDiscover.instance().onDiscoverDevice(bluetoothDevice);
    }

    /**
     * 处理配对状态变化
     */
    private void bondStateChanged(Intent intent) {
        BluetoothDevice bluetoothDevice = getDeviceFromIntent(intent);
        log.info("设备:{} 配对状态:{}", bluetoothDevice.getName(), getBondStateString(getBondStateFromIntent(intent)));
    }

    private void showDeviceInfo(BluetoothDevice device, String msg) {
        log.info("蓝牙名称:{} 配对状态:{} 设备类型:{} {}",
                device.getName(), getBondStateString(device.getBondState()), getTypeString(device.getType()), msg);
    }

}
