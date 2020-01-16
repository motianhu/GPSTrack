package com.smona.gpstrack.datacenter;

import com.smona.gpstrack.device.bean.RespDevice;

import java.util.List;

/**
 * 设备变更
 */
public interface IDeviceChangeListener {
    void onChangeListener(List<RespDevice> deviceList);
}
