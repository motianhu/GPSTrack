package com.smona.gpstrack.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.smona.gpstrack.util.AppContext;
import com.smona.gpstrack.util.SPUtils;

import java.util.ArrayList;
import java.util.UUID;

/**
 * description:
 *  获取设备信息类
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/18/19 6:48 PM
 */
public class DeviceProfile {

    public final static String UNKNOW = "unknow";
    private volatile static String sImei = UNKNOW;
    private volatile static String sImei1 = UNKNOW;

    private volatile static String STR_UUID = null;

    /**
     * 获取UUID。如果有存SP，直接返回；如果没有存，随机生成一个，持久化并返回。
     * @return UUID
     */
    private static String getStrUuid() {
        STR_UUID = (String)SPUtils.get("UUID", "");
        if(TextUtils.isEmpty(STR_UUID)) {
            STR_UUID = UUID.randomUUID().toString();
            SPUtils.put("UUID", STR_UUID);
        }
        return STR_UUID;
    }

    /**
     * 适配接口。IMEI在Android 10已经不可取。
     * @return UUID
     */
    public static String getIMEI() {
        return getStrUuid();
    }

    private static String getSingleIMEI() {
        if (!UNKNOW.equals(sImei)) {
            return sImei;
        }
        try {
            TelephonyManager phone = (TelephonyManager) AppContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(AppContext.getAppContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return UNKNOW;
            }
            sImei = phone.getDeviceId();
        } catch (Exception ignored) {
        }
        return sImei;
    }

    private static String getIMEI1() {
        if (!UNKNOW.equals(sImei1)) {
            return sImei1;
        }
        try {
            TelephonyManager phone = (TelephonyManager) AppContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(AppContext.getAppContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return UNKNOW;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sImei1 = phone.getImei(1);
            }
        } catch (Exception ignored) {
        }
        return sImei1;
    }

    private static ArrayList<String> getAllIMEI() {
        ArrayList<String> imeis = new ArrayList<>();

        TelephonyInfo telephonyInfo = new TelephonyInfo(AppContext.getAppContext());
        String imei1 = telephonyInfo.getImeiSIM1();
        String imei2 = telephonyInfo.getImeiSIM2();
        String imei3 = telephonyInfo.getImeiSIM3();

        if (imei1!=null) {
            if (imei1.length() > 0) {
                if (isValidIMEI(imei1)) imeis.add(imei1);
            }
        }
        if (imei2!=null) {
            if (imei2.length() > 0) {
                if (isValidIMEI(imei2)) imeis.add(imei2);
            }
        }
        if (imei3!=null) {
            if (imei3.length() > 0) {
                if (isValidIMEI(imei3)) imeis.add(imei3);
            }
        }
        return imeis;
    }

    // 201907
    // 1. MUST Numeric
    // 2. 000000000 Invalid
    // https://stackoverflow.com/questions/25229648/is-it-possible-to-validate-imei-number
    private static boolean isValidIMEI(String imei) {
        try {
            long val = Long.parseLong(imei);
            if (val == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }


    public static String getOS() {
        return "Android";
    }

    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }
}
