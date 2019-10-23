package com.smona.gpstrack.main.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.DeviceDecorate;
import com.smona.gpstrack.device.bean.DevicesAttachLocBean;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.device.model.DevicesAttachLocModel;
import com.smona.gpstrack.thread.WorkHandlerManager;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:44 PM
 */
public class MapPresenter extends BasePresenter<MapPresenter.IMapView> {

    private DeviceDecorate<RespDevice> deviceDecorate = new DeviceDecorate<>();
    private DevicesAttachLocModel mModel = new DevicesAttachLocModel();
    private int curPage = 0;

    public void requestDeviceList() {
        PageUrlBean urlBean = new PageUrlBean();
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        urlBean.setPage(curPage);
        urlBean.setPage_size(100);
        mModel.requestDeviceList(urlBean, new OnResultListener<DevicesAttachLocBean>() {
            @Override
            public void onSuccess(DevicesAttachLocBean deviceListBean) {
                if (mView != null) {
                    if (curPage < deviceListBean.getTtlPage()) {
                        curPage += 1;
                        mView.onSuccess(deviceListBean);
                        WorkHandlerManager.getInstance().runOnWorkerThread(() -> deviceDecorate.addAll(deviceListBean.getDatas()));
                    } else {
                        curPage = 0;
                    }
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    curPage = 0;
                    mView.onError("deviceList", stateCode, errorInfo);
                }
            }
        });
    }

    public void requestRefresh() {
        curPage = 0;
        requestDeviceList();
    }

    public interface IMapView extends ICommonView {
        void onSuccess(DevicesAttachLocBean deviceList);
    }
}
