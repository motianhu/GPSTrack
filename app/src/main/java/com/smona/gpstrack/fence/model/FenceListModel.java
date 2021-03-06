package com.smona.gpstrack.fence.model;

import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.fence.bean.FenceListBean;
import com.smona.gpstrack.fence.bean.FenceStatus;
import com.smona.gpstrack.fence.bean.url.FenceUrlBean;
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
