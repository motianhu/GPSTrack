package com.smona.gpstrack.util;

import android.content.Context;
import android.util.SparseIntArray;

import com.smona.gpstrack.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    private static SparseIntArray sparseArray = new SparseIntArray();
    static {
        sparseArray.put(1, R.string.Monday);
        sparseArray.put(2, R.string.Tuesday);
        sparseArray.put(3, R.string.Wednesday);
        sparseArray.put(4, R.string.Thursday);
        sparseArray.put(5, R.string.Friday);
        sparseArray.put(6, R.string.Saturday);
        sparseArray.put(7, R.string.Sunday);
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static String dayToWeek(Context context, int pos) {
        int resId = sparseArray.get(pos);
        return context.getString(resId);
    }

    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
