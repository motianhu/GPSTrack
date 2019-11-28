package com.smona.gpstrack.notify.event;

import com.smona.gpstrack.fence.bean.FenceBean;

public class FenceUpdateEvent {
    private FenceBean updateFence;

    public FenceBean getUpdateFence() {
        return updateFence;
    }

    public void setUpdateFence(FenceBean updateFence) {
        this.updateFence = updateFence;
    }
}
