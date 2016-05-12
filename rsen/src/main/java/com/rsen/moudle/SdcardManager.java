package com.rsen.moudle;

import java.util.LinkedHashSet;

/**
 * Created by robi on 2016-05-11 14:52.
 */
public class SdcardManager {
    private static SdcardManager sManager;
    private LinkedHashSet<ISdcardListener> mSdcardListeners;
    private long lastTime = 0l;
    private boolean lastState = false;

    public SdcardManager() {
        mSdcardListeners = new LinkedHashSet<>();
    }

    public static SdcardManager instance() {
        if (sManager == null) {
            synchronized (SdcardManager.class) {
                if (sManager == null) {
                    sManager = new SdcardManager();
                }
            }
        }
        return sManager;
    }

    public void addListener(ISdcardListener listener) {
        mSdcardListeners.add(listener);
    }

    public void removeListener(ISdcardListener listener) {
        mSdcardListeners.remove(listener);
    }

    public void notifyEvent(boolean mounted) {
        long nowTime = System.currentTimeMillis();
//        Log.e("TF卡通知:", nowTime + " " + mounted);
        if (lastTime == 0l) {
            lastTime = nowTime;
            lastState = mounted;
        } else {
            if ((nowTime - lastTime) < 500 && lastState == mounted) {
                lastTime = nowTime;
                lastState = mounted;
                return;
            }
        }

        if (mounted) {
            for (ISdcardListener listener : mSdcardListeners) {
                if (listener != null) {
                    listener.onMounted();
                }
            }
        } else {
            for (ISdcardListener listener : mSdcardListeners) {
                if (listener != null) {
                    listener.onRemoved();
                }
            }
        }
    }
}
