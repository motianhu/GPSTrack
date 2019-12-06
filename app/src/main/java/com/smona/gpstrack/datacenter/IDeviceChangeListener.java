package com.smona.gpstrack.datacenter;

import com.smona.gpstrack.device.bean.RespDevice;

import java.util.List;

public interface IDeviceChangeListener {
    void onChangeListener(List<RespDevice> deviceList);
}
