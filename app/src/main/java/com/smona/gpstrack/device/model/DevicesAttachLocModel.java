package com.smona.gpstrack.device.model;

import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.bean.IModel;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.device.bean.DeviceAttLocBean;
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

    public void requestDeviceList(PageUrlBean urlBean, OnResultListener<DeviceAttLocBean> listener) {
        HttpCallbackProxy<DeviceAttLocBean> httpCallbackProxy = new HttpCallbackProxy<DeviceAttLocBean>(listener) {
        };
        String api = String.format(BusinessHttpService.DEVICE_LIST, urlBean.getLocale(), urlBean.getPage_size(), urlBean.getPage());
        new GpsDynamicBuilder<DeviceAttLocBean>(GpsDynamicBuilder.REQUEST_GET, api).requestData(httpCallbackProxy);
    }
}
