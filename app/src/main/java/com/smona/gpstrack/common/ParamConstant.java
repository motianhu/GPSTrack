package com.smona.gpstrack.common;

import com.amap.api.maps.model.LatLng;
import com.smona.gpstrack.R;

import java.util.HashMap;
import java.util.Map;

/**
 * description:
 *  常量参数类
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 2:56 PM
 */
public class ParamConstant {
    //locale
    public static final String LOCALE_ZH_CN = "zh_CN";
    public static final String LOCALE_ZH_HK = "zh_HK";
    public static final String LOCALE_ZH_TW = "zh_TW";
    public static final String LOCALE_EN = "en";

    public static final Map<String, Integer> LANUAGEMAP = new HashMap<>();

    static {
        LANUAGEMAP.put(LOCALE_ZH_CN, R.string.jianti);
        LANUAGEMAP.put(LOCALE_ZH_TW, R.string.fanti);
        LANUAGEMAP.put(LOCALE_EN, R.string.english);
    }

    //服务器没有返回语言前的，与系统语言进行匹配的MAP
    public static final Map<String, String> SYSLANUAGEMAP = new HashMap<>();
    static {
        SYSLANUAGEMAP.put(LOCALE_ZH_CN, LOCALE_ZH_CN);
        SYSLANUAGEMAP.put(LOCALE_ZH_HK, LOCALE_ZH_TW);
        SYSLANUAGEMAP.put(LOCALE_ZH_TW, LOCALE_ZH_TW);
        SYSLANUAGEMAP.put(LOCALE_EN, LOCALE_EN);
    }

    //map
    public static final String MAP_AMAP = "amap";
    public static final String MAP_GOOGLE = "google";

    public static final Map<String, Integer> MAPMAP = new HashMap<>();

    static {
        MAPMAP.put(MAP_AMAP, R.string.gaodemap);
        MAPMAP.put(MAP_GOOGLE, R.string.googlemap);
    }


    //timezone。默认坐标点，原始坐标，高德地图要做转换
    public static final Double DEFAULT_POS_LA = 22.293849;
    public static final Double DEFAULT_POS_LO = 114.1703229;
    public static final LatLng DEFAULT_POS = new LatLng(22.293849,114.1703229);
}
