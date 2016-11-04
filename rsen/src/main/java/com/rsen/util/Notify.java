package com.rsen.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：通知
 * 创建人员：Robi
 * 创建时间：2016/11/04 9:47
 * 修改人员：Robi
 * 修改时间：2016/11/04 9:47
 * 修改备注：
 * Version: 1.0.0
 */
public class Notify {
    public static Notification show(Context context, String title, String content, String ticker, @DrawableRes int icon, Intent pendingIntent) {

        //如果描述的PendingIntent已经存在，则在产生新的Intent之前会先取消掉当前的
        PendingIntent hangPendingIntent = PendingIntent.getActivity(context, 0, pendingIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(icon)
                .setColor(Color.parseColor("#FD596B"))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), icon))
                .setAutoCancel(true)
                .setContentIntent(hangPendingIntent)
                .setFullScreenIntent(hangPendingIntent, true)
                .setVisibility(Notification.VISIBILITY_PRIVATE)
                .setTicker(ticker)
                .setWhen(System.currentTimeMillis())
                .build();

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify("notify", 10099, notification);

        return notification;

//        PendingIntent.getActivity(context, 0,,0,)
    }
}
