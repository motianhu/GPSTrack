package com.smona.gpstrack.device.model;

import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.bean.IModel;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.device.bean.req.ReqAddDevice;
import com.smona.gpstrack.device.bean.req.ReqChangeOwnerDevice;
import com.smona.gpstrack.device.bean.req.ReqDeviceAlarm;
import com.smona.gpstrack.device.bean.req.ReqDeviceDetail;
import com.smona.gpstrack.device.bean.req.ReqDeviceName;
import com.smona.gpstrack.device.bean.req.ReqShareDevice;
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

    public void updateDeviceName(ReqViewDevice urlBean, ReqDeviceName deviceName, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.DEVICE, urlBean.getLocale(), urlBean.getDeviceId());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_PUT, api).requestData(deviceName, httpCallbackProxy);
    }

    public void updateSwitch(ReqViewDevice urlBean, ReqDeviceAlarm alarm,  OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.DEVICE, urlBean.getLocale(), urlBean.getDeviceId());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_PUT, api).requestData(alarm, httpCallbackProxy);
    }

    public void viewDevice(ReqViewDevice urlBean, OnResultListener<ReqDeviceDetail> listener) {
        HttpCallbackProxy<ReqDeviceDetail> httpCallbackProxy = new HttpCallbackProxy<ReqDeviceDetail>(listener) {
        };
        String api = String.format(BusinessHttpService.DEVICE, urlBean.getLocale(), urlBean.getDeviceId());
        new GpsDynamicBuilder<ReqDeviceDetail>(GpsDynamicBuilder.REQUEST_GET, api).requestData(httpCallbackProxy);
    }

    public void deleteDevice(ReqViewDevice urlBean, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.DEVICE, urlBean.getLocale(), urlBean.getDeviceId());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_DELETE, api).requestData(httpCallbackProxy);
    }

    public void addShare(ReqShareDevice urlBean, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.DEVICE_SHARE, urlBean.getLocale(), urlBean.getDeviceId(), urlBean.getEmail());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_POST, api).requestData(httpCallbackProxy);
    }

    public void unShare(ReqChangeOwnerDevice urlBean, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.DEVICE_UNSHARE, urlBean.getLocale(), urlBean.getDeviceId(), urlBean.getShareId());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_POST, api).requestData(httpCallbackProxy);
    }

    public void changeOwner(ReqChangeOwnerDevice urlBean, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.DEVICE_UNSHARE, urlBean.getLocale(), urlBean.getDeviceId(), urlBean.getShareId());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_POST, api).requestData(httpCallbackProxy);
    }
}
