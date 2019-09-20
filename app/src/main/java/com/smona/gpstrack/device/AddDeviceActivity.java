package com.smona.gpstrack.device;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.presenter.DeviceAddPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 2:03 PM
 */
@Route(path = ARouterPath.PATH_TO_ADD_DEVICE)
public class AddDeviceActivity extends BasePresenterActivity<DeviceAddPresenter, DeviceAddPresenter.IDeviceAddView> implements DeviceAddPresenter.IDeviceAddView {
    @Override
    protected DeviceAddPresenter initPresenter() {
        return new DeviceAddPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_add;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
