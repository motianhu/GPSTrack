package com.smona.gpstrack.notify;

import org.greenrobot.eventbus.EventBus;

public class NotifyCenter {
    private NotifyCenter(){}

    private static class NotifyCenterHolder {
        private static NotifyCenter notifyCenter = new NotifyCenter();
    }

    public NotifyCenter getInstance() {
        return NotifyCenterHolder.notifyCenter;
    }

    public void registerListener(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public void unRegisterListener(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public void postEvent(Object event) {
        EventBus.getDefault().post(event);
    }
}
