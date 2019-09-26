package com.smona.gpstrack.register.bean;

import com.smona.gpstrack.common.bean.req.UrlBean;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 4:29 PM
 */
public class VerifyUrlBean extends UrlBean {
    private String email;
    private String imei;
    private String code;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
