package com.smona.gpstrack.map.search;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.smona.gpstrack.map.GoogleLocationManager;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.GpsFixedBuilder;
import com.smona.gpstrack.map.IMap;
import com.smona.gpstrack.map.listener.CommonLocationListener;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;
import com.smona.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 谷歌地图导航
 */
public abstract class GoogleRouteSearch implements IMap {

    protected GoogleMap googleMap;
    private LatLng phonePoint, devicePoint;
    private Marker phoneMk, deviceMk;

    @Override
    public void initSearch(int type, double deviceLa, double deviceLo) {
        refreshDevicePosition(deviceLa, deviceLo);
        GoogleLocationManager.getInstance().addLocationListerner(CommonLocationListener.AUTO_LOCATION, (type1, la, lo) -> {
            if (phonePoint == null) {
                firstLoadSearch(la, lo);
                return;
            }
            refreshPhone(la, lo);
            refreshPhoneMarker();
        });
        double la = GoogleLocationManager.getInstance().getLocation()[0];
        double lo = GoogleLocationManager.getInstance().getLocation()[1];
        if(CommonUtils.isInValidLatln(la, lo)) {
            GoogleLocationManager.getInstance().refreshLocation();
        } else {
            firstLoadSearch(la, lo);
        }
    }

    private void firstLoadSearch(double la, double lo) {
        refreshPhone(la, lo);
        refreshPhoneMarker();
        refreshRoute();
    }

    @Override
    public void refreshDevice(double deviceLa, double deviceLo) {
        refreshDevicePosition(deviceLa, deviceLo);
        refreshDeviceMarker();
    }

    @Override
    public void removeSearch() {
        GoogleLocationManager.getInstance().removeListener(CommonLocationListener.AUTO_LOCATION);
    }

    @Override
    public void refreshRoute() {
        if (googleMap == null) {
            return;
        }
        if (phonePoint == null) {
            return;
        }
        clearOverLay();
        searchPath();
        drawMarker();
    }

    private void refreshDevicePosition(double deviceLa, double deviceLo) {
        devicePoint = new LatLng(deviceLa, deviceLo);
    }

    private void drawMarker() {
        refreshPhoneMarker();
        refreshDeviceMarker();
    }

    private void refreshDeviceMarker() {
        if (deviceMk == null) {
            MarkerOptions markerOptions = new MarkerOptions().position(devicePoint).icon(BitmapDescriptorFactory.fromResource(R.drawable.destination));
            deviceMk = googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(devicePoint));
        } else {
            deviceMk.setPosition(devicePoint);
        }
    }

    private void refreshPhoneMarker() {
        if (phoneMk == null) {
            MarkerOptions markerOptions = new MarkerOptions().position(phonePoint).icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation));
            phoneMk = googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(phonePoint));
        } else {
            phoneMk.setPosition(phonePoint);
        }
    }

    private void refreshPhone(double phoneLa, double phoneLo) {
        phonePoint = new LatLng(phoneLa, phoneLo);
    }

    private void clearOverLay() {
        phoneMk = null;
        deviceMk = null;
        googleMap.clear();// 清理地图上的所有覆盖物
    }

    private void searchPath() {
        String url = getDirectionsUrl(new LatLng(phonePoint.latitude, phonePoint.longitude), new LatLng(devicePoint.latitude, devicePoint.longitude));
        //String url = getDirectionsUrl(new LatLng(39.99709957757345, 116.31184045225382), new LatLng(39.949158391497214, 116.4154639095068));
//        String url = getDirectionsUrl(new LatLng(22.313283, 113.945510), devicePoint);
        Logger.d("motianhu", "url: " + url);

        OnResultListener<String> listener = new OnResultListener<String>() {
            @Override
            public void onSuccess(String s) {
                Logger.e("motianhu", "s: " + s);
                drawRoutePath(s);
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                ToastUtil.showShort(errorInfo.getMessage());
                Logger.e("motianhu", "stateCode: " + stateCode + ", errorInfo: " + errorInfo.getMessage());
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
            lineOptions.color(Color.BLUE);
        }

        if (lineOptions == null) {
            ToastUtil.showShort(R.string.no_result);
            return;
        }
        if (points.size() > 0) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(points.get(0))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_start));
            googleMap.addMarker(markerOptions);
        }
        if (points.size() > 1) {
            MarkerOptions endmarkerOptions = new MarkerOptions()
                    .position(points.get(points.size() - 1))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_end));
            googleMap.addMarker(endmarkerOptions);
        }
        // Drawing polyline in the Google Map for the i-th route
        googleMap.addPolyline(lineOptions);
    }
}
