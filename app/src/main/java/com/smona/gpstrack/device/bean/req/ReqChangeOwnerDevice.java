package com.smona.gpstrack.device.bean.req;

import com.smona.gpstrack.common.bean.req.UrlBean;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 2:08 PM
 */
public class ReqChangeOwnerDevice extends UrlBean {
    private String deviceId;
    private String shareId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }
}
