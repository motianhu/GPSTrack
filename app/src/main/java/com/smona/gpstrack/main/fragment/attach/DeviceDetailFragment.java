package com.smona.gpstrack.main.fragment.attach;

import android.view.View;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.device.bean.DeviceListBean;
import com.smona.gpstrack.device.presenter.DeviceListPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/16/19 7:16 PM
 */
public class DeviceDetailFragment extends BasePresenterFragment<DeviceListPresenter, DeviceListPresenter.IDeviceListView> implements DeviceListPresenter.IDeviceListView {

    private Device device;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map_device;
    }

    @Override
    protected DeviceListPresenter initPresenter() {
        return new DeviceListPresenter();
    }

    @Override
    protected void initView(View content) {
        super.initView(content);
        content.findViewById(R.id.maskView).setOnTouchListener((v, event) -> true);

        content.findViewById(R.id.routeHistory).setOnClickListener(v -> ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_DEVICE_HISTORY));
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.requestDeviceList();
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public void onSuccess(DeviceListBean deviceList) {

    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
