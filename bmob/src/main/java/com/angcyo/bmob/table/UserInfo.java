package com.angcyo.bmob.table;

import cn.bmob.v3.BmobObject;

/**
 * Created by robi on 2016-04-13 17:23.
 */
public class UserInfo extends BmobObject {
    //名称
    private String userName;
    //注册码
    private String userCode;
    //手机号码
    private String telNum;

    private String serialNumber;

    public String getSimSerialNumber() {
        return simSerialNumber;
    }

    public void setSimSerialNumber(String simSerialNumber) {
        this.simSerialNumber = simSerialNumber;
    }

    private String simSerialNumber;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }
}
