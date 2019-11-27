package com.smona.gpstrack.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.smona.gpstrack.util.AppContext;

import java.util.ArrayList;

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
                sImei1 = phone.getImei(1);
            }
        } catch (Exception ignored) {
        }
        return sImei1;
    }

    public static ArrayList<String> getAllIMEI() {
        ArrayList<String> imeis = new ArrayList<>();
        TelephonyInfo telephonyInfo = new TelephonyInfo(AppContext.getAppContext());
        String imei1 = telephonyInfo.getImsiSIM1();
        String imei2 = telephonyInfo.getImsiSIM2();

        if (imei1 != null) {
            if (imei1.length() > 0) {
                imeis.add(imei1);
            }
        }
        if (imei2 != null) {
            if (imei2.length() > 0) {
                imeis.add(imei2);
            }
        }
        return imeis;
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
