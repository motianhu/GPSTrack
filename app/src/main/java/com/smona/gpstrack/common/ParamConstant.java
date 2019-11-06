package com.smona.gpstrack.common;

import com.amap.api.maps.model.LatLng;
import com.smona.gpstrack.R;

import java.util.HashMap;
import java.util.Map;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 2:56 PM
 */
public class ParamConstant {
    //locale
    public static final String LOCALE_ZH_CN = "zh_CN";
    public static final String LOCALE_ZH_TW = "zh_TW";
    public static final String LOCALE_EN = "en";

    public static final Map<String, Integer> LANUAGEMAP = new HashMap<>();

    static {
        LANUAGEMAP.put(LOCALE_ZH_CN, R.string.jianti);
        LANUAGEMAP.put(LOCALE_ZH_TW, R.string.fanti);
        LANUAGEMAP.put(LOCALE_EN, R.string.english);
    }

    //dateformat
    public static final String DATE_FORMAT_DDMMYY = "dd/mm/yyyy";
    public static final String DATE_FORMAT_MMDDYY = "mm/dd/yyyy";
    public static final String DATE_FORMAT_DDYYMM = "yyyy/mm/dd";
    public static final String DATE_FORMAT_DDMMYY_1 = "dd-mm-yyyy";
    public static final String DATE_FORMAT_MMDDYY_1 = "mm-dd-yyyy";
    public static final String DATE_FORMAT_DDYYMM_1 = "yyyy-mm-dd";
    public static final String DATE_FORMAT_DDMMYY_2 = "dd.mm.yyyy";
    public static final String DATE_FORMAT_MMDDYY_3 = "mm.dd.yyyy";
    public static final String DATE_FORMAT_DDYYMM_4 = "yyyy.mm.dd";

    //map
    public static final String MAP_AMAP = "amap";
    public static final String MAP_GOOGLE = "google";

    public static final Map<String, Integer> MAPMAP = new HashMap<>();

    static {
        MAPMAP.put(MAP_AMAP, R.string.gaodemap);
        MAPMAP.put(MAP_GOOGLE, R.string.googlemap);
    }


    //timezone
    public static final String TIME_ZONE_HK = "Asia/Hong_Kong";
    public static final Double DEFAULT_POS_LA = 22.293849;
    public static final Double DEFAULT_POS_LO = 114.1703229;
    public static final LatLng DEFAULT_POS = new LatLng(22.293849,114.1703229);
}
