package com.smona.gpstrack.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 10/10/19 1:50 PM
 */
public class TimeStamUtil {
    public static String timeStampToDate(long timeStam) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeStam));
    }

    public static List<String> getTimeZone() {
        String[] ids = TimeZone.getAvailableIDs();
        List<String> resultList = Arrays.asList(ids);
        return resultList;
    }

    public static long getBeforeByHourTime(int ihour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
        long time = calendar.getTime().getTime();
        return time;
    }

    public static long getToday0() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        long tt = calendar.getTime().getTime();
        return tt;
    }
}
