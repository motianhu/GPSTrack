package com.smona.gpstrack.common;

import com.smona.http.business.GpsBuilder;
import com.smona.http.config.LoadConfig;

/**
 * description:
 * 固定网络建造器。参数是固定APIKEY。参考BusinessHttpService
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 4:01 PM
 */
public class GpsFixedBuilder<R> extends GpsBuilder<R> {

    public GpsFixedBuilder(int type, String path) {
        super(type, path);
        addHeader("x-api-key", LoadConfig.appConfig.getApiKey());
    }
}
