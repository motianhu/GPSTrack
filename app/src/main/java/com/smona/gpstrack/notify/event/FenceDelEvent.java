package com.smona.gpstrack.notify.event;

/**
 * 删除电子围栏消息体
 */
public class FenceDelEvent {
    private String  fenceId;

    public String getFenceId() {
        return fenceId;
    }

    public void setFenceId(String fenceId) {
        this.fenceId = fenceId;
    }
}
