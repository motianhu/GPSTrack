package com.smona.gpstrack.main.fragment.attach;

import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.smona.base.ui.fragment.BaseFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.logger.Logger;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 10/11/19 3:16 PM
 */
public class MapViewFragment extends BaseFragment implements IMapController {

    private SupportMapFragment supportMapFragment;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;

    private RespDevice mCurrDevice = null;
    private Marker mCurrMarker;

    private IMapCallback mapCallback;

    @Override
    protected View getBaseView() {
        return View.inflate(getActivity(), R.layout.fragment_map, null);
    }

    @Override
    protected void initView(View content) {
        super.initView(content);
        initMap();
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
        fragmentTransaction.add(R.id.map_view, supportMapFragment);
        fragmentTransaction.commitAllowingStateLoss();
        aMap = supportMapFragment.getMap();
        if (aMap != null) {
            aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
            myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
            myLocationStyle.interval(2000);
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.getUiSettings().setMyLocationButtonEnabled(false);
            aMap.setOnMarkerClickListener(marker -> {
                clickMarker(marker);
                return true;
            });
        }
    }

    private void clickMarker(Marker marker) {
        if (marker == null) {
            return;
        }

        Object object = marker.getObject();
        if (object instanceof RespDevice) {
            if (mapCallback != null) {
                mapCallback.clickMark(((RespDevice) object));
            }
        }
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
    public void drawDevice(RespDevice device) {
        if (device == null) {
            return;
        }
        if (device.getLocation() == null) {
            return;
        }
        mCurrDevice = device;
        refreshDeviceMarker();
    }

    private void refreshDeviceMarker() {
        if (mCurrMarker == null) {
            MarkerOptions markerOption = new MarkerOptions().title(mCurrDevice.getName()).snippet("DefaultMarker");
            markerOption.draggable(true);//设置Marker可拖动
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.mipmap.ic_launcher)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(true);//设置marker平贴地图效果
            mCurrMarker = aMap.addMarker(markerOption);
            mCurrMarker.setObject(mCurrDevice);

            LatLng latLng = new LatLng(mCurrDevice.getLocation().getLatitude(), mCurrDevice.getLocation().getLongitude());
            mCurrMarker.setPosition(latLng);

            aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
        } else {
            LatLng latLng = new LatLng(mCurrDevice.getLocation().getLatitude(), mCurrDevice.getLocation().getLongitude());
            mCurrMarker.setPosition(latLng);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        supportMapFragment.onPause();
    }

    @Override
    public Fragment getMapFragment() {
        return this;
    }

    @Override
    public void setMapCallback(IMapCallback mapCallback) {
        this.mapCallback = mapCallback;
    }

    @Override
    public void location() {
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE));
    }
}
