package com.smona.gpstrack.alarm.bean;

import com.smona.gpstrack.common.bean.req.PageUrlBean;

public class ReqAlarmList extends PageUrlBean {
    private long date_from;

    public long getDate_from() {
        return date_from;
    }

    public void setDate_from(long date_from) {
        this.date_from = date_from;
    }
}
