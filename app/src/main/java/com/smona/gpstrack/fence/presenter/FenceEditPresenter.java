package com.smona.gpstrack.fence.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.DeviceDecorate;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.fence.bean.DeviceItem;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.fence.model.FenceEditModel;
import com.smona.gpstrack.thread.WorkHandlerManager;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 2:00 PM
 */
public class FenceEditPresenter extends BasePresenter<FenceEditPresenter.IGeoEditView> {
    private FenceEditModel editModel = new FenceEditModel();

    private DeviceDecorate<DeviceItem> deviceDecorate = new DeviceDecorate<DeviceItem>() {
    };

    public void requestAllDevice() {
        WorkHandlerManager.getInstance().runOnWorkerThread(() -> {
            List<Device> allDevice = deviceDecorate.listAll();
            List<DeviceItem> allDeviceItem = new ArrayList<>();
            if (!(allDevice == null || allDevice.isEmpty())) {
                DeviceItem item;
                for (Device device : allDevice) {
                    item = new DeviceItem();
                    item.copy(device);
                    allDeviceItem.add(item);
                }
            }
            postUI(allDeviceItem);
        });
    }

    private void postUI(List<DeviceItem> allDevice) {
        WorkHandlerManager.getInstance().runOnWorkerThread(() -> {
            if (mView != null) {
                mView.onDeviceList(allDevice);
            }
        });
    }

    public void requestAdd(FenceBean fenceBean) {
        UrlBean pageUrlBean = new UrlBean();
        pageUrlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        editModel.requestAddFence(pageUrlBean, fenceBean, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if (mView != null) {
                    mView.onAdd();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("requestAdd", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IGeoEditView extends ICommonView {
        void onDeviceList(List<DeviceItem> deviceList);

        void onAdd();
    }
}
