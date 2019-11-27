package com.smona.gpstrack.settings.model;

import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigInfo;
import com.smona.gpstrack.settings.bean.AppNoticeItem;
import com.smona.gpstrack.settings.bean.DateFormatItem;
import com.smona.gpstrack.settings.bean.LanuageItem;
import com.smona.gpstrack.settings.bean.LogoutItem;
import com.smona.gpstrack.settings.bean.MapItem;
import com.smona.gpstrack.settings.bean.TimeZoneItem;
import com.smona.gpstrack.settings.bean.UserNameItem;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

import java.util.TimeZone;

public class SettingModel {
    public void requestViewAccount(UrlBean urlBean, OnResultListener<ConfigInfo> listener) {
        HttpCallbackProxy<ConfigInfo> httpCallbackProxy = new HttpCallbackProxy<ConfigInfo>(listener) {
        };
        String api = String.format(BusinessHttpService.ACCOUNT, urlBean.getLocale());
        new GpsDynamicBuilder<ConfigInfo>(GpsDynamicBuilder.REQUEST_GET, api).requestData(httpCallbackProxy);
    }

    public void switchLanuage(UrlBean urlBean, LanuageItem item, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.ACCOUNT, urlBean.getLocale());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_PUT, api).requestData(item, httpCallbackProxy);
    }

    public void switchMap(UrlBean urlBean, MapItem item, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.ACCOUNT, urlBean.getLocale());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_PUT, api).requestData(item, httpCallbackProxy);
    }

    public void switchDateFormat(UrlBean urlBean, DateFormatItem item, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.ACCOUNT, urlBean.getLocale());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_PUT, api).requestData(item, httpCallbackProxy);
    }

    public void switchTimeZone(UrlBean urlBean, TimeZoneItem item, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.ACCOUNT, urlBean.getLocale());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_PUT, api).requestData(item, httpCallbackProxy);
    }

    public void modifyUserName(UrlBean urlBean, UserNameItem item, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.ACCOUNT, urlBean.getLocale());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_PUT, api).requestData(item, httpCallbackProxy);
    }


    public void modifyAppNotice(UrlBean urlBean, AppNoticeItem item, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.ACCOUNT, urlBean.getLocale());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_PUT, api).requestData(item, httpCallbackProxy);
    }

    public void logout(LogoutItem urlBean, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.LOGOUT, urlBean.getLocale(), urlBean.getImei());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_POST, api).requestData(httpCallbackProxy);
    }
}
