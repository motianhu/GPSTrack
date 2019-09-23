package com.smona.gpstrack.device;

import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.device.presenter.DeviceDetailPresenter;
import com.smona.http.wrapper.ErrorInfo;

public class DeviceDetailActivity extends BasePresenterActivity<DeviceDetailPresenter, DeviceDetailPresenter.IDeviceDetailView> implements DeviceDetailPresenter.IDeviceDetailView
{
    @Override
    protected DeviceDetailPresenter initPresenter() {
        return new DeviceDetailPresenter();
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
