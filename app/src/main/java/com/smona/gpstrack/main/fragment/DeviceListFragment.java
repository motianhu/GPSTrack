package com.smona.gpstrack.main.fragment;

import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.device.bean.DeviceListBean;
import com.smona.gpstrack.device.presenter.DeviceListPresenter;
import com.smona.gpstrack.main.adapter.DeviceAdapter;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.ArrayList;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:34 PM
 */
public class DeviceListFragment extends BasePresenterFragment<DeviceListPresenter, DeviceListPresenter.IDeviceListView> implements DeviceListPresenter.IDeviceListView {

    private DeviceAdapter deviceAdapter;

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
        XRecyclerView recyclerView = content.findViewById(R.id.xrecycler_wiget);
        deviceAdapter = new DeviceAdapter(R.layout.adapter_item_device);
        recyclerView.setAdapter(deviceAdapter);

        WidgetComponent.initXRecyclerView(mActivity, recyclerView, new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                deviceAdapter.setNewData(new ArrayList<>());
                mPresenter.requestRefresh();
            }

            @Override
            public void onLoadMore() {
                mPresenter.requestDeviceList();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.requestDeviceList();
    }

    @Override
    public void onSuccess(DeviceListBean deviceList) {
        deviceAdapter.addData(deviceList.getDatas());
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        ToastUtil.showShort(errorInfo.getMessage());
    }
}
