package com.smona.gpstrack.map.search;

import android.app.Activity;
import android.graphics.Color;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.smona.gpstrack.common.GpsFixedBuilder;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;
import com.smona.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoogleRouteSearch {

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
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback locationCallback = new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    startPoint = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                    drawStartEndMarker();
                    searchPath();
                }
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void searchPath() {
        String url = getDirectionsUrl(new LatLng(startPoint.latitude, startPoint.longitude), new LatLng(endPoint.latitude, endPoint.longitude));
        Logger.d("motianhu", "url: " + url);
//        String url = getDirectionsUrl(new LatLng(39.99709957757345, 116.31184045225382), new LatLng(39.949158391497214, 116.4154639095068));

        OnResultListener<String> listener = new OnResultListener<String>() {
            @Override
            public void onSuccess(String s) {
                Logger.e("motianhu", "s: " + s);
                drawRoutePath(s);
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                Logger.e("motianhu", "stateCode: " + stateCode);
            }
        };
        HttpCallbackProxy<String> proxy = new HttpCallbackProxy<String>(listener) {
        };
        new GpsFixedBuilder<String>(GpsFixedBuilder.REQUEST_CUSTOM, url).requestData(proxy);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String key = "AIzaSyDEVm40DAlxCr0vx3RW-cohE3TuFg3y2B4";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&key=" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        return "/maps/api/directions/" + output + "?" + parameters;
    }

    private void drawRoutePath(String routePath) {
        googleMap.clear();
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(routePath);
            DirectionsJSONParser parser = new DirectionsJSONParser();

            // Starts parsing data
            routes = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;
        // Traversing through all the routes
        for (int i = 0; i < routes.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = routes.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(20);

            // Changing the color polyline according to the mode
            lineOptions.color(Color.YELLOW);
        }

        // Drawing polyline in the Google Map for the i-th route
        googleMap.addPolyline(lineOptions);
    }

    private void drawStartEndMarker() {
        if (startMk == null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(startPoint)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            startMk = googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(startPoint));
        } else {
            startMk.setPosition(startPoint);
        }

        if (endMk == null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(endPoint)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            endMk = googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(endPoint));
        } else {
            endMk.setPosition(endPoint);
        }
    }
}
