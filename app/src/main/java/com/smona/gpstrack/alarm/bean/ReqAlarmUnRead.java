package com.smona.gpstrack.alarm.bean;

import com.smona.gpstrack.common.bean.req.UrlBean;

public class ReqAlarmUnRead extends UrlBean {
    private String devicePlatform;

    public String getDevicePlatform() {
        return devicePlatform;
    }

    public void setDevicePlatform(String devicePlatform) {
        this.devicePlatform = devicePlatform;
    }
}
