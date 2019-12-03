package com.smona.google;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class GoogleLocationManager {

    private GoogleLocationListener googleLocationListener;

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
        locationRequest.setInterval(24 * 60 * 60 * 1000);
        locationRequest.setFastestInterval(24 * 60 * 60 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                Log.e("motianhu", "location: " + locationResult);
                if (locationResult != null) {
                    location[0] = locationResult.getLastLocation().getLatitude();
                    location[1] = locationResult.getLastLocation().getLongitude();
                    if (googleLocationListener != null) {
                        googleLocationListener.onLocation(new LatLng(location[0], location[1]));
                        googleLocationListener = null;
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

    public void refreshLocation(GoogleLocationListener listener) {
        googleLocationListener = listener;
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
}
