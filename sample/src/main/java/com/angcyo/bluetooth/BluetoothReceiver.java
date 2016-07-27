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
//    RLog log = RLog.getLog("bluetooth.receiver");


    //扫描蓝牙设备时的广播顺序
    /*收到广播:android.bluetooth.device.action.NAME_CHANGED
    收到广播:android.bluetooth.device.action.CLASS_CHANGED
    收到广播:android.bluetooth.device.action.FOUND*/
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        log.debug("收到广播:{}", action);

        if (TextUtils.equals(BluetoothDevice.ACTION_FOUND, action)) {
            actionFound(intent);
        }
    }

    private void actionFound(Intent intent) {
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        log.info("蓝牙名称:{} 地址:{} 类型:{}", bluetoothDevice.getName(), bluetoothDevice.getAddress(), bluetoothDevice.getBluetoothClass().getDeviceClass());
        BluetoothDiscover.instance().onDevicoverDevice(bluetoothDevice);
    }
}
