package com.smona.gpstrack.device.bean.req;

import com.smona.gpstrack.common.bean.req.UrlBean;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 2:08 PM
 */
public class ReqShareDevice extends UrlBean {
    private String deviceId;
    private String email;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
