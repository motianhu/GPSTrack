package com.smona.gpstrack.geo.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.geo.bean.GeoBean;
import com.smona.gpstrack.geo.bean.GeoListBean;
import com.smona.gpstrack.geo.model.GeoListModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:44 PM
 */
public class GeoListPresenter extends BasePresenter<GeoListPresenter.IGeoListView> {

    private GeoListModel geoListModel = new GeoListModel();

    private int curPage = 0;

    public void requestGeoList() {
        PageUrlBean pageUrlBean = new PageUrlBean();
        pageUrlBean.setPage_size(10);
        pageUrlBean.setLocale(ParamConstant.LOCALE_EN);
        pageUrlBean.setPage(curPage);
        geoListModel.requestGeoList(pageUrlBean, new OnResultListener<GeoListBean>() {
            @Override
            public void onSuccess(GeoListBean geoBeans) {
                if (mView != null) {
                    mView.onSuccess(geoBeans.getDatas());
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("requestGeoList", stateCode, errorInfo);
                }
            }
        });
    }

    public void refreshGeoList() {
        curPage = 0;
        requestGeoList();
    }

    public void updateGeoInfo(GeoBean geoBean) {
        requestGeoList();
    }

    public interface IGeoListView extends ICommonView {
        void onSuccess(List<GeoBean> datas);
    }
}
