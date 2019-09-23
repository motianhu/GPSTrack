package com.smona.gpstrack.common;

import com.smona.http.business.GpsBuilder;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 4:01 PM
 */
public class GpsFixedBuilder<R> extends GpsBuilder<R> {

    public GpsFixedBuilder(int type, String path) {
        super(type, path);
        addHeader("x-api-key", "0h8a00FSgoQfQ8YTbi4NBkmKxfMtuw6guZ73BGzt");
    }
}
