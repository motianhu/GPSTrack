package com.smona.gpstrack.geo.model;

import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.geo.bean.FenceListBean;
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
public class FenceListModel {
    public void requestGeoList(PageUrlBean urlBean, OnResultListener<FenceListBean> listener) {
        HttpCallbackProxy<FenceListBean> httpCallbackProxy = new HttpCallbackProxy<FenceListBean>(listener) {
        };
        String api = String.format(BusinessHttpService.GEO_LIST, urlBean.getLocale(), urlBean.getPage_size(), urlBean.getPage());
        new GpsDynamicBuilder<FenceListBean>(GpsDynamicBuilder.REQUEST_GET, api).requestData(httpCallbackProxy);
    }
}
