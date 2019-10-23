package com.smona.gpstrack.device.bean.req;

public class ReqDeviceAlarm<T> {
    private T configs;

    public T getConfigs() {
        return configs;
    }

    public void setConfigs(T configs) {
        this.configs = configs;
    }
}
