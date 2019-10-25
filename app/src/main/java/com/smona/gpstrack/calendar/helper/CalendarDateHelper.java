package com.smona.gpstrack.calendar.helper;

import android.content.Context;

import com.smona.gpstrack.R;
import com.smona.gpstrack.calendar.model.DayTimeInfo;
import com.smona.gpstrack.calendar.model.MonthInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDateHelper {

    /**
     * 获取周信息
     *
     * @param position
     * @return
     */
    public static String getWeekStr(Context context, int position) {
        if (context == null) {
            return "";
        }
        int realPosition = position % 7;
        switch (realPosition) {
            case 0:
                return context.getString(R.string.Sunday);
            case 1:
                return context.getString(R.string.Monday);
            case 2:
                return context.getString(R.string.Tuesday);
            case 3:
                return context.getString(R.string.Wednesday);
            case 4:
                return context.getString(R.string.Thursday);
            case 5:
                return context.getString(R.string.Friday);
            case 6:
                return context.getString(R.string.Saturday);
            default:
                return "";

        }
    }

    /**
     * 获取两个日期的时间差
     *
     * @param startDay
     * @param endDay
     * @return
     */
    public static int getOffectDay(DayTimeInfo startDay, DayTimeInfo endDay) {
        int offDay = 0;
        if (startDay == null || endDay == null) {
            return offDay;
        }

        String startDayStr = "";
        String endDayStr = "";

        StringBuilder startDayBuilder = new StringBuilder();
        StringBuilder endDayBuilder = new StringBuilder();
        startDayBuilder.append(startDay.getYear()).append("-").append(startDay.getMonth())
                .append("-").append(startDay.getDay()).append(" 00:00:00");
        endDayBuilder.append(endDay.getYear()).append("-").append(endDay.getMonth())
                .append("-").append(endDay.getDay()).append(" 00:00:00");

        startDayStr = startDayBuilder.toString();
        endDayStr = endDayBuilder.toString();

        DateFormat df = new SimpleDateFormat(DateUtil.dateFormatYMDHMS);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        try {
            c1.setTime(df.parse(startDayStr));
            c2.setTime(df.parse(endDayStr));

            offDay = DateUtil.getOffectDay(c2.getTimeInMillis(), c1.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return offDay;
    }

    public static int getOffectDay(String startDate, String endDate) {
        int offDay = 0;
        DateFormat df = new SimpleDateFormat(DateUtil.dateFormatYMD);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(startDate));
            c2.setTime(df.parse(endDate));

            offDay = DateUtil.getOffectDay(c2.getTimeInMillis(), c1.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return offDay;
    }

    /**
     * 获取当月及当月以后的指定个数的月份信息
     *
     * @return
     */
    public static List<MonthInfo> getMonthInfos(int count) {
        List<MonthInfo> datas = new ArrayList<>();
        if (count <= 0) {
            return datas;
        }

        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        c.add(Calendar.MONTH, 1);
        datas.add(new MonthInfo(currentYear, currentMonth)); //当前月份
        int nextYear = currentYear;
        int nextMonth = currentMonth;
        for (int i = 0; i < count - 1; i++) {
            if (nextMonth == 12) {
                nextYear++;
                nextMonth = 1;
                datas.add(new MonthInfo(nextYear, nextMonth));//下一年的下个月
            } else {
                nextMonth++;
                datas.add(new MonthInfo(nextYear, nextMonth));//同一年的下个月
            }
        }
        return datas;
    }
}
