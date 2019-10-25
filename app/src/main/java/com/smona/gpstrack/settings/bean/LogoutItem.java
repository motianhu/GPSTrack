package com.smona.gpstrack.settings.bean;

import com.smona.gpstrack.common.bean.req.UrlBean;

public class LogoutItem extends UrlBean {
    private String imei;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
