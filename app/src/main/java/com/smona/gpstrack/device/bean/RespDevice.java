package com.smona.gpstrack.device.bean;

import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.db.table.Location;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 11:41 AM
 */
public class RespDevice extends Device {
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "RespDevice{" +
                "location=" + location +
                ", id=" + getId() +
                ", name=" + getName() +
                ", status=" + getStatus() +
                '}';
    }
}
