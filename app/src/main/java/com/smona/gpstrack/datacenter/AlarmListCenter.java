package com.smona.gpstrack.datacenter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AlarmListCenter {

    private AlarmListCenter() {
    }

    private static class ParamHolder {
        private static AlarmListCenter dataCenter = new AlarmListCenter();
    }

    public static AlarmListCenter getInstance() {
        return ParamHolder.dataCenter;
    }

    private List<Alarm> alarmList = new CopyOnWriteArrayList<>();

    public void addAlarmList(List<Alarm> alarms) {
        alarmList.addAll(alarms);
    }

    public void clearAlarmList() {
        alarmList.clear();
    }

    public void removeAlarm(String alarmId) {
        for (Alarm alarm : alarmList) {
            if (alarm.getId().equals(alarmId)) {
                alarmList.remove(alarm);
                break;
            }
        }
    }
}
