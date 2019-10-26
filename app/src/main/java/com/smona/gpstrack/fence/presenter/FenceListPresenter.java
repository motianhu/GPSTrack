package com.smona.gpstrack.fence.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.fence.bean.FenceListBean;
import com.smona.gpstrack.fence.model.FenceListModel;
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
public class FenceListPresenter extends BasePresenter<FenceListPresenter.IGeoListView> {

    private FenceListModel geoListModel = new FenceListModel();

    private int curPage = 0;

    public void requestGeoList() {
        PageUrlBean pageUrlBean = new PageUrlBean();
        pageUrlBean.setPage_size(10);
        pageUrlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        pageUrlBean.setPage(curPage);
        geoListModel.requestGeoList(pageUrlBean, new OnResultListener<FenceListBean>() {
            @Override
            public void onSuccess(FenceListBean geoBeans) {
                if (mView != null) {
                    mView.onSuccess(geoBeans.getDatas());
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError(curPage == 0 ? "":"requestGeoList", stateCode, errorInfo);
                }
            }
        });
    }

    public void refreshGeoList() {
        curPage = 0;
        requestGeoList();
    }

    public void updateGeoInfo(FenceBean geoBean) {
        requestGeoList();
    }

    public interface IGeoListView extends ICommonView {
        void onSuccess(List<FenceBean> datas);
    }
}
