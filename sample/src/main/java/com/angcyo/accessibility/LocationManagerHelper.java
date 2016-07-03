package com.angcyo.accessibility;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.angcyo.bmob.table.LocationInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by angcyo on 2016-04-16 10:03.
 */
public class LocationManagerHelper implements LocationListener {
    //高德坐标拾取器 http://lbs.amap.com/console/show/picker
    public static final String PROVIDER = LocationManager.GPS_PROVIDER;
    public static LocationManagerHelper locationManagerHelper;
    public static LocationManager locationManager;
    private List<LocationInfo> locationInfos;
    private static int index = 0;
    private Context context;

    private LocationManagerHelper(Context context) {
        locationInfos = new ArrayList<>();
        index = 0;
        this.context = context;
    }

    public void initLocation() {
        if (locationManager != null) {
            return;
        }
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.addTestProvider(PROVIDER, false, true, false, false, false, false, false, 0, 5);
//        locationManager.addTestProvider(PROVIDER, false, true, false, false, true, true, true, 0, 5);
        locationManager.setTestProviderEnabled(PROVIDER, true);
//        locationManager.requestLocationUpdates(PROVIDER, 0l, 0f, this);
    }

    public void initLocationInfos() {
        BmobUtil.getLocationInfos(locationInfos);
    }

    public static LocationManagerHelper getInstance(Context context) {
        if (locationManagerHelper == null) {
            synchronized (LocationManagerHelper.class) {
                if (locationManagerHelper == null) {
                    locationManagerHelper = new LocationManagerHelper(context);
                }
            }
        }
        return locationManagerHelper;
    }

    public void setLocation() {
        if (locationInfos.size() > 0) {
            LocationInfo info = locationInfos.get(index++ % locationInfos.size());
            Log.e("angcyo-->", "模拟地址:" + info.getName());
            locationManager.setTestProviderLocation(PROVIDER, makeLocation(info.getLongitude(), info.getLatitude()));
        }
    }

    //通过经纬度,返回一个地址对象
    private static Location makeLocation(double longitude, double latitude) {
        Location location = new Location(PROVIDER);
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        location.setTime(System.currentTimeMillis());
        location.setAltitude(2.0f);//海拔高度
        location.setAccuracy(3.0f);//精确度
        location.setBearing(180);
        location.setSpeed(10f);
        location.setTime(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= 17) {
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }

        try {
            Method makeComplete = Location.class.getMethod("makeComplete");
            if (makeComplete != null) {
                makeComplete.invoke(location);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
