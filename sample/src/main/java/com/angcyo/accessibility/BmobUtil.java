package com.angcyo.accessibility;

import android.content.Context;
import android.util.Log;

import com.angcyo.bmob.table.DeviceRegister;
import com.rsen.util.MD5;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by angcyo on 2016-04-10 21:38.
 */
public class BmobUtil {

    public static final boolean DEBUG = true;
    public static final String TAG = "BmobUtil";

    /**
     * 保存注册码,用于注册
     */
    public static void saveRegisterCode(String code) {
        DeviceRegister deviceRegister = new DeviceRegister();
        deviceRegister.setCODE(MD5.toMD5(code));
    }

    /**
     * 判断注册码是否存在
     */
    public static DeviceCodeInfo isDeviceCodeExist(Context context, String code) {
        BmobQuery<DeviceRegister> deviceRegisterBmobQuery = new BmobQuery<>();
        deviceRegisterBmobQuery.addWhereEqualTo("CODE", code);
        deviceRegisterBmobQuery.findObjects(context, new FindListener<DeviceRegister>() {
            @Override
            public void onSuccess(List<DeviceRegister> list) {
                e(list.size() + "");
            }

            @Override
            public void onError(int i, String s) {
                e(s + "");
            }
        });

        e(" -- >");

        return null;
    }

    public static void e(String msg) {
        if (DEBUG) {
            Log.e(TAG + " " + Thread.currentThread().getId(), "" + msg);
        }
    }

    static class DeviceCodeInfo {
        public boolean isCodeExist;//注册码是否存在
        public boolean isCodeRegister;//注册码是否已使用
        public boolean isDebug;//是否是debug Key
    }
}
