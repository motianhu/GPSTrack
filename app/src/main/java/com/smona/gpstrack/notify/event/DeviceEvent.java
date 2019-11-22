package com.smona.gpstrack.notify.event;

public class DeviceEvent {
    public static final int ACTION_ADD = 1;

    public static final int ACTION_UPDATE = 2;

    public static final int ACTION_DEL = 3;

    private int actionType;

    public DeviceEvent(int actionType) {
        this.actionType = actionType;
    }

    public int getActionType() {
        return actionType;
    }
}
