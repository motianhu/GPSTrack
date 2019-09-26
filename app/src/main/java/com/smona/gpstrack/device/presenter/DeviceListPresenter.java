package com.smona.gpstrack.device.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.device.bean.DeviceListBean;
import com.smona.gpstrack.device.model.DeviceListModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 1:30 PM
 */
public class DeviceListPresenter extends BasePresenter<DeviceListPresenter.IDeviceListView> {

    private DeviceListModel mModel = new DeviceListModel();
    private int curPage = 1;

    public void requestDeviceList() {
        PageUrlBean urlBean = new PageUrlBean();
        urlBean.setLocale(ParamConstant.LOCALE_EN);
        urlBean.setPage(curPage);
        urlBean.setPage_size(10);
        mModel.requestDeviceList(urlBean, new OnResultListener<DeviceListBean>() {
            @Override
            public void onSuccess(DeviceListBean deviceListBean) {
                if (mView != null) {
                    if (curPage < deviceListBean.getTtlPage()) {
                        curPage += 1;
                    }
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

    public void requestRefresh() {
        curPage = 1;
        requestDeviceList();
    }

    public interface IDeviceListView extends ICommonView {
        void onSuccess(DeviceListBean deviceList);
    }
}
