package com.smona.gpstrack.device.presenter;

import android.text.TextUtils;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.DeviceDecorate;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.device.bean.DeviceListBean;
import com.smona.gpstrack.device.model.DeviceListModel;
import com.smona.gpstrack.thread.WorkHandlerManager;
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

    private DeviceDecorate<Device> deviceDecorate = new DeviceDecorate<>();
    private DeviceListModel mModel = new DeviceListModel();
    private int curPage = 0;

    public void requestDeviceList(String filter) {
        requestDbDevices(filter);
    }

    //DB数据会丢掉location信息
    private void requestDbDevices(String filter) {
        WorkHandlerManager.getInstance().runOnWorkerThread(() -> {
            List<Device> deviceList = deviceDecorate.listDevice(filter);
            if (TextUtils.isEmpty(filter) && (deviceList == null || deviceList.isEmpty())) {
                requestNetDevices();
            } else {
                postUI(deviceList);
            }
        });
    }

    private void postUI(List<Device> deviceList) {
        WorkHandlerManager.getInstance().runOnMainThread(() -> mView.onSuccess(deviceList));
    }

    private void requestNetDevices() {
        PageUrlBean urlBean = new PageUrlBean();
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        urlBean.setPage(curPage);
        urlBean.setPage_size(100);
        mModel.requestDeviceList(urlBean, new OnResultListener<DeviceListBean>() {
            @Override
            public void onSuccess(DeviceListBean deviceListBean) {
                if (mView != null) {
                    if (curPage < deviceListBean.getTtlPage()) {
                        curPage += 1;
                    }
                    mView.onSuccess(deviceListBean.getDatas());
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError(curPage == 0 ? "":"deviceList", stateCode, errorInfo);
                }
            }
        });
    }

    public void requestRefresh(String filter) {
        curPage = 0;
        requestNetDevices();
    }

    public interface IDeviceListView extends ICommonView {
        void onSuccess(List<Device> deviceList);
    }
}
