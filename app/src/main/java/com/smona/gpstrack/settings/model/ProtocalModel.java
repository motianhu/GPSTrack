package com.smona.gpstrack.settings.model;

import com.smona.gpstrack.common.GpsFixedBuilder;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

public class ProtocalModel {
    public void requestTermCondition(UrlBean urlBean, OnResultListener<String> listener) {
        HttpCallbackProxy<String> httpCallbackProxy = new HttpCallbackProxy<String>(listener) {
        };
        String api = String.format(BusinessHttpService.TNC, urlBean.getLocale());
        new GpsFixedBuilder<String>(GpsFixedBuilder.REQUEST_GET, api).requestData(httpCallbackProxy);
    }
}
