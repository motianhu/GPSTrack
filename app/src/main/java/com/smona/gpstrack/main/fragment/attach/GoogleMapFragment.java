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
import com.smona.gpstrack.map.GoogleLocationManager;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.device.bean.RespDevice;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.smona.gpstrack.map.listener.CommonLocationListener;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.logger.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页谷歌地图
 */
public class GoogleMapFragment extends BaseFragment implements IMapController, OnMapReadyCallback, CommonLocationListener {

    private SupportMapFragment supportMapFragment;
    private GoogleMap googleMap;

    private String mCurDeviceId;
    private Map<String, Marker> deviceMap = new LinkedHashMap<>();
    private Map<String, Circle> circleMap = new LinkedHashMap<>();

    private List<Fence> fenceList = new ArrayList<>();
    private List<RespDevice> deviceList;

    private IMapCallback mapCallback;

    private boolean isFirstLocation = true;
    private boolean isClickLocation = false;
    private Marker mPhoneMarker;

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
        GoogleLocationManager.getInstance().addLocationListerner(CommonLocationListener.CLICK_LOCATION, this);
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
     * 切换Tab会调用
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            toUserVisible();
        } else {
            toUserGone();
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
        GoogleLocationManager.getInstance().clear();
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
            animatePosition(nextMarker.getPosition());
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
                animatePosition(curMarker.getPosition());
            }
        }
    }

    @Override
    public void drawFences(List<Fence> fenceList) {
        this.fenceList.clear();
        this.fenceList.addAll(fenceList);
        if (googleMap == null) {
            return;
        }
        if(!getUserVisibleHint()) {
            return;
        }
        drawFences();
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
        circleMap.put(fence.getId(), circle);
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
    }

    @Override
    public void addFence(Fence fence) {
        if (fence == null) {
            return;
        }
        for (Fence f : fenceList) {
            if (fence.getId().equals(f.getId())) {
                fenceList.remove(f);
            }
        }
        fenceList.add(fence);
    }

    @Override
    public void updateFence(Fence fence) {
        addFence(fence);
    }

    private void toUserVisible() {
        if (googleMap == null) {
            return;
        }
        if (fenceList.size() == 0) {
            return;
        }
        Circle circle;
        for (Fence fence : fenceList) {
            circle = circleMap.remove(fence.getId());
            if (circle != null) {
                circle.remove();
            }
            drawCircle(fence);
        }
    }

    private void toUserGone() {
        if (googleMap == null) {
            return;
        }
        if (circleMap.size() == 0) {
            return;
        }
        for (Map.Entry<String, Circle> entry : circleMap.entrySet()) {
            entry.getValue().remove();
        }
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
            animatePosition(preMarker.getPosition());
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
                animatePosition(marker.getPosition());
            }
        } else {
            marker.setTag(device);
            marker.setPosition(latLng);
        }
    }

    private void animatePosition(LatLng latLng) {
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

    //定位
    @Override
    public void location() {
        isClickLocation = true;
        double la = GoogleLocationManager.getInstance().getLocation()[0];
        double lo = GoogleLocationManager.getInstance().getLocation()[1];
        if (CommonUtils.isInValidLatln(la, lo)) {
            GoogleLocationManager.getInstance().refreshLocation();
        } else {
            onLocation(CommonLocationListener.CLICK_LOCATION, la, lo);
        }
    }

    //谷歌地图准备好了，可以使用了。
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        Logger.d("motianhu", "onMapReady: " + googleMap);
        //移动地图中心点
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(ParamConstant.DEFAULT_POS_LA, ParamConstant.DEFAULT_POS_LO)));
        //缩放地图等级
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        //设置marker点击
        googleMap.setOnMarkerClickListener(marker -> {
            clickMarker(marker);
            return true;
        });
        //绘制设备
        drawDevices();
        //绘制电子围栏
        drawFences();
    }

    @Override
    public void onLocation(int type, double la, double lo) {
        if (googleMap == null) {
            return;
        }
        if (CommonUtils.isInValidLatln(la, lo)) {
            return;
        }

        LatLng latLng = new LatLng(la, lo);
        if (mPhoneMarker == null) {
            createPhoneMarker(latLng);
        } else {
            mPhoneMarker.setPosition(latLng);
        }

        if(isFirstLocation && TextUtils.isEmpty(mCurDeviceId)) {
            isFirstLocation = false;
            animatePosition(latLng);
        } else if (isClickLocation) {
            isClickLocation = false;
            animatePosition(latLng);
        }
    }

    /**
     * 创建手机Marker
     * @param latLng
     */
    private void createPhoneMarker(LatLng latLng) {
        MarkerOptions markerOption = new MarkerOptions().title("PhonePositino").snippet("DefaultMarker");
        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mylocation)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.position(latLng);
        mPhoneMarker = googleMap.addMarker(markerOption);
    }
}