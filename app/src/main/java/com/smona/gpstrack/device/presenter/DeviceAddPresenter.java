package com.smona.gpstrack.device.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.IView;
import com.smona.gpstrack.device.model.DeviceAddModel;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 2:03 PM
 */
public class DeviceAddPresenter extends BasePresenter<DeviceAddPresenter.IDeviceAddView> {

    private DeviceAddModel mModel = new DeviceAddModel();

    public void addDevice() {
        mModel.addDevice();
    }

    public interface IDeviceAddView extends IView {
        void onSuccess();
    }
}
