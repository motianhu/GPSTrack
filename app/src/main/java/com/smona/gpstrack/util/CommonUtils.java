package com.smona.gpstrack.util;

import android.content.Context;
import android.util.SparseIntArray;

import com.smona.gpstrack.R;

import java.util.List;

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
}
