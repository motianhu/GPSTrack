package com.smona.gpstrack.device.bean.req;

import com.smona.gpstrack.common.bean.req.UrlBean;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 2:08 PM
 */
public class ReqViewDevice extends UrlBean {
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
