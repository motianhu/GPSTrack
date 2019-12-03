package com.smona.google;

import com.google.android.gms.maps.model.LatLng;

public interface GoogleLocationListener {
    void onLocation(LatLng latLng);
}
