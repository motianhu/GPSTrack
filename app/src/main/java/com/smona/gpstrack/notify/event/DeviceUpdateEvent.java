package com.smona.gpstrack.notify.event;

import com.smona.gpstrack.db.table.Device;

/**
 * 设备更新消息体
 */
public class DeviceUpdateEvent {
    private Device device;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
