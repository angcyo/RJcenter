package com.angcyo.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.LinkedHashSet;

/**
 * Created by robi on 2016-07-27 20:17.
 */
public class BluetoothDiscover {

    private LinkedHashSet<IBluetoothDiscoverListener> mDiscoverListeners;

    private BluetoothDiscover() {
        mDiscoverListeners = new LinkedHashSet<>();
    }

    public static BluetoothDiscover instance() {
        return Holder.discover;
    }

    public void addDiscoverListener(IBluetoothDiscoverListener listener) {
        mDiscoverListeners.add(listener);
    }

    public void removeDiscoverListener(IBluetoothDiscoverListener listener) {
        mDiscoverListeners.remove(listener);
    }

    public void onDiscoverDevice(BluetoothDevice device) {
        for (IBluetoothDiscoverListener listener : mDiscoverListeners) {
            listener.onDeviceDiscover(device);
        }
    }

    public interface IBluetoothDiscoverListener {
        /**
         * 发现了蓝牙设备时的回调
         */
        void onDeviceDiscover(BluetoothDevice device);
    }

    static class Holder {
        static BluetoothDiscover discover = new BluetoothDiscover();
    }
}
