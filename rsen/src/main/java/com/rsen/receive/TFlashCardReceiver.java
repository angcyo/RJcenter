package com.rsen.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rsen.moudle.SdcardManager;

public class TFlashCardReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
            SdcardManager.instance().notifyEvent(true);
        } else if (action.equals(Intent.ACTION_MEDIA_REMOVED)) {
            SdcardManager.instance().notifyEvent(false);
        } else if (action.equalsIgnoreCase(Intent.ACTION_MEDIA_EJECT)) {
            SdcardManager.instance().notifyEvent(false);
        }
    }
}
