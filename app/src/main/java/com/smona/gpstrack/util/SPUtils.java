package com.smona.gpstrack.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {

    public static final String SP_FILE_NAME = "gpstrack";

    /**
     * 保存数据
     */
    public static boolean put(Context context, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (value == null) {
            value = "";
        }
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Double) {
            editor.putLong(key, Double.doubleToLongBits((Double) value));//Double以Long的形式保存，get的时候需转换
        } else {
            editor.putString(key, value.toString());
        }
        return editor.commit();
    }

    /**
     * 获取数据
     */
    public static Object get(Context context, String key, Object defValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        if (defValue instanceof String) {
            return sp.getString(key, (String) defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Long) {
            return sp.getLong(key, (Long) defValue);
        } else if (defValue instanceof Double) {
            return sp.getLong(key, Double.doubleToLongBits((Double) defValue));//Double以Long的形式保存，get的时候需转换
        }
        return null;
    }
}
