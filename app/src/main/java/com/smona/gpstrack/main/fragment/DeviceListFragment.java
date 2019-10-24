package com.smona.gpstrack.main.fragment;

import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.BasePresenterLoadingFragment;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.device.presenter.DeviceListPresenter;
import com.smona.gpstrack.main.adapter.DeviceAdapter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.widget.adapter.CommonItemDecoration;
import com.smona.http.wrapper.ErrorInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:34 PM
 */
public class DeviceListFragment extends BasePresenterLoadingFragment<DeviceListPresenter, DeviceListPresenter.IDeviceListView> implements DeviceListPresenter.IDeviceListView {

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
        int margin = getResources().getDimensionPixelSize(R.dimen.dimen_10dp);
        CommonItemDecoration ex = new CommonItemDecoration(margin, margin, margin);
        recyclerView.addItemDecoration(ex);

        WidgetComponent.initGridXRecyclerView(mActivity, recyclerView);

        content.findViewById(R.id.addDevice).setOnClickListener(view -> clickAddDevice());
        content.findViewById(R.id.refreshDevice).setOnClickListener(view -> clickRefreshDevice());
        content.findViewById(R.id.moreAction).setOnClickListener(view -> clickMoreAction());

        initExceptionProcess(content.findViewById(R.id.loadingresult), recyclerView);
    }

    @Override
    protected void initData() {
        super.initData();
        requestDeviceList();
    }

    private void requestDeviceList() {
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
    public void onSuccess(List<Device> deviceList) {
        if (deviceList == null || deviceList.isEmpty()) {
            doEmpty();
            return;
        }
        doSuccess();
        deviceAdapter.addData(deviceList);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        onError(api, errCode, errorInfo, this::requestDeviceList);
    }
}
