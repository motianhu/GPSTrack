package com.smona.gpstrack.login.model;

import com.smona.gpstrack.common.GpsFixedBuilder;
import com.smona.gpstrack.common.bean.IModel;
import com.smona.gpstrack.common.bean.UrlBean;
import com.smona.gpstrack.common.param.ConfigParam;
import com.smona.gpstrack.login.bean.LoginBodyBean;
import com.smona.http.business.BusinessHttpService;
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

    public void login(UrlBean urlBean, LoginBodyBean bodyBean, OnResultListener<ConfigParam> listener) {
        HttpCallbackProxy<ConfigParam> httpCallbackProxy = new HttpCallbackProxy<ConfigParam>(listener) {
        };
        String api = String.format(BusinessHttpService.LOGIN, urlBean.getLocale());
        new GpsFixedBuilder<ConfigParam>(GpsFixedBuilder.REQUEST_POST, api).requestData(bodyBean, httpCallbackProxy);
    }
}
