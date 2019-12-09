package com.smona.gpstrack.map;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.smona.gpstrack.map.listener.CommonLocationListener;

import java.util.ArrayList;
import java.util.List;

public class GaodeLocationManager {

    private List<CommonLocationListener> gaodeLocationListenerList = new ArrayList<>();

    private GaodeLocationManager() {
    }

    private static class LocationHolder {
        private static GaodeLocationManager locationParam = new GaodeLocationManager();
    }

    public static GaodeLocationManager getInstance() {
        return LocationHolder.locationParam;
    }

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    //curLocation
    private double[] location = new double[]{0d, 0d};

    public void init(Context context) {
        //初始化定位
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        //声明定位回调监听器
        mLocationListener = aMapLocation -> {
            Log.e("motianhu", "location: " + aMapLocation);
            location[0] = aMapLocation.getLatitude();
            location[1] = aMapLocation.getLongitude();
            for(CommonLocationListener listener: gaodeLocationListenerList) {
                listener.onLocation(location[0], location[1]);
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //启动定位
        mLocationClient.startLocation();
    }

    public double[] getLocation() {
        return location;
    }

    public void addLocationListerner(CommonLocationListener listener) {
        gaodeLocationListenerList.add(listener);
    }

    public void removeListener(CommonLocationListener listener) {
        for(CommonLocationListener locationListener: gaodeLocationListenerList) {
            if(listener.equals(locationListener)) {
                gaodeLocationListenerList.remove(listener);
                break;
            }
        }
    }

    public void clear() {
        gaodeLocationListenerList.clear();
    }

    public void refreshLocation() {
        mLocationClient.startLocation();
    }
}
