package com.smona.gpstrack.util;

import com.smona.gpstrack.common.param.ConfigCenter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 10/10/19 1:50 PM
 */
public class TimeStamUtil {

    private static final int ONE_HOUR = 60* 60*1000;

    public static String timeStampToDate(long timeStamp) {
        return formatDate(timeStamp, ConfigCenter.getInstance().getConfigInfo().getDateFormat()+ " HH:mm:ss", TimeZone.getTimeZone(ConfigCenter.getInstance().getConfigInfo().getTimeZone()));
    }

    private static String formatDate(long timeStamp, String dateFormatPattern, TimeZone timeZone) {
        Date date = new Date(timeStamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        if (timeZone != null) {
            dateFormat.setTimeZone(timeZone);
        }
        return dateFormat.format(date);
    }

    //hour
    public static long timeStampForTimeZone(long timeStamp) {
        return formatDate(timeStamp, TimeZone.getTimeZone(ConfigCenter.getInstance().getConfigInfo().getTimeZone()));
    }

    private static long formatDate(long timeStamp, TimeZone timeZone) {
        Date date = new Date(timeStamp);
        Calendar calendar = Calendar.getInstance();
        if (timeZone != null) {
            calendar.setTimeZone(timeZone);
        }
        calendar.setTime(date);
        return calendar.getTime().getTime();
    }

    public static List<String> getTimeZone() {
        String[] ids = TimeZone.getAvailableIDs();
        List<String> resultList = Arrays.asList(ids);
        return resultList;
    }


    public static String getCurTimeZone() {
        return TimeZone.getDefault().getID();
    }

    public static long getBeforeByHourTime(int ihour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour);
        long time = calendar.getTime().getTime();
        return time;
    }

    public static long getToday0() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        long tt = calendar.getTime().getTime();
        return tt;
    }

    //date
    public static long getTimeZoneOffset(long timeStamp) {
        Calendar cal = Calendar.getInstance();
        long times = timeStamp / ONE_HOUR;
        int fromTimeZoneOffset = cal.getTimeZone().getOffset(times);
        cal.setTimeZone(TimeZone.getTimeZone(ConfigCenter.getInstance().getConfigInfo().getTimeZone()));
        long toTimeZoneOffset = cal.getTimeZone().getOffset(times);
        return toTimeZoneOffset - fromTimeZoneOffset;
    }

}
