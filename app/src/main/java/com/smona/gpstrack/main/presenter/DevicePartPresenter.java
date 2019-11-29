package com.smona.gpstrack.main.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.alarm.bean.AlarmUnRead;
import com.smona.gpstrack.alarm.bean.ReqAlarmUnRead;
import com.smona.gpstrack.alarm.model.AlarmUnReadModel;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.AlarmUnReadDeviceEvent;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

public class DevicePartPresenter extends BasePresenter<DevicePartPresenter.IUnRead> {

    private AlarmUnReadModel alarmUnReadModel = new AlarmUnReadModel();

    public void requestUnReadDevice(String deviceId) {
        ReqAlarmUnRead alarmRead = new ReqAlarmUnRead();
        alarmRead.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        alarmRead.setDevicePlatform(deviceId);
        alarmUnReadModel.requestUnReadCount(alarmRead, new OnResultListener<AlarmUnRead>() {
            @Override
            public void onSuccess(AlarmUnRead alarmUnRead) {
                if(mView != null) {
                    AlarmUnReadDeviceEvent event = new AlarmUnReadDeviceEvent();
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

    public interface IUnRead extends ICommonView {
    }
}
