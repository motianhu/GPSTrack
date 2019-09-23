package com.smona.gpstrack.forget.model;

import com.smona.gpstrack.common.GpsFixedBuilder;
import com.smona.gpstrack.common.bean.IModel;
import com.smona.gpstrack.common.bean.RespEmptyBean;
import com.smona.gpstrack.forget.bean.ForgetPwdBean;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 2:22 PM
 */
public class ForgetPwdModel implements IModel {
    public void sendEmail(ForgetPwdBean urlBean, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.FORGET_PASSWORD, urlBean.getLocale(), urlBean.getEmail());
        new GpsFixedBuilder<RespEmptyBean>(GpsFixedBuilder.REQUEST_POST, api).requestData(httpCallbackProxy);
    }
}
