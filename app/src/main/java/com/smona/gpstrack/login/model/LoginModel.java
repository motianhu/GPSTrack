package com.smona.gpstrack.login.model;

import com.smona.gpstrack.common.bean.IModel;
import com.smona.gpstrack.common.bean.RespEmptyBean;
import com.smona.gpstrack.common.bean.UrlBean;
import com.smona.gpstrack.login.bean.LoginBodyBean;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.business.GpsBuilder;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 10:33 AM
 */
public class LoginModel implements IModel {

    public void login(UrlBean urlBean, LoginBodyBean bodyBean, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.LOGIN, urlBean.getLangauge());
        new GpsBuilder<RespEmptyBean>(GpsBuilder.REQUEST_POST, api).requestData(bodyBean, httpCallbackProxy);
    }
}
