package com.smona.gpstrack.fence.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.FenceDecorate;
import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.fence.bean.FenceListBean;
import com.smona.gpstrack.fence.bean.FenceStatus;
import com.smona.gpstrack.fence.bean.url.FenceUrlBean;
import com.smona.gpstrack.fence.model.FenceEditModel;
import com.smona.gpstrack.fence.model.FenceListModel;
import com.smona.gpstrack.thread.WorkHandlerManager;
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

    private FenceDecorate<FenceBean> fenceDecorate = new FenceDecorate<>();
    private FenceListModel geoListModel = new FenceListModel();
    private FenceEditModel geoEditModel = new FenceEditModel();

    private int curPage = 0;

    public void requestGeoList() {
        PageUrlBean pageUrlBean = new PageUrlBean();
        pageUrlBean.setPage_size(1000);
        pageUrlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        pageUrlBean.setPage(curPage);
        geoListModel.requestGeoList(pageUrlBean, new OnResultListener<FenceListBean>() {
            @Override
            public void onSuccess(FenceListBean geoBeans) {
                WorkHandlerManager.getInstance().runOnWorkerThread(() -> fenceDecorate.addAll(geoBeans.getDatas()));
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
        FenceUrlBean urlBean = new FenceUrlBean();
        urlBean.setId(geoBean.getId());
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());

        geoEditModel.requestUpdateFenceStatus(urlBean, geoBean, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean emptyBean) {
                if(mView != null) {
                    mView.onUpdate();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("updateGeoInfo", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IGeoListView extends ICommonView {
        void onSuccess(List<FenceBean> datas);
        void onUpdate();
    }
}
