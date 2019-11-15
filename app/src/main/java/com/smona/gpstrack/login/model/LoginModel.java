package com.smona.gpstrack.login.model;

import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.GpsFixedBuilder;
import com.smona.gpstrack.common.bean.IModel;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.AccountInfo;
import com.smona.gpstrack.login.bean.LoginBodyBean;
import com.smona.gpstrack.login.bean.LoginPushToken;
import com.smona.gpstrack.settings.bean.UserNameItem;
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

    public void login(UrlBean urlBean, LoginBodyBean bodyBean, OnResultListener<AccountInfo> listener) {
        HttpCallbackProxy<AccountInfo> httpCallbackProxy = new HttpCallbackProxy<AccountInfo>(listener) {
        };
        String api = String.format(BusinessHttpService.LOGIN, urlBean.getLocale());
        new GpsFixedBuilder<AccountInfo>(GpsFixedBuilder.REQUEST_POST, api).requestData(bodyBean, httpCallbackProxy);
    }

    public void sendGooglePushToken(UrlBean urlBean, LoginPushToken item, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.ACCOUNT, urlBean.getLocale());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_PUT, api).requestData(item, httpCallbackProxy);
    }
}
