package com.smona.gpstrack.util;

import com.google.gson.Gson;

public class GsonUtil {
    private static Gson gson = new Gson();

    public static <T> T jsonToObj(String gsonString, Class<T> cls) {
        T t = null;
        try {
            t = gson.fromJson(gsonString, cls);
        } catch (Exception e) {
        }
        return t;
    }

    public static <T> String objToJson(T data) {
        try {
            return gson.toJson(data);
        } catch (Exception e) {
        }
        return null;
    }
}
