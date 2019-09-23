package com.smona.gpstrack.register.model;

import com.smona.gpstrack.common.GpsFixedBuilder;
import com.smona.gpstrack.common.bean.BodyBean;
import com.smona.gpstrack.common.bean.IModel;
import com.smona.gpstrack.common.bean.RespEmptyBean;
import com.smona.gpstrack.common.bean.UrlBean;
import com.smona.gpstrack.register.bean.VerifyUrlBean;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 4:16 PM
 */
public class RegisterModel implements IModel {

    public void register(UrlBean urlBean, BodyBean bodyBean, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.REGISTER, urlBean.getLocale());
        new GpsFixedBuilder<RespEmptyBean>(GpsFixedBuilder.REQUEST_POST, api).requestData(bodyBean, httpCallbackProxy);
    }

    public void register(VerifyUrlBean urlBean, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.VERIFY, urlBean.getLocale(), urlBean.getImei(), urlBean.getEmail(), urlBean.getCode());
        new GpsFixedBuilder<RespEmptyBean>(GpsFixedBuilder.REQUEST_POST, api).requestData(httpCallbackProxy);
    }
}
