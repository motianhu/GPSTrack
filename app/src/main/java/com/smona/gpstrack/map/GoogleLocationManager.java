package com.smona.gpstrack.map;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.smona.gpstrack.map.listener.CommonLocationListener;

/**
 * 谷歌地图定位管理类
 */
public class GoogleLocationManager {

    private SparseArray<CommonLocationListener> googleLocationListenerArray = new SparseArray<>();

    private GoogleLocationManager() {
    }

    private static class LocationHolder {
        private static GoogleLocationManager locationParam = new GoogleLocationManager();
    }

    public static GoogleLocationManager getInstance() {
        return LocationHolder.locationParam;
    }

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    //curLocation
    private double[] location = new double[]{0d, 0d};

    public void init(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context.getApplicationContext());
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(10 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                Log.e("motianhu", "location: " + locationResult);
                if (locationResult != null) {
                    location[0] = locationResult.getLastLocation().getLatitude();
                    location[1] = locationResult.getLastLocation().getLongitude();
                    CommonLocationListener listener = null;
                    for (int i = 0; i < googleLocationListenerArray.size(); i++) {
                        listener = googleLocationListenerArray.valueAt(i);
                        listener.onLocation(googleLocationListenerArray.keyAt(i), location[0], location[1]);
                    }
                }
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    public double[] getLocation() {
        return location;
    }

    public void addLocationListerner(int type, CommonLocationListener listener) {
        removeListener(type);
        googleLocationListenerArray.put(type, listener);
    }

    public void removeListener(int type) {
        googleLocationListenerArray.remove(type);
    }

    public void clear() {
        googleLocationListenerArray.clear();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public void refreshLocation() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
}
