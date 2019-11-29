package com.smona.gpstrack.main.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.alarm.bean.AlarmUnRead;
import com.smona.gpstrack.alarm.bean.ReqAlarmUnRead;
import com.smona.gpstrack.alarm.model.AlarmUnReadModel;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.DeviceDecorate;
import com.smona.gpstrack.db.FenceDecorate;
import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.device.bean.DeviceAttLocBean;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.device.model.DevicesAttachLocModel;
import com.smona.gpstrack.fence.bean.FenceListBean;
import com.smona.gpstrack.fence.model.FenceListModel;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.AlarmUnReadEvent;
import com.smona.gpstrack.notify.event.FenceAllEvent;
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
    private AlarmUnReadModel alarmUnReadModel = new AlarmUnReadModel();
    private FenceListModel geoListModel = new FenceListModel();
    private int curPage = 0;
    private int fenceCurPage = 0;

    public void requestDeviceList() {
        if (curPage == 0) {
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
                    if (curPage == 0 && CommonUtils.isEmpty(deviceListBean.getDatas())) {
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

    public void requestGeoList() {
        if (fenceCurPage == 0) {
            WorkHandlerManager.getInstance().runOnWorkerThread(() -> fenceDecorate.deleteAll());
        }
        PageUrlBean pageUrlBean = new PageUrlBean();
        pageUrlBean.setPage_size(100);
        pageUrlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        pageUrlBean.setPage(fenceCurPage);
        geoListModel.requestGeoList(pageUrlBean, new OnResultListener<FenceListBean>() {
            @Override
            public void onSuccess(FenceListBean geoBeans) {
                if (mView != null) {
                    WorkHandlerManager.getInstance().runOnWorkerThread(() -> {
                        fenceDecorate.addAll(geoBeans.getDatas());
                        FenceAllEvent allEvent = new FenceAllEvent();
                        NotifyCenter.getInstance().postEvent(allEvent);
                    });
                    if ((fenceCurPage + 1) < geoBeans.getTtlPage()) {
                        fenceCurPage += 1;
                    } else {
                        fenceCurPage = 0;
                    }
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    fenceCurPage = 0;
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


    public void requestUnRead(String deviceId) {
        ReqAlarmUnRead alarmRead = new ReqAlarmUnRead();
        alarmRead.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        alarmRead.setDevicePlatform(deviceId);
        alarmUnReadModel.requestUnReadCount(alarmRead, new OnResultListener<AlarmUnRead>() {
            @Override
            public void onSuccess(AlarmUnRead alarmUnRead) {
                if(mView != null) {
                    AlarmUnReadEvent event = new AlarmUnReadEvent();
                    event.setUnReadCount(alarmUnRead.getTtlUnRead());
                    NotifyCenter.getInstance().postEvent(event);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("updateAlarmStatus", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IMapView extends ICommonView {
        void onEmpty();

        void onSuccess(int curPage, DeviceAttLocBean deviceList);

        void onFenceList(List<Fence> fenceList);
    }
}
