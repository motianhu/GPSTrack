package com.smona.gpstrack.device.model;

import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.bean.IModel;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.device.bean.DevicesAttachLocBean;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 1:32 PM
 */
public class DevicesAttachLocModel implements IModel {

    public void requestDeviceList(PageUrlBean urlBean, OnResultListener<DevicesAttachLocBean> listener) {
        HttpCallbackProxy<DevicesAttachLocBean> httpCallbackProxy = new HttpCallbackProxy<DevicesAttachLocBean>(listener) {
        };
        String api = String.format(BusinessHttpService.DEVICE_LIST, urlBean.getLocale(), urlBean.getPage_size(), urlBean.getPage());
        new GpsDynamicBuilder<DevicesAttachLocBean>(GpsDynamicBuilder.REQUEST_GET, api).requestData(httpCallbackProxy);
    }
}
