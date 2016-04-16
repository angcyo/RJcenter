package com.angcyo.bmob.table;

import cn.bmob.v3.BmobObject;

/**
 * Created by angcyo on 2016-04-16 10:47.
 */
public class LocationInfo extends BmobObject {
    private double latitude;
    private double longitude;
    private String name;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
