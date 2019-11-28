package com.smona.gpstrack.fence.bean;

import com.smona.gpstrack.db.table.Fence;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 5:15 PM
 */
public class FenceBean extends Fence {

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

    public void copyValue(FenceBean fenceBean) {
        this.setName(fenceBean.getName());
        this.setColor(fenceBean.getColor());
        this.setLatitude(fenceBean.getLatitude());
        this.setLongitude(fenceBean.getLongitude());
        this.setRadius(fenceBean.getRadius());
        this.setStatus(fenceBean.getStatus());

        this.setDevicePlatformIds(fenceBean.getDevicePlatformIds());
        this.setEntryAlarm(fenceBean.getEntryAlarm());
        this.setLeaveAlarm(fenceBean.getLeaveAlarm());
    }
}
