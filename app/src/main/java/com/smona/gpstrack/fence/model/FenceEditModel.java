package com.smona.gpstrack.fence.model;

import com.smona.gpstrack.common.GpsDynamicBuilder;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.fence.bean.FenceListBean;
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
public class FenceEditModel {
    public void requestAddFence(UrlBean urlBean, FenceBean fenceBean, OnResultListener<RespEmptyBean> listener) {
        HttpCallbackProxy<RespEmptyBean> httpCallbackProxy = new HttpCallbackProxy<RespEmptyBean>(listener) {
        };
        String api = String.format(BusinessHttpService.GEO_ADD, urlBean.getLocale());
        new GpsDynamicBuilder<RespEmptyBean>(GpsDynamicBuilder.REQUEST_POST, api).requestData(fenceBean, httpCallbackProxy);
    }
}
