package com.smona.gpstrack.device.model;

import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.bean.IModel;
import com.smona.gpstrack.device.bean.LocationListBean;
import com.smona.gpstrack.device.bean.req.ReqLocationList;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/29/19 1:03 PM
 */
public class DeviceLocationModel implements IModel {
    public void requestHistoryLocation(ReqLocationList urlBean, OnResultListener<LocationListBean> listener) {
        HttpCallbackProxy<LocationListBean> httpCallbackProxy = new HttpCallbackProxy<LocationListBean>(listener) {
        };
        String api = String.format(BusinessHttpService.LOCATION_DEVICE, urlBean.getLocale(), urlBean.getDevicePlatformId(), urlBean.getPage_size(), urlBean.getPage(), urlBean.getMap(), urlBean.getDateFrom(), urlBean.getDateTo());
        new GpsDynamicBuilder<LocationListBean>(GpsDynamicBuilder.REQUEST_GET, api).requestData(httpCallbackProxy);
    }
}
