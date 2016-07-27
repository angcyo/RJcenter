package com.angcyo.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lib.common.RLog;

/**
 * 蓝牙相关广播接收
 * Created by robi on 2016-07-27 15:43.
 */
public class BluetoothReceiver extends BroadcastReceiver {
    //    public static Logger log = LoggerFactory.getLogger("bluetooth.receiver");
    RLog log = RLog.getLog("bluetooth.receiver");

    @Override
    public void onReceive(Context context, Intent intent) {
        log.info("收到广播:{}", intent.getAction());
    }
}
