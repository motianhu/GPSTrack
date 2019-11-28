package com.smona.gpstrack.fence.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.FenceDecorate;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.fence.bean.FenceListBean;
import com.smona.gpstrack.fence.bean.url.FenceUrlBean;
import com.smona.gpstrack.fence.model.FenceEditModel;
import com.smona.gpstrack.fence.model.FenceListModel;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.FenceUpdateEvent;
import com.smona.gpstrack.thread.WorkHandlerManager;
import com.smona.gpstrack.util.CommonUtils;
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
                    //没数据
                    if (curPage == 0 && CommonUtils.isEmpty(geoBeans.getDatas())) {
                        mView.onEmpty();
                        return;
                    }
                    //有数据
                    mView.onFenceList(curPage, geoBeans.getDatas());

                    if ((curPage + 1) < geoBeans.getTtlPage()) {
                        curPage += 1;
                    } else {
                        curPage = 0;
                        //最后一页
                        mView.onFinishMoreLoad();
                    }
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError(curPage == 0 ? "" : "requestGeoList", stateCode, errorInfo);
                }
            }
        });
    }

    public void updateGeoInfo(FenceBean geoBean) {
        FenceUrlBean urlBean = new FenceUrlBean();
        urlBean.setId(geoBean.getId());
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());

        geoEditModel.requestUpdateFenceStatus(urlBean, geoBean, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean emptyBean) {
                FenceUpdateEvent updateEvent = new FenceUpdateEvent();
                updateEvent.setUpdateFence(geoBean);
                NotifyCenter.getInstance().postEvent(updateEvent);
                if (mView != null) {
                    mView.onUpdateEnable();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    geoBean.setStatus(FenceBean.ACTIVE.equals(geoBean.getStatus()) ? FenceBean.INACTIVE : FenceBean.ACTIVE);
                    mView.onError("updateGeoInfo", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IGeoListView extends ICommonView {
        void onEmpty();

        void onFinishMoreLoad();

        void onFenceList(int curPage, List<FenceBean> datas);

        void onUpdateEnable();
    }
}
