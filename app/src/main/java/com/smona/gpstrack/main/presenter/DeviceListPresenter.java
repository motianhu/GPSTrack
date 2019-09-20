package com.smona.gpstrack.main.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ConstParam;
import com.smona.gpstrack.common.IView;
import com.smona.gpstrack.common.bean.UrlBean;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.main.model.DeviceListModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:44 PM
 */
public class DeviceListPresenter extends BasePresenter<DeviceListPresenter.IDeviceView> {
    private DeviceListModel mModel = new DeviceListModel();

    public void requestDeviceList() {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ConstParam.LOCALE_EN);
        mModel.requestDeviceList(urlBean, new OnResultListener<Device>(){

            @Override
            public void onSuccess(Device device) {
                if(mView != null) {
                    mView.onSuccess();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if(mView != null) {
                    mView.onError("deviceList", stateCode, errorInfo);
                }
            }
        });
    }


    public interface IDeviceView extends IView {
        void onSuccess();
    }
}
