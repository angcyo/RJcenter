package com.rsen.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Created by robi on 2016-06-02 20:47.
 */
public class MainThreadExecutor {
    static private MainThreadExecutor instance;
    private final Executor mCallbackPoster;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private MainThreadExecutor() {
        mCallbackPoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    public static MainThreadExecutor instance() {
        return instance == null ? instance = new MainThreadExecutor() : instance;
    }

    public void onMain(Runnable runnable) {
        mCallbackPoster.execute(runnable);
    }

}
