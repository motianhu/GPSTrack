package com.smona.gpstrack.main.fragment.attach;

import android.support.v4.app.Fragment;

import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.device.bean.RespDevice;

import java.util.List;

/**
 * description:
 * 地图控制器
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 10/12/19 8:07 AM
 */
public interface IMapController {

    Fragment getMapFragment();

    void setMapCallback(IMapCallback mapCallback);

    void location();

    void onResume();

    void onPause();

    void onDestroy();

    void drawDevices(List<RespDevice> deviceList);

    void removeDevice(String deviceId);

    void leftDevice();

    void rightDevice();

    void setCurDevice(RespDevice device);


    void setUserVisibleHint(boolean isVisibleToUser);
    void drawFences(List<Fence> fenceList);
    void removeFence(String fenceId);
    void addFence(Fence fence);
    void updateFence(Fence fence);
}
