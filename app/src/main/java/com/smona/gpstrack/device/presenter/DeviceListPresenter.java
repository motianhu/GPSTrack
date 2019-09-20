package com.smona.gpstrack.device.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ConstParam;
import com.smona.gpstrack.common.IView;
import com.smona.gpstrack.common.bean.UrlBean;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.device.bean.DeviceListBean;
import com.smona.gpstrack.device.model.DeviceListModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 1:30 PM
 */
public class DeviceListPresenter extends BasePresenter<DeviceListPresenter.IDeviceListView> {

    private DeviceListModel mModel = new DeviceListModel();

    public void requestDeviceList() {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ConstParam.LOCALE_EN);
        mModel.requestDeviceList(urlBean, new OnResultListener<DeviceListBean>() {
            @Override
            public void onSuccess(DeviceListBean deviceListBean) {
                if (mView != null) {
                    mView.onSuccess(deviceListBean);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("deviceList", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IDeviceListView extends IView {
        void onSuccess(DeviceListBean deviceList);
    }
}
