package com.smona.gpstrack.alarm.bean;

import com.smona.gpstrack.common.bean.req.PageUrlBean;

public class ReqAlarmList extends PageUrlBean {
    private String devicePlatformId;

    public String getDevicePlatformId() {
        return devicePlatformId;
    }

    public void setDevicePlatformId(String devicePlatformId) {
        this.devicePlatformId = devicePlatformId;
    }
}
