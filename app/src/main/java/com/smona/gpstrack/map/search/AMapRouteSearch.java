package com.smona.gpstrack.map.search;

import android.app.Activity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.smona.gpstrack.R;
import com.smona.gpstrack.util.AMapUtil;
import com.smona.gpstrack.util.AppContext;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.gpstrack.widget.map.DrivingRouteOverlay;
import com.smona.logger.Logger;

public abstract class AMapRouteSearch implements RouteSearch.OnRouteSearchListener, AMap.OnMyLocationChangeListener {

    protected AMap aMap;
    private RouteSearch routeSearch;
    private LatLonPoint startPoint, endPoint;
    private Marker startMk, endMk;

    public void initSearch(Activity activity, int type, double targetLa, double targetLo) {
        LatLng latLng = AMapUtil.wgsToCjg(AppContext.getAppContext(), targetLa, targetLo);
        endPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.interval(5000);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);

        routeSearch = new RouteSearch(AppContext.getAppContext());
        routeSearch.setRouteSearchListener(this);
    }

    private void searchPath() {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        //do nothing
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
        //do nothing
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
        //do nothing
    }

    @Override
    public void onMyLocationChange(android.location.Location location) {
        Logger.d("motianhu", "onMyLocationChange: " + location);
        startPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
        drawStartEndMarker();
        searchPath();
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode != AMapException.CODE_AMAP_SUCCESS) {
            ToastUtil.showShort(R.string.no_result);
            return;
        }
        if (result == null || result.getPaths() == null || result.getPaths().size() == 0) {
            ToastUtil.showShort(R.string.no_result);
            return;
        }
        final DrivePath drivePath = result.getPaths().get(0);
        if (drivePath == null) {
            ToastUtil.showShort(R.string.no_result);
            return;
        }
        DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(AppContext.getAppContext(), aMap, drivePath, result.getStartPos(), result.getTargetPos(), null);
        drivingRouteOverlay.setNodeIconVisibility(false);
        drivingRouteOverlay.setIsColorfulline(true);
        drivingRouteOverlay.removeFromMap();
        drivingRouteOverlay.addToMap();
        drivingRouteOverlay.zoomToSpan();
    }

    private void drawStartEndMarker() {
        if (startMk == null) {
            startMk = aMap.addMarker(new MarkerOptions().position(AMapUtil.convertToLatLng(startPoint)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background)));
            aMap.animateCamera(CameraUpdateFactory.changeLatLng(AMapUtil.convertToLatLng(startPoint)));
        } else {
            startMk.setPosition(AMapUtil.convertToLatLng(startPoint));
        }

        if (endMk == null) {
            endMk = aMap.addMarker(new MarkerOptions().position(AMapUtil.convertToLatLng(endPoint)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background)));
        } else {
            endMk.setPosition(AMapUtil.convertToLatLng(endPoint));
        }
    }
}
