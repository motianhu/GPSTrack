package com.smona.gpstrack.main.fragment.attach;

import android.support.v4.app.Fragment;

import com.smona.gpstrack.device.bean.RespDevice;

/**
 * description:
 *
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

    void drawDevice(RespDevice device);
}
