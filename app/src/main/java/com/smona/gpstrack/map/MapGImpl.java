package com.smona.gpstrack.map;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.smona.gpstrack.common.ParamConstant;

public class MapGImpl implements IMap {
    public static void initMap(AMap aMap) {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(ParamConstant.DEFAULT_POS));
    }
}
