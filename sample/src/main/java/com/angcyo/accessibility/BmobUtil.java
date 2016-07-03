package com.angcyo.accessibility;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.angcyo.bmob.table.DeviceRegister;
import com.angcyo.bmob.table.LocationInfo;
import com.angcyo.bmob.table.UserInfo;
import com.orhanobut.hawk.Hawk;
import com.rsen.util.DeviceUtil;
import com.rsen.util.MD5;
import com.rsen.util.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by angcyo on 2016-04-10 21:38.
 */
public class BmobUtil {

    public static final boolean DEBUG = true;
    public static final String TAG = "BmobUtil";

    /**
     * 获取所有位置信息
     */
    public static void getLocationInfos(List<LocationInfo> locationInfos) {
        BmobQuery<LocationInfo> locationInfoBmobQuery = new BmobQuery<>();
        locationInfoBmobQuery.findObjects(new FindListener<LocationInfo>() {
            @Override
            public void done(List<LocationInfo> list, BmobException e) {
                if (e != null) {
                    locationInfos.addAll(list);
                }
            }
        });
    }

    /**
     * 保存用户信息到后台
     */
    public static void saveUserInfo(Context context, String userName) {
        Observable.just(userName).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(s -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setSimSerialNumber(userName);
            userInfo.setTelNum(DeviceUtil.getSimSerialNumber(context));
            userInfo.setTelNum(DeviceUtil.getTelNumber(context));
            userInfo.setSerialNumber(DeviceUtil.getSerialNumber1());
            userInfo.setUserCode(Hawk.get(RAccessibilityActivity.KEY_CODE_RAW, ""));
            userInfo.save();
        });
    }

    /**
     * 保存注册码,用于注册
     */
    public static void saveRegisterCode(String code, SaveListener<String> saveListener) {
        String md5 = MD5.toMD5(code);
        DeviceRegister deviceRegister = new DeviceRegister();
        deviceRegister.setCODE(md5);
        deviceRegister.setDebug(code.charAt(0) == 'd' || code.charAt(0) == 'D');
        deviceRegister.setDeviceModelName(DeviceUtil.getDeviceModelName());
        deviceRegister.setDeviceName(DeviceUtil.getDeviceName() + ":" + code);
        deviceRegister.setIMEI("");//如果有IMEI,说明已经使用了注册码
        deviceRegister.setRunCount(0L);//如果有IMEI,说明已经使用了注册码
        deviceRegister.setOsVer(Utils.getOsSdk());//系统版本

        isDeviceCodeExist(md5, new FindListener<DeviceRegister>() {
            @Override
            public void done(List<DeviceRegister> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        saveListener.done(null, new BmobException("注册码已经存在"));
                    } else {
                        deviceRegister.save();
                    }
                } else {
                    //如果注册码不存在
                    deviceRegister.save(saveListener);
                }
            }
        });
    }

    /**
     * 运行次数自增
     */
    public static void increment(String objId) {
        BmobQuery<DeviceRegister> deviceRegisterBmobQuery = new BmobQuery<>();
        deviceRegisterBmobQuery.getObject(objId, new QueryListener<DeviceRegister>() {
            @Override
            public void done(DeviceRegister deviceRegister, BmobException e) {
                if (e != null) {
                    deviceRegister.increment("runCount");
                    deviceRegister.update();
                }
            }
        });
    }

    public static void registerCode(Context context, String code, FindListener<DeviceCodeInfo> registerListener) {
        String md5 = MD5.toMD5(code);
        isDeviceCodeExist(md5, new FindListener<DeviceRegister>() {
                    @Override
                    public void done(List<DeviceRegister> list, BmobException e) {
                        if (e == null) {
                            if (list.size() != 1) {
                                registerListener.done(null, new BmobException("注册码已被注册."));
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
                                    deviceRegister.update(objectId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                ArrayList<DeviceCodeInfo> codeInfo = new ArrayList<>();
                                                codeInfo.add(info);
                                                registerListener.done(codeInfo, null);
                                            } else {
                                                registerListener.done(null, e);
                                            }
                                        }
                                    });
                                }
                            }
                        } else {
                            //如果注册码不存在
                            registerListener.done(null, new BmobException("注册失败,请检查注册码是否正确."));
                        }
                    }
                }

        );
    }

    /**
     * 判断注册码是否存在
     */

    public static void isDeviceCodeExist(String code, FindListener<DeviceRegister> findListener) {
        BmobQuery<DeviceRegister> deviceRegisterBmobQuery = new BmobQuery<>();
        deviceRegisterBmobQuery.addWhereEqualTo("CODE", code);
        deviceRegisterBmobQuery.findObjects(findListener);
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
