package com.smona.gpstrack.device.bean.req;

import com.smona.gpstrack.common.bean.req.PageUrlBean;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/29/19 1:07 PM
 */
public class ReqLocationList extends PageUrlBean {
    private String devicePlatformId;
    private String map;
    private String dateFrom;
    private String dateTo;

    public String getDevicePlatformId() {
        return devicePlatformId;
    }

    public void setDevicePlatformId(String devicePlatformId) {
        this.devicePlatformId = devicePlatformId;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
}
