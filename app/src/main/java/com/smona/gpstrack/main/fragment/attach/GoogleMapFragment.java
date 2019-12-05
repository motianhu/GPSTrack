package com.smona.gpstrack.main.fragment.attach;

import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smona.base.ui.fragment.BaseFragment;
import com.smona.google.GoogleLocationManager;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.device.bean.RespDevice;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.logger.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GoogleMapFragment extends BaseFragment implements IMapController, OnMapReadyCallback {

    private SupportMapFragment supportMapFragment;
    private GoogleMap googleMap;

    private String mCurDeviceId;
    private Map<String, Marker> deviceMap = new LinkedHashMap<>();
    private Map<String, Circle> fenceMap = new LinkedHashMap<>();

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
        GoogleLocationManager.getInstance().init(mActivity);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isAdded()) {
            return;
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

    @Override
    public void removeDevice(String deviceId) {
        if (TextUtils.isEmpty(deviceId)) {
            return;
        }
        Marker marker = deviceMap.get(deviceId);
        if (marker == null) {
            return;
        }
        deviceMap.remove(deviceId);
        marker.remove();
    }

    private void drawDevices() {
        if (CommonUtils.isEmpty(deviceList)) {
            return;
        }
        for (RespDevice device : deviceList) {
            if (device == null) {
                continue;
            }
            if (device.getLocation() == null) {
                removeDevice(device.getId());
                continue;
            }
            refreshDeviceMarker(device);
        }
    }

    @Override
    public void rightDevice() {
        if (googleMap == null) {
            ToastUtil.showShort(R.string.map_no_ready);
            return;
        }
        if (deviceMap.size() == 0) {
            ToastUtil.showShort(R.string.no_devices);
            return;
        }
        Marker firstMarker = null;
        Marker nextMarker = null;
        boolean indexSuc = false;

        for (Map.Entry<String, Marker> entry : deviceMap.entrySet()) {
            if (firstMarker == null) {
                firstMarker = entry.getValue();
            }

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

        if (nextMarker == null) {
            nextMarker = firstMarker;
        }

        Object obj = nextMarker.getTag();
        if (obj instanceof RespDevice) {
            mCurDeviceId = ((RespDevice) obj).getId();
            animateCameraCurMarker(nextMarker.getPosition());
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
                animateCameraCurMarker(curMarker.getPosition());
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

    @Override
    public void removeFence(String fenceId) {
        if (TextUtils.isEmpty(fenceId)) {
            return;
        }
        for (Fence f : fenceList) {
            if (fenceId.equals(f.getId())) {
                fenceList.remove(f);
            }
        }
        Circle circle = fenceMap.get(fenceId);
        if (circle == null) {
            return;
        }
        circle.remove();
    }

    @Override
    public void addFence(Fence fence) {
        if (fence == null) {
            return;
        }
        drawCircle(fence);
    }

    @Override
    public void updateFence(Fence fence) {
        if (fence == null) {
            return;
        }
        Circle circle = fenceMap.get(fence.getId());
        if (circle == null) {
            drawCircle(fence);
            return;
        }
        circle.remove();
        drawCircle(fence);
    }

    private void drawFences() {
        if (CommonUtils.isEmpty(fenceList)) {
            return;
        }

        for (Fence fence : fenceList) {
            drawCircle(fence);
        }
    }

    private void drawCircle(Fence fence) {
        LatLng latLng = new LatLng(fence.getLatitude(), fence.getLongitude());
        Circle circle = googleMap.addCircle(new CircleOptions().
                center(latLng).
                fillColor(Fence.getFenceColor(fence.getStatus())).
                radius(fence.getRadius()).
                strokeWidth(1));
        fenceMap.put(fence.getId(), circle);
    }

    @Override
    public void leftDevice() {
        if (googleMap == null) {
            ToastUtil.showShort(R.string.map_no_ready);
            return;
        }
        if (deviceMap.size() == 0) {
            ToastUtil.showShort(R.string.no_devices);
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

        if (preMarker == null) {
            for (Map.Entry<String, Marker> entry : deviceMap.entrySet()) {
                preMarker = entry.getValue();
            }
        }

        Object obj = preMarker.getTag();
        if (obj instanceof RespDevice) {
            mCurDeviceId = ((RespDevice) obj).getId();
            animateCameraCurMarker(preMarker.getPosition());
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
                animateCameraCurMarker(marker.getPosition());
            }
        } else {
            marker.setTag(device);
            marker.setPosition(latLng);
        }
    }

    private void animateCameraCurMarker(LatLng latLng) {
        Logger.d("motianhu", "mCurDevice: " + mCurDeviceId);
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
        GoogleLocationManager.getInstance().refreshLocation(latLng -> {
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        Logger.d("motianhu", "onMapReady: " + googleMap);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(ParamConstant.DEFAULT_POS_LA, ParamConstant.DEFAULT_POS_LO)));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        googleMap.setOnMarkerClickListener(marker -> {
            clickMarker(marker);
            return true;
        });
        drawDevices();
        drawFences();
    }
}