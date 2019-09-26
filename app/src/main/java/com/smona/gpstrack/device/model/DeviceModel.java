package com.smona.gpstrack.device.model;

import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.bean.IModel;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.device.bean.req.ReqAddDevice;
import com.smona.gpstrack.device.bean.req.ReqDeviceDetail;
import com.smona.gpstrack.device.bean.req.ReqViewDevice;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 2:04 PM
 */
public class DeviceModel implements IModel {
    public void addDevice(UrlBean urlBean, ReqAddDevice addDevice, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.ADD_DEVICE, urlBean.getLocale());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_POST, api).requestData(addDevice, httpCallbackProxy);
    }

    public void viewDevice(ReqViewDevice urlBean, OnResultListener<ReqDeviceDetail> listener) {
        HttpCallbackProxy<ReqDeviceDetail> httpCallbackProxy = new HttpCallbackProxy<ReqDeviceDetail>(listener) {
        };
        String api = String.format(BusinessHttpService.DEVICE, urlBean.getLocale(), urlBean.getDeviceId());
        new GpsDynamicBuilder<ReqDeviceDetail>(GpsDynamicBuilder.REQUEST_GET, api).requestData(httpCallbackProxy);
    }
}
