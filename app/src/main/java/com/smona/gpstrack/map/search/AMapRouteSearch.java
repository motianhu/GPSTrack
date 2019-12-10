package com.smona.gpstrack.map.search;

import android.graphics.BitmapFactory;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.smona.gpstrack.R;
import com.smona.gpstrack.map.GaodeLocationManager;
import com.smona.gpstrack.map.IMap;
import com.smona.gpstrack.map.listener.CommonLocationListener;
import com.smona.gpstrack.util.AMapUtil;
import com.smona.gpstrack.util.AppContext;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.gpstrack.widget.map.DrivingRouteOverlay;

public abstract class AMapRouteSearch implements IMap, RouteSearch.OnRouteSearchListener {

    private CommonLocationListener listener;
    protected AMap aMap;
    private RouteSearch routeSearch;
    private LatLng phonePoint, devicePoint;
    private Marker phoneMk, deviceMk;

    public void initSearch(CommonLocationListener listener, int type, double targetLa, double targetLo) {
        devicePoint = AMapUtil.wgsToCjg(AppContext.getAppContext(), targetLa, targetLo);
        routeSearch = new RouteSearch(AppContext.getAppContext());
        routeSearch.setRouteSearchListener(this);
        this.listener = listener;
        GaodeLocationManager.getInstance().addLocationListerner(CommonLocationListener.AUTO_LOCATION, listener);
        refreshSearch();
    }

    public void refreshSearch() {
        GaodeLocationManager.getInstance().refreshLocation();
    }

    public void refreshPath() {
        phonePoint = new LatLng(GaodeLocationManager.getInstance().getLocation()[0], GaodeLocationManager.getInstance().getLocation()[1]);
        clearOverLay();
        searchPath();
        drawStartEndMarker();
    }

    private void searchPath() {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(phonePoint.latitude, phonePoint.longitude), new LatLonPoint(devicePoint.latitude, devicePoint.longitude));
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);
    }

    private void clearOverLay() {
        phoneMk = null;
        deviceMk = null;
        aMap.clear();// 清理地图上的所有覆盖物
    }

    @Override
    public void refreshDeviceLoc(double targetLa, double targetLo) {
        devicePoint = AMapUtil.wgsToCjg(AppContext.getAppContext(), targetLa, targetLo);
        refreshDeviceMarker();
    }

    private void drawStartEndMarker() {
        refreshPhoneMarker();
        refreshDeviceMarker();
    }

    private void refreshPhoneMarker() {
        if (phoneMk == null) {
            MarkerOptions markerOption = new MarkerOptions().title("PhoneMarker").snippet("DefaultMarker");
            markerOption.draggable(true);//设置Marker可拖动
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(AppContext.getAppContext().getResources(), R.drawable.mylocation)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(true);//设置marker平贴地图效果
            phoneMk = aMap.addMarker(markerOption);
        }
        phoneMk.setPosition(phonePoint);
    }

    private void refreshDeviceMarker() {
        if (deviceMk == null) {
            MarkerOptions markerOption = new MarkerOptions().title("DeviceMarker").snippet("DefaultMarker");
            markerOption.draggable(true);//设置Marker可拖动
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(AppContext.getAppContext().getResources(), R.drawable.destination)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(true);//设置marker平贴地图效果
            deviceMk = aMap.addMarker(markerOption);
        }
        deviceMk.setPosition(devicePoint);
    }

    public void removeSearch() {
        GaodeLocationManager.getInstance().removeListener(CommonLocationListener.AUTO_LOCATION);
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
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
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
}
