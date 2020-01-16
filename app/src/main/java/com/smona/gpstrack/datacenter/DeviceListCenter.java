package com.smona.gpstrack.datacenter;

import android.text.TextUtils;

import com.smona.gpstrack.device.bean.RespDevice;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 设备数据中心。单例模式。
 */
public class DeviceListCenter {

    /**
     * 系统定时轮询设备，在导航页面，设备的位置可能变化，在此订阅。
     */
    private IDeviceChangeListener deviceChangeListener;

    /**
     * 内存设备数据。地图/导航/历史路线使用。
     */
    private ConcurrentLinkedQueue<RespDevice> deviceList = new ConcurrentLinkedQueue<>();

    private DeviceListCenter() {
    }

    private static class ParamHolder {
        private static DeviceListCenter dataCenter = new DeviceListCenter();
    }

    public static DeviceListCenter getInstance() {
        return ParamHolder.dataCenter;
    }

    /**
     * 获取所有设备
     * @return
     */
    public ConcurrentLinkedQueue<RespDevice> getDeviceList() {
        return deviceList;
    }

    /**
     * 新设备加入到内存。并通知设备变更
     * @param devices
     */
    public void addData(List<RespDevice> devices) {
        if (deviceChangeListener != null) {
            deviceChangeListener.onChangeListener(devices);
        }
        deviceList.addAll(devices);
    }

    /**
     * 通过设备ID从内存中移除某个设备
     * @param deviceId
     */
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

    /**
     * 清除所有设备
     */
    public void clearAll() {
        deviceList.clear();
    }

    /**
     * 订阅设备信息变化
     * @param deviceChangeListener
     */
    public void registerDeviceChangeListener(IDeviceChangeListener deviceChangeListener) {
        this.deviceChangeListener = deviceChangeListener;
    }

    /**
     * 取消订阅
     */
    public void removeChangeListener() {
        this.deviceChangeListener = null;
    }
}
