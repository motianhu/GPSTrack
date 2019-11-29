package com.smona.gpstrack.alarm.model;

import com.smona.gpstrack.alarm.bean.AlarmUnRead;
import com.smona.gpstrack.alarm.bean.ReqAlarmUnRead;
import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

public class AlarmUnReadModel {

    public void requestUnReadCount(ReqAlarmUnRead urlBean, OnResultListener<AlarmUnRead> listener) {
        HttpCallbackProxy<AlarmUnRead> httpCallbackProxy = new HttpCallbackProxy<AlarmUnRead>(listener) {
        };
        String api = String.format(BusinessHttpService.ALERT_UNREAD, urlBean.getLocale(), urlBean.getDevicePlatform());
        new GpsDynamicBuilder<AlarmUnRead>(GpsDynamicBuilder.REQUEST_POST, api).requestData(httpCallbackProxy);
    }
}
