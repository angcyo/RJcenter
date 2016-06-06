package com.rsen.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

/**
 * Created by dengjun on 2016/1/21.
 * Description :
 */
public class VersionTools {
    public static final String FILE_NAME = "file://";

    public static final String APPLICATION_NAME = "application/vnd.android.package-archive";

    public static String getAppVersion(Context context){
        PackageInfo packageInfo = null;
        String versionName = "";
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static int getAppVersionCode(Context context){
        PackageInfo packageInfo = null;
        int versionName = 0;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionName = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }


    public static void installApp(Context context, File file) {
        Intent intent = new Intent("android.intent.action.VIEW.HIDE");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse(FILE_NAME + file.toString()), APPLICATION_NAME);
        context.startActivity(intent);
    }
}
