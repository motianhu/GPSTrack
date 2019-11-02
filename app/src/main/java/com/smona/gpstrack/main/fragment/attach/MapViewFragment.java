package com.smona.gpstrack.main.fragment.attach;

import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.smona.base.ui.fragment.BaseFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.map.MapAImpl;
import com.smona.gpstrack.settings.adapter.MapAdapter;
import com.smona.gpstrack.util.AMapUtil;
import com.smona.logger.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

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

    private String mCurDeviceId;
    private Map<String, Marker> deviceMap = new LinkedHashMap<>();

    private Map<String, Circle> fenceMap = new LinkedHashMap<>();

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
            myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
            myLocationStyle.interval(2000);
            aMap.setMyLocationStyle(myLocationStyle);

            MapAImpl.initMap(aMap, AMapUtil.wgsToCjg(mActivity, ParamConstant.DEFAULT_POS.latitude, ParamConstant.DEFAULT_POS.longitude));

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
        refreshDeviceMarker(device);
    }

    @Override
    public void rightDevice() {
        if (deviceMap.size() == 0) {
            return;
        }
        Marker nextMarker = null;
        boolean indexSuc = false;

        for (Map.Entry<String, Marker> entry : deviceMap.entrySet()) {
            if (TextUtils.isEmpty(mCurDeviceId)) {
                nextMarker = entry.getValue();
                break;
            }
            if (indexSuc) {
                nextMarker = entry.getValue();
                break;
            }
            if (mCurDeviceId.equalsIgnoreCase(entry.getKey())) {
                indexSuc = true;
            }
        }

        if (nextMarker != null) {
            Object obj = nextMarker.getObject();
            if (obj instanceof RespDevice) {
                mCurDeviceId = ((RespDevice) obj).getId();
                refreshCurrentDeviceMarker();
            }
        }
    }

    @Override
    public void setCurDevice(RespDevice device) {
        if(device == null) {
            return;
        }
        Marker curMarker = null;
        for (Map.Entry<String, Marker> entry : deviceMap.entrySet()) {
            if (device.getId().equalsIgnoreCase(entry.getKey())) {
                curMarker = entry.getValue();
                break;
            }
        }

        if (curMarker != null) {
            Object obj = curMarker.getObject();
            if (obj instanceof RespDevice) {
                mCurDeviceId = ((RespDevice) obj).getId();
                refreshCurrentDeviceMarker();
            }
        }
    }

    @Override
    public void drawFence(Fence fence) {
        LatLng latLng = AMapUtil.wgsToCjg(mActivity, fence.getLatitude(), fence.getLongitude());
        Circle circle = MapAImpl.drawFence(aMap, latLng, (int)fence.getRadius());
        fenceMap.put(fence.getId(), circle);
    }

    @Override
    public void leftDevice() {
        if (deviceMap.size() == 0) {
            return;
        }
        Marker preMarker = null;
        for (Map.Entry<String, Marker> entry : deviceMap.entrySet()) {
            if (TextUtils.isEmpty(mCurDeviceId)) {
                preMarker = entry.getValue();
                break;
            }
            if (mCurDeviceId.equalsIgnoreCase(entry.getKey())) {
                break;
            } else {
                preMarker = entry.getValue();
            }
        }

        if (preMarker != null) {
            Object obj = preMarker.getObject();
            if (obj instanceof RespDevice) {
                mCurDeviceId = ((RespDevice) obj).getId();
                refreshCurrentDeviceMarker();
            }
        }
    }

    private void refreshDeviceMarker(RespDevice device) {
        Marker marker = deviceMap.get(device.getId());
        if (marker == null) {
            MarkerOptions markerOption = new MarkerOptions().title(device.getName()).snippet("DefaultMarker");
            markerOption.draggable(true);//设置Marker可拖动
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.destination)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(true);//设置marker平贴地图效果
            marker = aMap.addMarker(markerOption);
            marker.setObject(device);

            LatLng latLng = AMapUtil.wgsToCjg(mActivity, device.getLocation().getLatitude(), device.getLocation().getLongitude());
            marker.setPosition(latLng);
            deviceMap.put(device.getId(), marker);
            if (TextUtils.isEmpty(mCurDeviceId)) {
                mCurDeviceId = device.getId();
            }
        } else {
            LatLng latLng = AMapUtil.wgsToCjg(mActivity, device.getLocation().getLatitude(), device.getLocation().getLongitude());
            marker.setPosition(latLng);
        }

        refreshCurrentDeviceMarker();
    }

    private void refreshCurrentDeviceMarker() {
        Logger.d("motianhu", "mCurDevice: " + mCurDeviceId);
        if (TextUtils.isEmpty(mCurDeviceId)) {
            return;
        }
        Marker marker = deviceMap.get(mCurDeviceId);
        if (marker == null) {
            return;
        }
        Object obj = marker.getObject();
        if (!(obj instanceof RespDevice)) {
            return;
        }
        RespDevice device = (RespDevice) obj;
        LatLng latLng = AMapUtil.wgsToCjg(mActivity, device.getLocation().getLatitude(), device.getLocation().getLongitude());
        marker.setPosition(latLng);
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
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
