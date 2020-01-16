package com.smona.gpstrack.notify;

import org.greenrobot.eventbus.EventBus;

/**
 * 订阅中心。通过EventBus订阅和取消订阅。Event是各个消息体
 */
public class NotifyCenter {
    private NotifyCenter() {
    }

    private static class NotifyCenterHolder {
        private static NotifyCenter notifyCenter = new NotifyCenter();
    }

    public static NotifyCenter getInstance() {
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
