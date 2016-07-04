package com.angcyo.sample.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.rsen.github.common.L;

public class ServiceTest extends Service {
    public ServiceTest() {
        L.i("ServiceTest: ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        L.i("onBind: ");
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        L.i("onCreate: ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.i("onStartCommand: ");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        L.i("onDestroy: ");
        super.onDestroy();
    }
}
