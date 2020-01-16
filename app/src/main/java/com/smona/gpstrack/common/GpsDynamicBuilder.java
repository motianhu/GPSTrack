package com.smona.gpstrack.common;

import com.smona.gpstrack.common.param.AccountCenter;
import com.smona.http.business.GpsBuilder;

/**
 * description:
 *  网络请求建造器。参数是动态APIKEY时使用，参考BusinessHttpService。
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
