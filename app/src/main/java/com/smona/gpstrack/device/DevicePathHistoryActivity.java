package com.smona.gpstrack.device;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.db.table.Location;
import com.smona.gpstrack.device.presenter.DeviceHistoryPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.Constant;
import com.smona.gpstrack.util.SPUtils;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/29/19 12:59 PM
 */

@Route(path = ARouterPath.PATH_TO_DEVICE_HISTORY)
public class DevicePathHistoryActivity extends BasePresenterActivity<DeviceHistoryPresenter, DeviceHistoryPresenter.IDeviceHistory> implements DeviceHistoryPresenter.IDeviceHistory {

    private MapView mMapView;
    private AMap aMap;

    @Override
    protected DeviceHistoryPresenter initPresenter() {
        return new DeviceHistoryPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_location_hitory;
    }

    @Override
    protected void initContentView() {
        super.initContentView();

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
    public void onSuccess(List<Location> datas) {

    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        ToastUtil.showShort(errorInfo.getMessage());
    }
}
