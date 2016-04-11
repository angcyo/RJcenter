package com.angcyo.accessibility;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.angcyo.bmob.table.DeviceRegister;
import com.rsen.util.DeviceUtil;
import com.rsen.util.MD5;
import com.rsen.util.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by angcyo on 2016-04-10 21:38.
 */
public class BmobUtil {

    public static final boolean DEBUG = true;
    public static final String TAG = "BmobUtil";

    /**
     * 保存注册码,用于注册
     */
    public static void saveRegisterCode(Context context, String code, SaveListener saveListener) {
        String md5 = MD5.toMD5(code);
        DeviceRegister deviceRegister = new DeviceRegister();
        deviceRegister.setCODE(md5);
        deviceRegister.setDebug(code.charAt(0) == 'd' || code.charAt(0) == 'D');
        deviceRegister.setDeviceModelName(DeviceUtil.getDeviceModelName());
        deviceRegister.setDeviceName(DeviceUtil.getDeviceName() + ":" + code);
        deviceRegister.setIMEI("");//如果有IMEI,说明已经使用了注册码
        deviceRegister.setRunCount(0L);//如果有IMEI,说明已经使用了注册码
        deviceRegister.setOsVer(Utils.getOsSdk());//系统版本

        isDeviceCodeExist(context, md5, new FindListener<DeviceRegister>() {
            @Override
            public void onSuccess(List<DeviceRegister> list) {
                if (list.size() > 0) {
                    saveListener.onFailure(1001, "注册码已经存在");
                } else {
                    deviceRegister.save(context, saveListener);
                }
            }

            @Override
            public void onError(int i, String s) {
                //如果注册码不存在
                deviceRegister.save(context, saveListener);
            }
        });
    }

    /**
     * 运行次数自增
     */
    public static void increment(Context context, String objId) {
        BmobQuery<DeviceRegister> deviceRegisterBmobQuery = new BmobQuery<>();
        deviceRegisterBmobQuery.getObject(context, objId, new GetListener<DeviceRegister>() {
            @Override
            public void onSuccess(DeviceRegister deviceRegister) {
                deviceRegister.increment("runCount");
                deviceRegister.update(context);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    public static void registerCode(Context context, String code, FindListener<DeviceCodeInfo> registerListener) {
        String md5 = MD5.toMD5(code);
        isDeviceCodeExist(context, md5, new FindListener<DeviceRegister>() {
            @Override
            public void onSuccess(List<DeviceRegister> list) {
                if (list.size() != 1) {
                    registerListener.onError(1001, "注册失败,请检查注册码是否正确.");
                } else {
                    DeviceRegister deviceRegister = list.get(0);
                    if (TextUtils.isEmpty(deviceRegister.getIMEI())) {
                        //如果IMEI为空,表示没有注册.保存设备IMEI信息,注册
                        String objectId = deviceRegister.getObjectId();
                        DeviceCodeInfo info = new DeviceCodeInfo();
                        info.isDebug = deviceRegister.isDebug;
                        info.objectId = objectId;
                        info.code = md5;

                        deviceRegister.setValue("IMEI", DeviceUtil.getIMEI(context) + ":" + code);
                        deviceRegister.setValue("deviceName", "by " + DeviceUtil.getDeviceName());
                        deviceRegister.update(context, objectId, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                ArrayList<DeviceCodeInfo> codeInfo = new ArrayList<>();
                                codeInfo.add(info);
                                registerListener.onSuccess(codeInfo);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                registerListener.onError(i, s);
                            }
                        });
                    } else {
                        registerListener.onError(1001, "注册码已被注册.");
                    }

                }
            }

            @Override
            public void onError(int i, String s) {
                //如果注册码不存在
                registerListener.onError(1001, "注册失败,请检查注册码是否正确.");
            }
        });
    }

    /**
     * 判断注册码是否存在
     */
    public static void isDeviceCodeExist(Context context, String code, FindListener<DeviceRegister> findListener) {
        BmobQuery<DeviceRegister> deviceRegisterBmobQuery = new BmobQuery<>();
        deviceRegisterBmobQuery.addWhereEqualTo("CODE", code);
        deviceRegisterBmobQuery.findObjects(context, findListener);
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
        public String objectId;//
        public String code;//
    }
}
