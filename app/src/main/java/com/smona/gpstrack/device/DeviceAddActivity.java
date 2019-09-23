package com.smona.gpstrack.device;

import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.presenter.DeviceAddPresenter;
import com.smona.http.wrapper.ErrorInfo;

public class DeviceAddActivity extends BasePresenterActivity<DeviceAddPresenter, DeviceAddPresenter.IDeviceAddView> implements DeviceAddPresenter.IDeviceAddView
{
    @Override
    protected DeviceAddPresenter initPresenter() {
        return new DeviceAddPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_add;
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }

    @Override
    public void onSuccess() {

    }
}
