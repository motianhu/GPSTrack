package com.smona.gpstrack.device;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.datacenter.DeviceListCenter;
import com.smona.gpstrack.datacenter.IDeviceChangeListener;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.device.presenter.DeviceNavigationPresenter;
import com.smona.gpstrack.map.IMap;
import com.smona.gpstrack.map.MapViewProxy;
import com.smona.gpstrack.map.listener.OnMapReadyListener;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.http.wrapper.ErrorInfo;

import java.util.List;

/**
 * 设备导航
 */
@Route(path = ARouterPath.PATH_TO_DEVICE_NAVIGATION)
public class DeviceNavigationActivity extends BasePresenterActivity<DeviceNavigationPresenter, DeviceNavigationPresenter.IDeviceNavigation>
        implements DeviceNavigationPresenter.IDeviceNavigation, OnMapReadyListener, IDeviceChangeListener {

    private MapViewProxy mMapView;
    private IMap aMap;

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
        initMap();
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
        DeviceListCenter.getInstance().registerDeviceChangeListener(this);
    }

    private void initHeader() {
        findViewById(R.id.back).setOnClickListener(view -> finish());
        TextView titleTv = findViewById(R.id.title);
        titleTv.setText(R.string.deviceNavigation);
        ImageView refreshIv = findViewById(R.id.rightIv);
        refreshIv.setVisibility(View.VISIBLE);
        refreshIv.setImageResource(R.drawable.refresh);
        refreshIv.setOnClickListener(v -> {
            if (aMap != null) {
                aMap.refreshRoute();
            }
        });
    }

    private void initMap() {
        mMapView = new MapViewProxy();
        mMapView.buildMap();

        FrameLayout frameLayout = findViewById(R.id.mapContainer);
        frameLayout.addView(mMapView.getMapView(this));
        mMapView.onCreate(null);
        mMapView.setMapReadyListener(this);
        initMapReady();
    }

    private void initMapReady() {
        aMap = mMapView.getMap();
        if (aMap != null) {
            aMap.initSearch(0, device.getLocation().getLatitude(), device.getLocation().getLongitude());
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        CommonUtils.showToastByFilter(errCode, errorInfo.getMessage());
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
        if (aMap != null) {
            aMap.removeSearch();
        }
        mMapView.onDestroy();
        DeviceListCenter.getInstance().removeChangeListener();
    }

    @Override
    public void onMapReady() {
        initMapReady();
    }

    @Override
    public void onChangeListener(List<RespDevice> deviceList) {
        if (CommonUtils.isEmpty(deviceList)) {
            return;
        }
        boolean needRefreshDeviceLoc = false;
        for (RespDevice respDevice : deviceList) {
            if (respDevice.getId().equals(device.getId())) {
                if (respDevice.getLocation() != null) {
                    needRefreshDeviceLoc = true;
                    device.setLocation(respDevice.getLocation());
                }
                break;
            }
        }
        if (needRefreshDeviceLoc) {
            if (aMap != null) {
                aMap.refreshDevice(device.getLocation().getLatitude(), device.getLocation().getLongitude());
            }
        }
    }
}
