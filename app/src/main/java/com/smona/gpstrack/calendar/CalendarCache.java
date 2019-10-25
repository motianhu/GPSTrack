package com.smona.gpstrack.calendar;

import android.content.Context;


import com.smona.gpstrack.calendar.model.DayTimeInfo;

import java.util.Calendar;

public class CalendarCache {
    public static DayTimeInfo sStartDay;
    public static DayTimeInfo sStopDay;
    public static DayTimeInfo sTheOnlyChooseDay;

    public static void initDay() {
        Calendar cal = Calendar.getInstance();
        if(sTheOnlyChooseDay == null) {
            sTheOnlyChooseDay = new DayTimeInfo(cal.get(Calendar.DATE), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR), -1);
        }

        if(sStartDay == null) {
            sStartDay = new DayTimeInfo(cal.get(Calendar.DATE), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR), -1);
        }

        if(sStopDay == null) {
            cal.add(Calendar.DATE, 1);
            sStopDay = new DayTimeInfo(cal.get(Calendar.DATE), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR), -1);
        }
    }
}
