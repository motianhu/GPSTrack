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
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.map.GaodeMapView;
import com.smona.gpstrack.util.AMapUtil;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.logger.Logger;
import com.smona.map.gaode.GaodeLocationManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 10/11/19 3:16 PM
 */
public class AmapFragment extends BaseFragment implements IMapController {

    private MapView mapView;
    private AMap aMap;

    private String mCurDeviceId;
    private Map<String, Marker> deviceMap = new LinkedHashMap<>();
    private Map<String, Circle> circleMap = new LinkedHashMap<>();
    private Map<String, Fence> fenceMap = new LinkedHashMap<>();

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
            GaodeLocationManager.getInstance().init(mActivity);
            GaodeMapView.initMap(aMap, AMapUtil.wgsToCjg(mActivity, ParamConstant.DEFAULT_POS.latitude, ParamConstant.DEFAULT_POS.longitude));
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

        Object obj = nextMarker.getObject();
        if (obj instanceof RespDevice) {
            mCurDeviceId = ((RespDevice) obj).getId();
            animateCameraCurMarker(nextMarker.getPosition());
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
                animateCameraCurMarker(curMarker.getPosition());
            }
        }
    }

    @Override
    public void drawFences(List<Fence> fenceList) {
        for (Fence fence : fenceList) {
            fenceMap.put(fence.getId(), fence);
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
        if (fence == null) {
            return;
        }
        fenceMap.remove(fence.getId());
        fenceMap.put(fence.getId(), fence);
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

        Object obj = preMarker.getObject();
        if (obj instanceof RespDevice) {
            mCurDeviceId = ((RespDevice) obj).getId();
            animateCameraCurMarker(preMarker.getPosition());
        }
    }

    private void animateCameraCurMarker(LatLng latLng) {
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
                animateCameraCurMarker(marker.getPosition());
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
        GaodeLocationManager.getInstance().refreshLocation(this::animateCameraCurMarker);
    }
}
