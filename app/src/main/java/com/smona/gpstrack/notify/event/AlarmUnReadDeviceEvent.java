package com.smona.gpstrack.notify.event;

/**
 * 已读设备报警消息体
 */
public class AlarmUnReadDeviceEvent {
    private int unReadCount;

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }
}
