package com.rsen.angcyo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 电话广播
 * <p>
 * http://www.cnblogs.com/zyw-205520/archive/2013/01/25/2876844.html
 * <p>
 * Created by robi on 2016-07-19 20:33.
 */
public class PhoneBroadcastReceiver extends BroadcastReceiver {
    public static Logger log = LoggerFactory.getLogger("voip");
    private static boolean mIncomingFlag = false;
    private static String mIncomingNumber = null;

    /*
    <!--打电话广播-->
    <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
    <!--监听电话状态-->
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    */

    /*
    <receiver android:name=".service.PhoneBroadcastReceiver">
    <intent-filter android:priority="1000">
    <action android:name="android.intent.action.NEW_OUTGOING_CALL"/>
    <action android:name="android.intent.action.PHONE_STATE"/>
    </intent-filter>
    </receiver>
    */

    @Override
    public void onReceive(Context context, Intent intent) {
        log.info("收到电话广播:{}", intent == null ? "null" : intent.getAction());

        // 如果是拨打电话
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            //拨打电话会优先,收到此广播. 再收到 android.intent.action.PHONE_STATE 的 TelephonyManager.CALL_STATE_OFFHOOK 状态广播;
            mIncomingFlag = false;
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            log.info("call OUT:{}", phoneNumber);

        } else {
            // 如果是来电
            TelephonyManager tManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);

            log.info("电话状态:{}", tManager.getCallState());

            switch (tManager.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    //等待接听状态
                    mIncomingNumber = intent.getStringExtra("incoming_number");
                    log.info("RINGING :" + mIncomingNumber);

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //接听状态
                    if (mIncomingFlag) {
                        log.info("incoming ACCEPT :" + mIncomingNumber);
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //挂断状态
                    if (mIncomingFlag) {
                        log.info("incoming IDLE");
                    }
                    break;
            }
        }
    }
}
