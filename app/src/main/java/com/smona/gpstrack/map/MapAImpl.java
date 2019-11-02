package com.smona.gpstrack.map;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.util.Constant;

public class MapAImpl implements IMap {

    public static void initMap(AMap aMap, LatLng latLng) {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
        if (ParamConstant.LOCALE_EN.equals(ConfigCenter.getInstance().getConfigInfo().getLocale())) {
            aMap.setMapLanguage(AMap.ENGLISH);
        } else {
            aMap.setMapLanguage(AMap.CHINESE);
        }
    }

    public static Circle drawFence(AMap aMap, LatLng latLng, int radius) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.destination));
        markerOptions.position(latLng);
        aMap.addMarker(markerOptions);
        return  aMap.addCircle(new CircleOptions().
                center(latLng).
                radius(radius).
                strokeWidth(15));
    }
}
