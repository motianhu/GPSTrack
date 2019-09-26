package com.smona.gpstrack.device.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.device.bean.req.ReqDeviceDetail;
import com.smona.gpstrack.device.bean.req.ReqViewDevice;
import com.smona.gpstrack.device.model.DeviceModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

public class DeviceDetailPresenter extends BasePresenter<DeviceDetailPresenter.IDeviceDetailView> {

    private DeviceModel deviceModel = new DeviceModel();

    public void deviceDetail(String deviceId) {
        ReqViewDevice viewDevice = new ReqViewDevice();
        viewDevice.setLocale(ParamConstant.LOCALE_EN);
        viewDevice.setDeviceId(deviceId);
        deviceModel.viewDevice(viewDevice, new OnResultListener<ReqDeviceDetail>() {
            @Override
            public void onSuccess(ReqDeviceDetail deviceDetail) {
                if (mView != null) {
                    mView.onSuccess(deviceDetail);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("deviceDetail", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IDeviceDetailView extends ICommonView {
        void onSuccess(ReqDeviceDetail deviceDetail);
    }
}
