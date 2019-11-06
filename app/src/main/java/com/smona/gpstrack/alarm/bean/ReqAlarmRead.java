package com.smona.gpstrack.alarm.bean;

import java.util.List;

public class ReqAlarmRead {
    private List<String> readIds;

    public List<String> getReadIds() {
        return readIds;
    }

    public void setReadIds(List<String> readIds) {
        this.readIds = readIds;
    }
}
