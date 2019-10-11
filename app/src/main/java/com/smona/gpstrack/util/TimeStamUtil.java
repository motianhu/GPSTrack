package com.smona.gpstrack.util;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}
