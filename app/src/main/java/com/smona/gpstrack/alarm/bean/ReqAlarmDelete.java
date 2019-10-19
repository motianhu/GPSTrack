package com.smona.gpstrack.alarm.bean;

import com.smona.gpstrack.common.bean.req.UrlBean;

public class ReqAlarmDelete extends UrlBean {
    private String alarmId;

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }
}
