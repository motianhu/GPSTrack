package com.smona.http.wrapper;

public class BusinessHttpCode {
    public static boolean isSuccessful(String code) {
        return "00000".equals(code) || "0".equals(code) || "000000".equals(code);
    }
}
