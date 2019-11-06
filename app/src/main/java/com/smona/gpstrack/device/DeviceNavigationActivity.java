package com.smona.gpstrack.device;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
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
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.device.presenter.DeviceNavigationPresenter;
import com.smona.gpstrack.map.GaodeMapView;
import com.smona.gpstrack.util.AMapUtil;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.gpstrack.widget.map.DrivingRouteOverlay;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.logger.Logger;

@Route(path = ARouterPath.PATH_TO_DEVICE_NAVIGATION)
public class DeviceNavigationActivity extends BasePresenterActivity<DeviceNavigationPresenter, DeviceNavigationPresenter.IDeviceNavigation>
        implements DeviceNavigationPresenter.IDeviceNavigation, AMap.OnMyLocationChangeListener, RouteSearch.OnRouteSearchListener {

    private MapView mMapView;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;

    private DriveRouteResult mDriveRouteResult;
    private LatLonPoint startPoint = null;
    private LatLonPoint endPoint = null;
    private Marker startMk, endMk;
    private RouteSearch routeSearch;

    private RespDevice device;

    @Override
    protected DeviceNavigationPresenter initPresenter() {
        return new DeviceNavigationPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_navigation;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        initSeralize();
        initHeader();
        initViews();
        initObject();
    }

    private void initSeralize() {
        Bundle bundle = getIntent().getBundleExtra(ARouterPath.PATH_TO_DEVICE_NAVIGATION);
        if (bundle == null) {
            finish();
            return;
        }
        device = (RespDevice) bundle.getSerializable(ARouterPath.PATH_TO_DEVICE_NAVIGATION);
        if (device == null) {
            finish();
        }
        endPoint = new LatLonPoint(device.getLocation().getLatitude(), device.getLocation().getLongitude());
    }

    private void initHeader() {
        findViewById(R.id.back).setOnClickListener(view -> finish());
        TextView titleTv = findViewById(R.id.title);
        titleTv.setText(R.string.deviceNavigation);
        ImageView refreshIv = findViewById(R.id.rightIv);
        refreshIv.setVisibility(View.VISIBLE);
        refreshIv.setImageResource(R.drawable.refresh);
    }

    private void initViews() {
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(null);
        if (aMap == null) {
            aMap = mMapView.getMap();

            aMap.setOnMyLocationChangeListener(this);

            myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
            myLocationStyle.interval(5000);
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.setMyLocationEnabled(true);

            GaodeMapView.initMap(aMap, AMapUtil.wgsToCjg(this, ParamConstant.DEFAULT_POS.latitude, ParamConstant.DEFAULT_POS.longitude));
        }
    }

    private void initObject() {
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
    }

    private void refreshUI() {
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

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT,
                null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }


    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        ToastUtil.showShort(errorInfo.getMessage());
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        hideLoadingDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    if (drivePath == null) {
                        return;
                    }
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            this, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                } else if (result.getPaths() == null) {
                    ToastUtil.showShort(R.string.no_result);
                }

            } else {
                ToastUtil.showShort(R.string.no_result);
            }
        } else {
            ToastUtil.showShort(errorCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onMyLocationChange(Location location) {
        Logger.d("motianhu", "onMyLocationChange location: " + location);
        startPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
        refreshUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
