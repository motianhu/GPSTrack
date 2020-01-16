package com.smona.gpstrack.notify.event;

/**
 * 删除报警消息体
 */
public class AlarmDelEvent {
    private int uiPos;
    private String alarmId;

    public int getUiPos() {
        return uiPos;
    }

    public void setUiPos(int uiPos) {
        this.uiPos = uiPos;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }
}
