package com.smona.gpstrack.geo.model;

import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.geo.bean.GeoListBean;
import com.smona.http.business.BusinessHttpService;
import com.smona.http.wrapper.HttpCallbackProxy;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 5:19 PM
 */
public class GeoListModel {
    public void requestGeoList(PageUrlBean urlBean, OnResultListener<GeoListBean> listener) {
        HttpCallbackProxy<GeoListBean> httpCallbackProxy = new HttpCallbackProxy<GeoListBean>(listener) {
        };
        String api = String.format(BusinessHttpService.GEO_LIST, urlBean.getLocale(), urlBean.getPage_size(), urlBean.getPage());
        new GpsDynamicBuilder<GeoListBean>(GpsDynamicBuilder.REQUEST_GET, api).requestData(httpCallbackProxy);
    }
}
