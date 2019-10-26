package com.smona.gpstrack.fence.bean;

import com.smona.gpstrack.db.table.Fence;

import java.io.Serializable;
import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 5:15 PM
 */
public class FenceBean extends Fence implements Serializable {

    public static final String STATUS_ENABLE = "a";
    public static final String STATUS_DISABLE = "i";

    private List<TimeAlarm> entryAlarm;
    private List<TimeAlarm> leaveAlarm;
    private List<String> devicePlatformIds;

    public List<TimeAlarm> getEntryAlarm() {
        return entryAlarm;
    }

    public void setEntryAlarm(List<TimeAlarm> entryAlarm) {
        this.entryAlarm = entryAlarm;
    }

    public List<TimeAlarm> getLeaveAlarm() {
        return leaveAlarm;
    }

    public void setLeaveAlarm(List<TimeAlarm> leaveAlarm) {
        this.leaveAlarm = leaveAlarm;
    }

    public List<String> getDevicePlatformIds() {
        return devicePlatformIds;
    }

    public void setDevicePlatformIds(List<String> devicePlatformIds) {
        this.devicePlatformIds = devicePlatformIds;
    }
}
