package com.smona.gpstrack.main.fragment;

import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.bean.DeviceListBean;
import com.smona.gpstrack.device.presenter.DeviceListPresenter;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:34 PM
 */
public class DeviceListFragment extends BasePresenterFragment<DeviceListPresenter, DeviceListPresenter.IDeviceListView> implements DeviceListPresenter.IDeviceListView {

    private XRecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_device_list;
    }

    @Override
    protected DeviceListPresenter initPresenter() {
        return new DeviceListPresenter();
    }

    @Override
    protected void initView(View content) {
        super.initView(content);
        recyclerView = content.findViewById(R.id.xrecycler_wiget);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.requestDeviceList();
    }

    @Override
    public void onSuccess(DeviceListBean deviceList) {

    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
