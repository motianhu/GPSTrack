package com.smona.gpstrack.notify.event;

public class DeviceEvent {
    public static final int ACTION_ADD = 1;

    public static final int ACTION_UPDATE = 2;

    public static final int ACTION_DEL = 3;

    private int actionType;
    private String deviceId;

    public DeviceEvent(int actionType, String deviceId) {
        this.actionType = actionType;
        this.deviceId = deviceId;
    }

    public int getActionType() {
        return actionType;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
