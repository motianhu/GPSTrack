package com.smona.gpstrack.data;

import com.smona.gpstrack.device.bean.RespDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 10/11/19 1:37 PM
 */
public class MemoryDeviceManager {

    private List<RespDevice> deviceList = new ArrayList<>();

    private MemoryDeviceManager() {
    }

    private static class MemoryHolder {
        private static MemoryDeviceManager sMemoryDevice = new MemoryDeviceManager();
    }

    public static MemoryDeviceManager getInstance() {
        return MemoryHolder.sMemoryDevice;
    }

    public void addDeviceList(List<RespDevice> list) {
        deviceList.addAll(list);
    }

    public void addDevice(RespDevice device) {
        deviceList.add(device);
    }

    public RespDevice getDevice(int index) {
        if (index < deviceList.size()) {
            return deviceList.get(index);
        } else {
            return null;
        }
    }
}
