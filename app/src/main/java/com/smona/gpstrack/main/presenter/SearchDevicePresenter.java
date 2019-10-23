package com.smona.gpstrack.main.presenter;

import android.text.TextUtils;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.base.ui.mvp.IBaseView;
import com.smona.gpstrack.db.DeviceDecorate;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.thread.WorkHandlerManager;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/16/19 7:18 PM
 */
public class SearchDevicePresenter extends BasePresenter<SearchDevicePresenter.IView> {

    private DeviceDecorate deviceDecorate = new DeviceDecorate();

    public void requestSearchDevice(String name) {
        WorkHandlerManager.getInstance().runOnWorkerThread(() -> {
            List<Device> devices = TextUtils.isEmpty(name) ? deviceDecorate.listAll() : deviceDecorate.searchDevice(name);
            refreshUI(devices);
        });
    }

    private void refreshUI(List<Device> devices) {
        WorkHandlerManager.getInstance().runOnMainThread(() -> {
            if (mView != null) {
                mView.onSearchResult(devices);
            }
        });
    }


    public interface IView extends IBaseView {
        void onSearchResult(List<Device> devices);
    }
}
