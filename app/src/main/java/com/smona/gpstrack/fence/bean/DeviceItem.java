package com.smona.gpstrack.fence.bean;

import com.smona.gpstrack.db.table.Device;

import java.io.Serializable;

public class DeviceItem extends Device implements Serializable {
    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public void copy(Device device) {
        this.setId(device.getId());
        this.setName(device.getName());
        this.setExpiryDate(device.getExpiryDate());
        this.setOnlineDate(device.getOnlineDate());
        this.setStatus(device.getStatus());
    }
}
