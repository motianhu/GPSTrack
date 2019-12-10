package com.smona.gpstrack.map;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.smona.gpstrack.map.listener.CommonLocationListener;
import com.smona.gpstrack.util.ToastUtil;

public class GaodeLocationManager {

    private SparseArray<CommonLocationListener> gaodeLocationListenerArray = new SparseArray<>();

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
        mLocationOption.setInterval(10 * 1000);
        mLocationClient.setLocationOption(mLocationOption);
        //声明定位回调监听器
        mLocationListener = aMapLocation -> {
            Log.e("motianhu", "location: " + aMapLocation);
            if (aMapLocation.getErrorCode() != AMapLocation.LOCATION_SUCCESS) {
                ToastUtil.showShort(aMapLocation.getLocationDetail());
                return;
            }
            location[0] = aMapLocation.getLatitude();
            location[1] = aMapLocation.getLongitude();
            CommonLocationListener listener = null;
            for (int i = 0; i < gaodeLocationListenerArray.size(); i++) {
                listener = gaodeLocationListenerArray.valueAt(i);
                listener.onLocation(gaodeLocationListenerArray.keyAt(i), location[0], location[1]);
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

    public void addLocationListerner(int type, CommonLocationListener listener) {
        removeListener(type);
        gaodeLocationListenerArray.put(type, listener);
    }

    public void removeListener(int type) {
        gaodeLocationListenerArray.remove(type);
    }

    public void clear() {
        gaodeLocationListenerArray.clear();
    }

    public void refreshLocation() {
        mLocationClient.startLocation();
    }
}
