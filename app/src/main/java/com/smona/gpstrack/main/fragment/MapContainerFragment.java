package com.smona.gpstrack.main.fragment;

import android.os.RemoteException;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.MyLocationStyle;
import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.data.MemoryDeviceManager;
import com.smona.gpstrack.device.bean.DeviceListBean;
import com.smona.gpstrack.main.fragment.attach.DeviceDetailFragment;
import com.smona.gpstrack.main.fragment.attach.MapViewFragment;
import com.smona.gpstrack.main.poll.OnPollListener;
import com.smona.gpstrack.main.poll.RefreshPoll;
import com.smona.gpstrack.main.presenter.MapPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.PopupAnim;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.logger.Logger;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:30 PM
 */
public class MapContainerFragment extends BasePresenterFragment<MapPresenter, MapPresenter.IMapView> implements MapPresenter.IMapView {

    private DeviceDetailFragment deviceDetailFragment;
    private MapViewFragment mapViewFragment;

    private TextView refreshCountDownTv;
    private RefreshPoll refreshPoll = new RefreshPoll();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map_main;
    }

    @Override
    protected MapPresenter initPresenter() {
        return new MapPresenter();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mapViewFragment = new MapViewFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapView, mapViewFragment);
        fragmentTransaction.commitAllowingStateLoss();

        rootView.findViewById(R.id.searchDevices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rootView.findViewById(R.id.previousDevice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDevicePart();
            }
        });

        rootView.findViewById(R.id.nextDevice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //rootView.findViewById(R.id.location).setOnClickListener(view -> aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)));

        refreshCountDownTv = rootView.findViewById(R.id.refreshCountDown);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.requestDeviceList();
        refreshPoll.setParam(new OnPollListener() {
            @Override
            public void onFinish() {
                mPresenter.requestDeviceList();
                refreshTv(11);
                refreshPoll.starPoll();
            }

            @Override
            public void onTick(int mills) {
                refreshTv(mills - 1);
            }

            @Override
            public void cancle() {

            }
        });
        refreshPoll.starPoll();
    }

    private void refreshTv(int i) {
        String time = i + "";
        refreshCountDownTv.setText(time);
    }


    private void showDevicePart() {
        if (deviceDetailFragment == null) {
            deviceDetailFragment = new DeviceDetailFragment();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.anchorFragment, deviceDetailFragment);
            fragmentTransaction.commit();
        }
        deviceDetailFragment.setDevice(MemoryDeviceManager.getInstance().getDevice(0));
        deviceDetailFragment.showFragment();
    }

    /**
     * 切换fragment会调用
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 点击进其他Activity会调用
     */
    @Override
    public void onResume() {
        super.onResume();
        mapViewFragment.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapViewFragment.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapViewFragment.onPause();
    }

    @Override
    public void onSuccess(DeviceListBean deviceList) {
        MemoryDeviceManager.getInstance().addDeviceList(deviceList.getDatas());
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        ToastUtil.showShort(errorInfo.getMessage());
    }
}
