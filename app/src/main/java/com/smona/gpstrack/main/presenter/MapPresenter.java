package com.smona.gpstrack.main.presenter;

import android.text.TextUtils;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.DeviceDecorate;
import com.smona.gpstrack.db.FenceDecorate;
import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.device.bean.DeviceAttLocBean;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.device.model.DevicesAttachLocModel;
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
public class MapPresenter extends BasePresenter<MapPresenter.IMapView> {

    private FenceDecorate<Fence> fenceDecorate = new FenceDecorate<>();
    private DeviceDecorate<RespDevice> deviceDecorate = new DeviceDecorate<>();
    private DevicesAttachLocModel mModel = new DevicesAttachLocModel();
    private int curPage = 0;

    public void requestDeviceList() {
        if(curPage == 0) {
            WorkHandlerManager.getInstance().runOnWorkerThread(() -> deviceDecorate.deleteAll());
        }
        PageUrlBean urlBean = new PageUrlBean();
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        urlBean.setPage(curPage);
        urlBean.setPage_size(1000);
        mModel.requestDeviceList(urlBean, new OnResultListener<DeviceAttLocBean>() {
            @Override
            public void onSuccess(DeviceAttLocBean deviceListBean) {
                if (mView != null) {
                    if(curPage == 0 && CommonUtils.isEmpty(deviceListBean.getDatas())) {
                        mView.onEmpty();
                        return;
                    }

                    mView.onSuccess(curPage, deviceListBean);
                    WorkHandlerManager.getInstance().runOnWorkerThread(() -> deviceDecorate.addAll(deviceListBean.getDatas()));

                    if ((curPage + 1) < deviceListBean.getTtlPage()) {
                        curPage += 1;
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

    public void requestFenceAll() {
        WorkHandlerManager.getInstance().runOnWorkerThread(() -> {
            List<Fence> fenceList = fenceDecorate.listAll();
            refreshFenceUI(fenceList);
        });
    }

    private void refreshFenceUI(List<Fence> fenceList) {
        WorkHandlerManager.getInstance().runOnMainThread(() -> {
            if (mView != null) {
                mView.onFenceList(fenceList);
            }
        });
    }

    public interface IMapView extends ICommonView {
        void onEmpty();
        void onSuccess(int curPage, DeviceAttLocBean deviceList);

        void onFenceList(List<Fence> fenceList);
    }
}
