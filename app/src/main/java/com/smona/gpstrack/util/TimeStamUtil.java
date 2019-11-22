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
    public static String timeStampToDate(long timeStamp) {
        return formatDate(timeStamp, ConfigCenter.getInstance().getConfigInfo().getDateFormat()+ " HH:mm:ss", TimeZone.getTimeZone(ConfigCenter.getInstance().getConfigInfo().getTimeZone()));
    }

    public static String formatDate(long timeStamp, String dateFormatPattern, TimeZone timeZone) {
        Date date = new Date(timeStamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        if (timeZone != null) {
            dateFormat.setTimeZone(timeZone);
        }
        return dateFormat.format(date);
    }

    public static List<String> getTimeZone() {
        String[] ids = TimeZone.getAvailableIDs();
        List<String> resultList = Arrays.asList(ids);
        return resultList;
    }

    private TreeMap<String, String> getTreeMap() {
        TreeMap<String, String> timeZone = new TreeMap<String, String>();
        String[] ids = TimeZone.getAvailableIDs();
        for (String id : ids) {
            timeZone.put(displayTimeZone(TimeZone.getTimeZone(id)), id);
        }
        return timeZone;
    }

    private static String displayTimeZone(TimeZone tz) {

        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes =
                TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours);
        // avoid -4:-30 issue
        minutes = Math.abs(minutes);

        String result = "";
        if (hours >= 0) {
            result = String.format("(GMT+%02d:%02d) %s", hours, minutes, tz.getID());
        } else {
            result = String.format("(GMT-%02d:%02d) %s", Math.abs(hours), minutes, tz.getID());
        }

        return result;
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
