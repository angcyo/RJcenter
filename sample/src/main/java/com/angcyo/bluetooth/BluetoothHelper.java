package com.angcyo.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

/**
 * Created by robi on 2016-07-27 11:00.
 */
public class BluetoothHelper {
    public static BluetoothAdapter getDefaultAdapter(Context context) {
        BluetoothAdapter adapter = null;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            adapter = BluetoothAdapter.getDefaultAdapter();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            adapter = bluetoothManager.getAdapter();
        }
        return adapter;
    }

    /**
     * 判断是否支持蓝牙
     */
    public static boolean isSupportBluetooth(BluetoothAdapter adapter) {
        if (adapter != null && !TextUtils.isEmpty(adapter.getAddress())) {
            return true;
        }
        return false;
    }
}
