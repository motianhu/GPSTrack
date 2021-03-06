package com.smona.gpstrack.device.bean.req;

import com.smona.gpstrack.db.table.Device;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 3:05 PM
 */
public class ReqDeviceDetail extends Device {
    public static final int DEVICE_NAME = 0;
    public static final int BAT_ALARM = 1;
    public static final int SOS_ALARM = 2;
    public static final int TMPR_ALARM = 3;
    public static final int VOCMON_ALARM = 4;
    public static final int DEVICE_PHONES = 5;

    private String owner;
    private boolean isOwner;
    private DeviceConfig configs;
    private List<ShareInfo> shares;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public DeviceConfig getConfigs() {
        return configs;
    }

    public void setConfigs(DeviceConfig configs) {
        this.configs = configs;
    }

    public List<ShareInfo> getShares() {
        return shares;
    }

    public void setShares(List<ShareInfo> shares) {
        this.shares = shares;
    }
}
