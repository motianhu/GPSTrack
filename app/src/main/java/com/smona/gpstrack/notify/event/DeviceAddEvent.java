package com.smona.gpstrack.notify.event;

import com.smona.gpstrack.db.table.Device;

public class DeviceAddEvent {
    private Device device;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
