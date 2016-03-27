package com.angcyo;

import android.app.Application;

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
    }
}
