package com.smona.gpstrack.device;

import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.presenter.DeviceNavigationPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.Constant;
import com.smona.gpstrack.util.SPUtils;
import com.smona.http.wrapper.ErrorInfo;

@Route(path = ARouterPath.PATH_TO_DEVICE_NAVIGATION)
public class DeviceNavigationActivity extends BasePresenterActivity<DeviceNavigationPresenter, DeviceNavigationPresenter.IDeviceNavigation> implements DeviceNavigationPresenter.IDeviceNavigation {

    private MapView mMapView;
    private AMap aMap;

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
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView titleTv = findViewById(R.id.title);
        titleTv.setText(R.string.deviceNavigation);

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

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
