package com.angcyo;

import android.app.Application;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.rsen.db.X;
import com.rsen.exception.RCrashHandler;

/**
 * Created by angcyo on 16-03-04-004.
 */
public class RApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RCrashHandler.init(this);
        X.Ext.init(this);
        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setLogLevel(LogLevel.NONE)
                .setStorage(HawkBuilder.newSqliteStorage(this))
                .setPassword("angcyo")
                .build();

    }
}
