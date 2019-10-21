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
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.device.presenter.DeviceNavigationPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.Constant;
import com.smona.gpstrack.util.SPUtils;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

@Route(path = ARouterPath.PATH_TO_DEVICE_NAVIGATION)
public class DeviceNavigationActivity extends BasePresenterActivity<DeviceNavigationPresenter, DeviceNavigationPresenter.IDeviceNavigation> implements DeviceNavigationPresenter.IDeviceNavigation, AMap.OnMyLocationChangeListener
        , RouteSearch.OnRouteSearchListener {

    private MapView mMapView;
    private AMap aMap;

    private LatLonPoint startPoint = null;
    private LatLonPoint endPoint = null;
    private Marker startMk, targetMk;
    private RouteSearch routeSearch;

    private RespDevice device;
    //https://github.com/amapapi/Android_3D_Demo/blob/master/src/com/amapv2/apis/route/RouteActivity.java

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
            aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
            myLocationStyle.interval(2000);
            aMap.setMyLocationStyle(myLocationStyle);
            String language = (String) SPUtils.get(Constant.SP_KEY_LANGUAGE, Constant.VALUE_LANGUAGE_ZH_CN);
            if (Constant.VALUE_LANGUAGE_EN.equals(language)) {
                aMap.setMapLanguage(AMap.ENGLISH);
            } else {
                aMap.setMapLanguage(AMap.CHINESE);
            }
        }
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

    private void initObject(){
       routeSearch = new RouteSearch(this);
       routeSearch.setRouteSearchListener(this);
       refreshUI();
    }

    private void refreshUI() {
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
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int rCode) {
        if (rCode == 0) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null
                    && driveRouteResult.getPaths().size() > 0) {
                DrivePath drivePath = driveRouteResult.getPaths().get(0);
                aMap.clear();// 清理地图上的所有覆盖物
                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                        this, aMap, drivePath, driveRouteResult.getStartPos(),
                        driveRouteResult.getTargetPos());
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
            } else {
                ToastUtil.showShort(R.string.no_result);
            }
        } else if (rCode == 27) {
            ToastUtil.showShort(R.string.error_network);
        } else if (rCode == 32) {
            ToastUtil.showShort(R.string.error_key);
        } else {
            ToastUtil.showShort(getString(R.string.error_other)
                    + rCode);
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
        startPoint.setLatitude(location.getLatitude());
        startPoint.setLongitude(location.getLongitude());
        refreshUI();
    }
}
