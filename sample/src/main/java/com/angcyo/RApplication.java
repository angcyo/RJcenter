package com.angcyo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.rsen.db.XUtil;
import com.rsen.exception.RCrashHandler;
import com.rsen.realm.RRealm;

/**
 * Created by angcyo on 16-03-04-004.
 */
public class RApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RCrashHandler.init(this);
        XUtil.Ext.init(this);
        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setLogLevel(LogLevel.NONE)
                .setStorage(HawkBuilder.newSqliteStorage(this))
                .setPassword("angcyo")
                .build();

        RRealm.init(this, "rjcenter.realm", true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
