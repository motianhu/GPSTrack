package com.smona.gpstrack.alarm.model;

import com.smona.gpstrack.alarm.bean.AlarmListBean;
import com.smona.gpstrack.alarm.bean.ReqAlarmDelete;
import com.smona.gpstrack.alarm.bean.ReqAlarmList;
import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.bean.IModel;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

public class AlarmListModel implements IModel {
    public void requestAlarmList(ReqAlarmList urlBean, OnResultListener<AlarmListBean> listener) {
        HttpCallbackProxy<AlarmListBean> httpCallbackProxy = new HttpCallbackProxy<AlarmListBean>(listener) {
        };
        String api = String.format(BusinessHttpService.ALERT_LIST, urlBean.getLocale(), urlBean.getPage_size(), urlBean.getPage(), urlBean.getDate_from());
        new GpsDynamicBuilder<AlarmListBean>(GpsDynamicBuilder.REQUEST_GET, api).requestData(httpCallbackProxy);
    }

    public void requestRemoveMessage(ReqAlarmDelete urlBean, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.ALERT_DELETE, urlBean.getLocale(), urlBean.getAlarmId());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_DELETE, api).requestData(httpCallbackProxy);
    }
}
