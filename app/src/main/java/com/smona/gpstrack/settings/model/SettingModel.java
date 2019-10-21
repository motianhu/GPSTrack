package com.smona.gpstrack.settings.model;

import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.param.ConfigInfo;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

public class SettingModel {
    public void requestViewAccount(UrlBean urlBean, OnResultListener<ConfigInfo> listener) {
        HttpCallbackProxy<ConfigInfo> httpCallbackProxy = new HttpCallbackProxy<ConfigInfo>(listener) {
        };
        String api = String.format(BusinessHttpService.ACCOUNT, urlBean.getLocale());
        new GpsDynamicBuilder<ConfigInfo>(GpsDynamicBuilder.REQUEST_GET, api).requestData(httpCallbackProxy);
    }



    public void switchLanuage() {

    }

    public void switchMap() {

    }

    public void switchDateFormat() {

    }

    public void switchTimeZone() {

    }
}
