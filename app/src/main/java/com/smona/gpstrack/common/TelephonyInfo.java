package com.smona.gpstrack.common;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 手机IMEI获取类
 */
public class TelephonyInfo {
    private String imeiSIM1;
    private String imeiSIM2;
    private String imeiSIM3;
    private boolean isSIM1Ready;
    private boolean isSIM2Ready;

    public String getImeiSIM1() {
        return imeiSIM1;
    }

    public String getImeiSIM3() {
        if (imeiSIM1.equals(imeiSIM3) || imeiSIM2.equals(imeiSIM3)) {
            return "";
        } else {
            return imeiSIM3;
        }
    }

    public String getImeiSIM2() {
        return imeiSIM2;
    }

    public boolean isSIM1Exists() {
        return isSIM1Ready;
    }

    public boolean isSIM2Exists() {
        return isSIM2Ready;
    }

    public boolean isDualSIM() {
        boolean ret = true;
        try {
            if (imeiSIM2 == null) {
                ret = false;
            } else if (imeiSIM2.isEmpty()) {
                ret = false;
            } else if (imeiSIM1!=null && imeiSIM2!=null && imeiSIM1.equalsIgnoreCase(imeiSIM2)) {
                ret = false; // equals IMEI means only 1 slot
            }
            return ret;
        } catch (Exception e) {
            return false;
        }
    }
    /*
        private TelephonyInfo() {
        }
    */
    @SuppressLint("MissingPermission")
    public TelephonyInfo(Context context){
/*
        if(telephonyInfo == null) {

            telephonyInfo = new TelephonyInfo(context);
*/
        TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));

        this.imeiSIM1 = telephonyManager.getDeviceId();
        this.imeiSIM2 = null;

        try {
            this.imeiSIM1 = getDeviceIdBySlot(context, "getDeviceIdGemini", 0);
            this.imeiSIM2 = getDeviceIdBySlot(context, "getDeviceIdGemini", 1);
            this.imeiSIM3 = getDeviceIdBySlot(context, "getDeviceIdGemini", 2);
//                Log.d("dgnss", "SIM1: " + this.imeiSIM1);
//                Log.d("dgnss", "SIM2: " +this.imeiSIM2);
//                Log.d("dgnss", "SIM3: " +this.imeiSIM3);
        } catch (GeminiMethodNotFoundException e) {
            //e.printStackTrace();

            try {
                this.imeiSIM1 = getDeviceIdBySlot(context, "getDeviceId", 0);
                this.imeiSIM2 = getDeviceIdBySlot(context, "getDeviceId", 1);
                this.imeiSIM3 = getDeviceIdBySlot(context, "getDeviceId", 2);
//                    Log.d("dgnss", "SIM1: " + this.imeiSIM1);
//                    Log.d("dgnss", "SIM2: " +this.imeiSIM2);
//                    Log.d("dgnss", "SIM3: " +this.imeiSIM3);
            } catch (GeminiMethodNotFoundException e1) {
                //Call here for next manufacturer's predicted method name if you wish
                //e1.printStackTrace();
            }
        }

        this.isSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
        this.isSIM2Ready = false;

        try {
            this.isSIM1Ready = getSIMStateBySlot(context, "getSimStateGemini", 0);
            this.isSIM2Ready = getSIMStateBySlot(context, "getSimStateGemini", 1);
        } catch (GeminiMethodNotFoundException e) {
            //e.printStackTrace();

            try {
                this.isSIM1Ready = getSIMStateBySlot(context, "getSimState", 0);
                this.isSIM2Ready = getSIMStateBySlot(context, "getSimState", 1);
            } catch (GeminiMethodNotFoundException e1) {
                //Call here for next manufacturer's predicted method name if you wish
                //e1.printStackTrace();
            }
        }

        // 20190201 Fixed same IMEI both in SIM1 & SIM2
        if (this.imeiSIM1!=null && this.imeiSIM1!=null) {
            if (this.imeiSIM1.equalsIgnoreCase(this.imeiSIM2)) {
                this.imeiSIM2 = null;
                this.isSIM2Ready = false;
            }
        }
//        }

        //return telephonyInfo;
    }

    private static String getDeviceIdBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

        String imei = null;

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try{

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);

            if(ob_phone != null){
                imei = ob_phone.toString();

            }
        } catch (NoSuchMethodException e) {
            // Enhancement 201903 No print log
            throw new GeminiMethodNotFoundException(predictedMethodName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return imei;
    }

    private static  boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

        boolean isReady = false;

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try{

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

            if(ob_phone != null){
                int simState = Integer.parseInt(ob_phone.toString());
                if(simState == TelephonyManager.SIM_STATE_READY){
                    isReady = true;
                }
            }
        } catch (NoSuchMethodException e) {
            throw new GeminiMethodNotFoundException(predictedMethodName);
        } catch (Exception e) {
            // Print log for other exception
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return isReady;
    }


    private static class GeminiMethodNotFoundException extends Exception {

        private static final long serialVersionUID = -996812356902545308L;

        public GeminiMethodNotFoundException(String info) {
            super(info);
        }
    }

}
