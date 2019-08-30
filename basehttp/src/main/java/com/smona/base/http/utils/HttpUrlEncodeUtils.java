package com.smona.base.http.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


public final class HttpUrlEncodeUtils {

    /**
     * KEY_URL 编码
     * @param input 要编码的字符
     * @return 编码为 UTF-8 的字符串
     */
    public static String urlEncode(final String input) {
        try{
            return URLEncoder.encode(input, "UTF-8");
        }catch (UnsupportedEncodingException e){
            throw new IllegalArgumentException("Unsupported Encoding Exception", e);
        }

    }

    /**
     * KEY_URL 编码
     * @param input   要编码的字符
     * @param charset 字符集
     * @return 编码为字符集的字符串
     */
    public static String urlEncode(final String input, final String charset) {
        try {
            return URLEncoder.encode(input, charset);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unsupported Encoding Exception", e);
        }
    }

    /**
     * KEY_URL 解码
     * @param input 要解码的字符串
     * @return KEY_URL 解码后的字符串
     */
    public static String urlDecode(final String input) {
        try {
            return URLDecoder.decode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unsupported Encoding Exception", e);
        }
    }

    /**
     * KEY_URL 解码
     * @param input   要解码的字符串
     * @param charset 字符集
     * @return KEY_URL 解码为指定字符集的字符串
     */
    public static String urlDecode(final String input, final String charset) {
        try {
            return URLDecoder.decode(input, charset);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unsupported Encoding Exception", e);
        }
    }

}
