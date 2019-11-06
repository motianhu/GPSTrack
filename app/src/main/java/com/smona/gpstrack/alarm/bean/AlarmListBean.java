package com.smona.gpstrack.alarm.bean;

import com.smona.gpstrack.common.bean.resp.PageDataBean;
import com.smona.gpstrack.db.table.Alarm;

public class AlarmListBean extends PageDataBean<Alarm> {
    private int ttlUnRead;

    public int getTtlUnRead() {
        return ttlUnRead;
    }

    public void setTtlUnRead(int ttlUnRead) {
        this.ttlUnRead = ttlUnRead;
    }
}
