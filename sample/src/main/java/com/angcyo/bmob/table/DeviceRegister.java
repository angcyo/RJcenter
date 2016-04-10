package com.angcyo.bmob.table;

import cn.bmob.v3.BmobObject;

/**
 * Created by angcyo on 2016-04-10 21:18.
 */
public class DeviceRegister extends BmobObject {
    public String IMEI;//设备唯一标识符,一个注册码只能绑定一台设备
    public String CODE;//注册码
    public Boolean isDebug;//debug模式下,注册的设备只能使用一次
    public String deviceName;//设备名
    public String deviceModelName;//设备制作商名
    public Long runCount;//运行的次数
    public String osVer;//系统的版本

    public String getOsVer() {
        return osVer;
    }

    public void setOsVer(String osVer) {
        this.osVer = osVer;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public Boolean getDebug() {
        return isDebug;
    }

    public void setDebug(Boolean debug) {
        isDebug = debug;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceModelName() {
        return deviceModelName;
    }

    public void setDeviceModelName(String deviceModelName) {
        this.deviceModelName = deviceModelName;
    }

    public Long getRunCount() {
        return runCount;
    }

    public void setRunCount(Long runCount) {
        this.runCount = runCount;
    }
}
