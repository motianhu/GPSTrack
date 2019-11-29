package com.smona.gpstrack.alarm.model;

import android.text.TextUtils;

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
        if (TextUtils.isEmpty(urlBean.getDevicePlatform())) {
            String api = String.format(BusinessHttpService.ALERT_UNREAD_ALL, urlBean.getLocale());
            new GpsDynamicBuilder<AlarmUnRead>(GpsDynamicBuilder.REQUEST_GET, api).requestData(httpCallbackProxy);
        } else {
            String api = String.format(BusinessHttpService.ALERT_UNREAD, urlBean.getLocale(), urlBean.getDevicePlatform());
            new GpsDynamicBuilder<AlarmUnRead>(GpsDynamicBuilder.REQUEST_GET, api).requestData(httpCallbackProxy);
        }
    }
}
