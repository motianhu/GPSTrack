package com.smona.gpstrack.map;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.smona.gpstrack.map.listener.CommonLocationListener;

import java.util.ArrayList;
import java.util.List;

public class GoogleLocationManager {

    private List<CommonLocationListener> googleLocationListenerList = new ArrayList<>();

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
                    for(CommonLocationListener listener: googleLocationListenerList) {
                        listener.onLocation(location[0], location[1]);
                    }
                }
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    public double[] getLocation() {
        return location;
    }

    public void addLocationListerner(CommonLocationListener listener) {
        googleLocationListenerList.add(listener);
    }

    public void removeListener(CommonLocationListener listener) {
        for(CommonLocationListener locationListener: googleLocationListenerList) {
            if(listener.equals(locationListener)) {
                googleLocationListenerList.remove(listener);
                break;
            }
        }
    }

    public void clear() {
        googleLocationListenerList.clear();
    }

    public void refreshLocation() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
}
