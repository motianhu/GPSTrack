package com.smona.gpstrack.device.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.DeviceDecorate;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.device.bean.req.ReqAddDevice;
import com.smona.gpstrack.device.model.DeviceModel;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.DeviceEvent;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 2:03 PM
 */
public class DeviceAddPresenter extends BasePresenter<DeviceAddPresenter.IDeviceAddView> {
    private DeviceDecorate<Device> deviceDecorate = new DeviceDecorate<>();
    private DeviceModel mModel = new DeviceModel();

    public void addDevice(String deviceId, String deviceName, String deviceOrderNo) {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());

        ReqAddDevice addDevice = new ReqAddDevice();
        addDevice.setNo(deviceId);
        addDevice.setName(deviceName);
        addDevice.setPoNo(deviceOrderNo);
        mModel.addDevice(urlBean, addDevice, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                notifyRefreshDevice();
                if (mView != null) {
                    mView.onSuccess();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("addDevice", stateCode, errorInfo);
                }
            }
        });
    }

    private void notifyRefreshDevice() {
        NotifyCenter.getInstance().postEvent(new DeviceEvent());
    }

    public interface IDeviceAddView extends ICommonView {
        void onSuccess();

    }
}
