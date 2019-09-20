package com.smona.gpstrack.main.model;

import com.smona.gpstrack.common.bean.IModel;
import com.smona.gpstrack.common.bean.UrlBean;
import com.smona.gpstrack.db.table.Device;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.business.GpsBuilder;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 9:46 AM
 */
public class DeviceListModel implements IModel {

    public void requestDeviceList(UrlBean urlBean, OnResultListener<Device> listener) {
        HttpCallbackProxy<Device> httpCallbackProxy = new HttpCallbackProxy<Device>(listener) {
        };
        String api = String.format(BusinessHttpService.DEVICE_LIST, urlBean.getLocale());
        new GpsBuilder<Device>(GpsBuilder.REQUEST_POST, api).requestData(httpCallbackProxy);
    }

}
