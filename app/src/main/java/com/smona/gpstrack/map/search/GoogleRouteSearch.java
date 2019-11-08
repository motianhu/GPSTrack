package com.smona.gpstrack.map.search;

import android.app.Activity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class GoogleRouteSearch  {

    protected GoogleMap googleMap;
    private LatLng startPoint, endPoint;
    private Marker startMk, endMk;

    public void initSearch(Activity activity, int type, double targetLa, double targetLo) {
        endPoint = new LatLng(targetLa, targetLo);
        location(activity);
    }

    private void location(Activity activity) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback locationCallback =new LocationCallback(){
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult != null) {
                    startPoint = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                    searchPath();
                }
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void searchPath() {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=30.5702000000,104.0647600000&destination=22.5428600000,114.0595600000&language=zh-CN";

    }

}
