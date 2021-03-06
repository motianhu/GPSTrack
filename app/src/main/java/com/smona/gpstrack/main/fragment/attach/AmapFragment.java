package com.smona.gpstrack.main.fragment.attach;

import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.smona.base.ui.fragment.BaseFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.map.MapGaode;
import com.smona.gpstrack.map.listener.CommonLocationListener;
import com.smona.gpstrack.util.AMapUtil;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.logger.Logger;
import com.smona.gpstrack.map.GaodeLocationManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 * 首页高德地图
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 10/11/19 3:16 PM
 */
public class AmapFragment extends BaseFragment implements IMapController, CommonLocationListener {

    private MapView mapView;
    private AMap aMap;

    private String mCurDeviceId;
    private Map<String, Marker> deviceMap = new LinkedHashMap<>();
    private Map<String, Circle> circleMap = new LinkedHashMap<>();
    private Map<String, Fence> fenceMap = new LinkedHashMap<>();

    private boolean isFirstLocation = true;
    private boolean isClickLocation = false;
    private Marker mPhoneMarker;

    private IMapCallback mapCallback;

    @Override
    protected View getBaseView() {
        return View.inflate(getActivity(), R.layout.amap_view, null);
    }

    @Override
    protected void initView(View content) {
        super.initView(content);
        initMap(content);
    }

    private void initMap(View rootView) {
        mapView = (MapView) rootView;
        mapView.onCreate(null);
        aMap = mapView.getMap();
        if (aMap != null) {
            GaodeLocationManager.getInstance().addLocationListerner(CommonLocationListener.CLICK_LOCATION, this);
            GaodeLocationManager.getInstance().init(mActivity);
            MapGaode.initMap(aMap);
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
        mapView.onResume();
        toUserVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        toUserGone();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        GaodeLocationManager.getInstance().clear();
    }

    @Override
    public void drawDevices(List<RespDevice> deviceList) {
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

    @Override
    public void rightDevice() {
        if (deviceMap.size() == 0) {
            CommonUtils.showShort(mActivity, R.string.no_devices);
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

        Object obj = nextMarker.getObject();
        if (obj instanceof RespDevice) {
            mCurDeviceId = ((RespDevice) obj).getId();
            animatePosition(nextMarker.getPosition());
        }
    }

    @Override
    public void setCurDevice(RespDevice device) {
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
            Object obj = curMarker.getObject();
            if (obj instanceof RespDevice) {
                mCurDeviceId = ((RespDevice) obj).getId();
                animatePosition(curMarker.getPosition());
            }
        }
    }

    @Override
    public void drawFences(List<Fence> fenceList) {
        for (Fence fence : fenceList) {
            fenceMap.put(fence.getId(), fence);
            if(!getUserVisibleHint()) {
                continue;
            }
            drawCircle(fence);
        }
    }

    private void drawCircle(Fence fence) {
        LatLng latLng = AMapUtil.wgsToCjg(mActivity, fence.getLatitude(), fence.getLongitude());
        Circle circle = aMap.addCircle(new CircleOptions().
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
        fenceMap.remove(fenceId);
    }

    @Override
    public void addFence(Fence fence) {
        if (fence == null) {
            return;
        }
        fenceMap.remove(fence.getId());
        fenceMap.put(fence.getId(), fence);
    }

    @Override
    public void updateFence(Fence fence) {
        addFence(fence);
    }

    private void toUserVisible() {
        if (fenceMap.size() == 0) {
            return;
        }
        Circle circle;
        for (Map.Entry<String, Fence> entry : fenceMap.entrySet()) {
            circle = circleMap.remove(entry.getKey());
            if (circle != null) {
                circle.remove();
            }
            drawCircle(entry.getValue());
        }
    }

    private void toUserGone() {
        if (circleMap.size() == 0) {
            return;
        }
        for (Map.Entry<String, Circle> entry : circleMap.entrySet()) {
            entry.getValue().remove();
        }
    }

    @Override
    public void leftDevice() {
        if (deviceMap.size() == 0) {
            CommonUtils.showShort(mActivity, R.string.no_devices);
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

        Object obj = preMarker.getObject();
        if (obj instanceof RespDevice) {
            mCurDeviceId = ((RespDevice) obj).getId();
            animatePosition(preMarker.getPosition());
        }
    }

    private void animatePosition(LatLng latLng) {
        Logger.d("motianhu", "mCurDevice: " + mCurDeviceId);
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
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
                animatePosition(marker.getPosition());
            }
        } else {
            LatLng latLng = AMapUtil.wgsToCjg(mActivity, device.getLocation().getLatitude(), device.getLocation().getLongitude());
            marker.setObject(device);
            marker.setPosition(latLng);
        }
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
        isClickLocation = true;
        double la = GaodeLocationManager.getInstance().getLocation()[0];
        double lo = GaodeLocationManager.getInstance().getLocation()[1];
        if (CommonUtils.isInValidLatln(la, lo)) {
            GaodeLocationManager.getInstance().refreshLocation();
        } else {
            onLocation(CommonLocationListener.CLICK_LOCATION, la, lo);
        }
    }

    @Override
    public void onLocation(int type, double la, double lo) {
        if(aMap == null) {
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

    private void createPhoneMarker(LatLng latLng) {
        MarkerOptions markerOption = new MarkerOptions().title("PhonePositino").snippet("DefaultMarker");
        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mylocation)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.position(latLng);
        mPhoneMarker = aMap.addMarker(markerOption);
    }
}
