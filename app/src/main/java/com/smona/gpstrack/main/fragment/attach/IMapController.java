package com.smona.gpstrack.main.fragment.attach;

import android.support.v4.app.Fragment;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 10/12/19 8:07 AM
 */
public interface IMapController {

    Fragment getMapFragment();
    void location();

    void onResume();
    void onPause();
    void onDestroy();
}
