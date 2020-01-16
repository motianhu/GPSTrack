package com.smona.gpstrack.notify.event;

import com.smona.gpstrack.fence.bean.FenceBean;

/**
 * 电子围栏添加消息体
 */
public class FenceAddEvent {
    private FenceBean addFence;

    public FenceBean getAddFence() {
        return addFence;
    }

    public void setAddFence(FenceBean addFence) {
        this.addFence = addFence;
    }
}
