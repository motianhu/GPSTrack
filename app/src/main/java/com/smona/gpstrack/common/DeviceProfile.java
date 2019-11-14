package com.smona.gpstrack.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.smona.gpstrack.util.AppContext;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/18/19 6:48 PM
 */
public class DeviceProfile {

    public final static String UNKNOW = "unknow";
    private volatile static String sImei = UNKNOW;
    private volatile static String sImei1 = UNKNOW;

    public static String getIMEI() {
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

    public static String getIMEI1() {
        if (!UNKNOW.equals(sImei1)) {
            return sImei1;
        }
        try {
            TelephonyManager phone = (TelephonyManager) AppContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(AppContext.getAppContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return UNKNOW;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sImei1 = phone.getImei();
            }
        } catch (Exception ignored) {
        }
        return sImei1;
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
