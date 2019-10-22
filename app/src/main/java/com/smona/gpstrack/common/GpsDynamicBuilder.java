package com.smona.gpstrack.common;

import com.smona.gpstrack.common.param.AccountCenter;
import com.smona.http.business.GpsBuilder;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 4:03 PM
 */
public class GpsDynamicBuilder<R> extends GpsBuilder<R> {
    public GpsDynamicBuilder(int type, String path) {
        super(type, path);
        addHeader("x-api-key", AccountCenter.getInstance().getAccountInfo().getApiKey());
    }
}
