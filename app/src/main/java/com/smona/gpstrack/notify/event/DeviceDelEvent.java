package com.smona.gpstrack.notify.event;

/**
 * 设备删除消息体
 */
public class DeviceDelEvent {
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
