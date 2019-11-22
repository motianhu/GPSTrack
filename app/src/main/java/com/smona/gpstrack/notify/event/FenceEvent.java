package com.smona.gpstrack.notify.event;

import com.smona.gpstrack.db.table.Fence;

public class FenceEvent {
    public static final int ACTION_ADD = 1;

    public static final int ACTION_UPDATE = 2;

    public static final int ACTION_DEL = 3;

    private int actionType;
    private Fence fence;

    public FenceEvent(int actionType, Fence fence) {
        this.actionType = actionType;
        this.fence = fence;
    }

    public int getActionType() {
        return actionType;
    }

    public Fence getFence() {
        return fence;
    }
}
