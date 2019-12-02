package com.smona.gpstrack.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.SparseIntArray;

import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.db.DeviceDecorate;
import com.smona.gpstrack.db.FenceDecorate;
import com.smona.gpstrack.db.LocationDecorate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    private static SparseIntArray sparseArray = new SparseIntArray();
    static {

        sparseArray.put(1, R.string.Sunday);
        sparseArray.put(2, R.string.Monday);
        sparseArray.put(3, R.string.Tuesday);
        sparseArray.put(4, R.string.Wednesday);
        sparseArray.put(5, R.string.Thursday);
        sparseArray.put(6, R.string.Friday);
        sparseArray.put(7, R.string.Saturday);
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

    public static void showToastByFilter(int stateCode, String msg) {
        if(stateCode == 403) {
            return;
        }
        ToastUtil.showShort(msg);
    }

    public static void sendCloseAllActivity(Context context) {
        Intent closeAllIntent = new Intent(BaseActivity.ACTION_BASE_ACTIVITY);
        closeAllIntent.putExtra(BaseActivity.ACTION_BASE_ACTIVITY_EXIT_KEY, BaseActivity.ACTION_BASE_ACTIVITY_EXIT_VALUE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(closeAllIntent);
    }

    public static void clearAllCache() {
        new DeviceDecorate().deleteAll();
        new FenceDecorate<>().deleteAll();
        new LocationDecorate().deleteAll();
    }
}
