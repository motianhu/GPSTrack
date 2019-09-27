package com.smona.gpstrack.main.fragment;

import android.os.RemoteException;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.MyLocationStyle;
import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.main.fragment.attach.DeviceDetailFragment;
import com.smona.gpstrack.main.presenter.MapPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.logger.Logger;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:30 PM
 */
public class MapMainFragment extends BasePresenterFragment<MapPresenter, MapPresenter.IView> implements MapPresenter.IView {

    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    private SupportMapFragment supportMapFragment;
    private DeviceDetailFragment deviceDetailFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    protected MapPresenter initPresenter() {
        return new MapPresenter();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initMap();
        initDeviceDetail();
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

        rootView.findViewById(R.id.location).setOnClickListener(view -> aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)));
    }

    private void initMap() {
        try {
            MapsInitializer.initialize(getContext());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        AMapOptions aMapOptions = new AMapOptions();
        aMapOptions.zoomControlsEnabled(false);
        supportMapFragment = SupportMapFragment.newInstance(aMapOptions);


        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapView, supportMapFragment);
        fragmentTransaction.commitAllowingStateLoss();
        aMap = supportMapFragment.getMap();
        Logger.e("motianhu", "map : " + aMap);
        if (aMap != null) {
            aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
            myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
            myLocationStyle.interval(2000);
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.getUiSettings().setMyLocationButtonEnabled(false);
            aMap.setMyLocationEnabled(true);
        }
    }

    private void initDeviceDetail() {
        deviceDetailFragment = new DeviceDetailFragment();
    }


    private void showDevicePart() {
//        deviceDetailFragment.setDevice(null);
//        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.anchorFragment, deviceDetailFragment);
//        fragmentTransaction.commit();
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_DEVICE_PART);
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
        supportMapFragment.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        supportMapFragment.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();
        supportMapFragment.onPause();
    }
}
