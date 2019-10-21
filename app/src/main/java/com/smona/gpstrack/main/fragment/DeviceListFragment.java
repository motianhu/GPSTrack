package com.smona.gpstrack.main.fragment;

import android.icu.text.CaseMap;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.data.MemoryDeviceManager;
import com.smona.gpstrack.device.bean.DeviceListBean;
import com.smona.gpstrack.device.presenter.DeviceListPresenter;
import com.smona.gpstrack.main.adapter.DeviceAdapter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.gpstrack.widget.GridItemDecoration;
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
        recyclerView.addItemDecoration(new GridItemDecoration(mActivity, 20, 15, false));

        WidgetComponent.initGridXRecyclerView(mActivity, recyclerView, new XRecyclerView.LoadingListener() {
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

        content.findViewById(R.id.addDevice).setOnClickListener(view -> clickAddDevice());
        content.findViewById(R.id.refreshDevice).setOnClickListener(view -> clickRefreshDevice());
        content.findViewById(R.id.moreAction).setOnClickListener(view -> clickMoreAction());
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.requestDeviceList();
    }

    private void clickAddDevice() {
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_ADD_DEVICE);
    }

    private void clickRefreshDevice() {
        mPresenter.requestRefresh();
    }

    private void clickMoreAction() {

    }

    @Override
    public void onSuccess(DeviceListBean deviceList) {
        MemoryDeviceManager.getInstance().addDeviceList(deviceList.getDatas());
        deviceAdapter.addData(deviceList.getDatas());
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        ToastUtil.showShort(errorInfo.getMessage());
    }
}
