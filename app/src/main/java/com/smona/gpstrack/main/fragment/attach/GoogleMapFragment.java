package com.smona.gpstrack.main.fragment.attach;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smona.base.ui.fragment.BaseFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.device.bean.RespDevice;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.logger.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GoogleMapFragment extends BaseFragment implements IMapController, OnMapReadyCallback {

    private SupportMapFragment supportMapFragment;
    private GoogleMap googleMap;

    private String mCurDeviceId;
    private Map<String, Marker> deviceMap = new LinkedHashMap<>();

    private List<Fence> fenceList;
    private List<RespDevice> deviceList;

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
        GoogleMapOptions aMapOptions = new GoogleMapOptions();
        aMapOptions.zoomControlsEnabled(false);
        supportMapFragment = SupportMapFragment.newInstance(aMapOptions);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_view, supportMapFragment);
        fragmentTransaction.commitAllowingStateLoss();
        supportMapFragment.getMapAsync(this);
    }

    private void clickMarker(Marker marker) {
        if (marker == null) {
            return;
        }

        Object object = marker.getTag();
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
    public void drawDevices(List<RespDevice> deviceList) {
        this.deviceList = deviceList;
        if (googleMap == null) {
            return;
        }
        drawDevices();
    }

    private void drawDevices() {
        if (CommonUtils.isEmpty(deviceList)) {
            return;
        }
        for (RespDevice device : deviceList) {
            refreshDeviceMarker(device);
        }
    }

    @Override
    public void rightDevice() {
        if (googleMap == null) {
            return;
        }
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
            Object obj = nextMarker.getTag();
            if (obj instanceof RespDevice) {
                mCurDeviceId = ((RespDevice) obj).getId();
                refreshCurrentDeviceMarker();
            }
        }
    }

    @Override
    public void setCurDevice(RespDevice device) {
        if (googleMap == null) {
            return;
        }
        if (device == null) {
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
            Object obj = curMarker.getTag();
            if (obj instanceof RespDevice) {
                mCurDeviceId = ((RespDevice) obj).getId();
                refreshCurrentDeviceMarker();
            }
        }
    }

    @Override
    public void drawFences(List<Fence> fenceList) {
        this.fenceList = fenceList;
        if (googleMap == null) {
            return;
        }
        drawFences();
    }

    private void drawFences() {
        if (CommonUtils.isEmpty(fenceList)) {
            return;
        }

        for (Fence fence : fenceList) {
            LatLng latLng = new LatLng(fence.getLatitude(), fence.getLongitude());
            googleMap.addCircle(new CircleOptions().
                    center(latLng).
                    fillColor(Color.argb(50, 1, 1, 1)).
                    radius(fence.getRadius()).
                    strokeWidth(1));
        }
    }

    @Override
    public void leftDevice() {
        if (googleMap == null) {
            return;
        }
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
            Object obj = preMarker.getTag();
            if (obj instanceof RespDevice) {
                mCurDeviceId = ((RespDevice) obj).getId();
                refreshCurrentDeviceMarker();
            }
        }
    }

    private void refreshDeviceMarker(RespDevice device) {
        Marker marker = deviceMap.get(device.getId());
        LatLng latLng = new LatLng(device.getLocation().getLatitude(), device.getLocation().getLongitude());
        if (marker == null) {
            MarkerOptions markerOption = new MarkerOptions().title(device.getName()).snippet("DefaultMarker");
            markerOption.draggable(true);//设置Marker可拖动
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.destination)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.position(latLng);
            marker = googleMap.addMarker(markerOption);
            marker.setTag(device);
            deviceMap.put(device.getId(), marker);
            if (TextUtils.isEmpty(mCurDeviceId)) {
                mCurDeviceId = device.getId();
            }
        } else {
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
        Object obj = marker.getTag();
        if (!(obj instanceof RespDevice)) {
            return;
        }
        RespDevice device = (RespDevice) obj;
        LatLng latLng = new LatLng(device.getLocation().getLatitude(), device.getLocation().getLongitude());
        marker.setPosition(latLng);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
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
        // googleMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(marker -> {
            clickMarker(marker);
            return true;
        });
        Logger.d("motianhu", "onMapReady: " + googleMap);
        drawDevices();
        drawFences();
    }
}