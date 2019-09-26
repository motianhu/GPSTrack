package com.smona.gpstrack.changepwd.model;

import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.bean.IModel;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 11:44 AM
 */
public class ChangePwdModel implements IModel {
    public void changePwd(UrlBean urlBean, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.CHNAGE_PASSWORD, urlBean.getLocale());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_POST, api).requestData(httpCallbackProxy);
    }
}
