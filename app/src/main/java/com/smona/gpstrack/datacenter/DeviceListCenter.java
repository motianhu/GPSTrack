package com.smona.gpstrack.datacenter;

import android.text.TextUtils;

import com.smona.gpstrack.device.bean.RespDevice;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DeviceListCenter {

    private IDeviceChangeListener deviceChangeListener;

    private ConcurrentLinkedQueue<RespDevice> deviceList = new ConcurrentLinkedQueue<>();

    private DeviceListCenter() {
    }

    private static class ParamHolder {
        private static DeviceListCenter dataCenter = new DeviceListCenter();
    }

    public static DeviceListCenter getInstance() {
        return ParamHolder.dataCenter;
    }

    public ConcurrentLinkedQueue<RespDevice> getDeviceList() {
        return deviceList;
    }

    public void addData(List<RespDevice> devices) {
        if (deviceChangeListener != null) {
            deviceChangeListener.onChangeListener(devices);
        }
        deviceList.addAll(devices);
    }

    public void removeDevice(String deviceId) {
        if (TextUtils.isEmpty(deviceId)) {
            return;
        }
        for (RespDevice d : deviceList) {
            if (d.getId().equals(deviceId)) {
                deviceList.remove(d);
                break;
            }
        }
    }

    public void clearAll() {
        deviceList.clear();
    }

    public void registerDeviceChangeListener(IDeviceChangeListener deviceChangeListener) {
        this.deviceChangeListener = deviceChangeListener;
    }

    public void removeChangeListener() {
        this.deviceChangeListener = null;
    }
}
