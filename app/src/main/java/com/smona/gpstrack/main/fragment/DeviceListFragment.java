package com.smona.gpstrack.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.smona.gpstrack.R;
import com.smona.gpstrack.common.BasePresenterLoadingFragment;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.device.bean.FilteItem;
import com.smona.gpstrack.device.dialog.SelectCommonDialog;
import com.smona.gpstrack.device.presenter.DeviceListPresenter;
import com.smona.gpstrack.main.adapter.DeviceAdapter;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.DeviceEvent;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.widget.adapter.CommonItemDecoration;
import com.smona.http.wrapper.ErrorInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    private RecyclerView recyclerView;
    private DeviceAdapter deviceAdapter;

    //filter
    private SelectCommonDialog filterDialog;
    private List<FilteItem> filterList;
    private FilteItem curFilterItem;

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
        deviceAdapter = new DeviceAdapter(this, R.layout.adapter_item_device);
        recyclerView.setAdapter(deviceAdapter);
        int margin = getResources().getDimensionPixelSize(R.dimen.dimen_10dp);
        CommonItemDecoration ex = new CommonItemDecoration(margin, margin, margin);
        recyclerView.addItemDecoration(ex);

        WidgetComponent.initGridRecyclerView(mActivity, recyclerView);

        content.findViewById(R.id.addDevice).setOnClickListener(view -> clickAddDevice());
        content.findViewById(R.id.refreshDevice).setOnClickListener(view -> clickRefreshDevice());
        content.findViewById(R.id.moreAction).setOnClickListener(view -> clickMoreAction());

        initExceptionProcess(content.findViewById(R.id.loadingresult), recyclerView);

        initFilterData();
        NotifyCenter.getInstance().registerListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotifyCenter.getInstance().unRegisterListener(this);
    }

    private void initFilterData() {
        filterList = new ArrayList<>();
        FilteItem item = new FilteItem();
        item.setFilterName(getResources().getString(R.string.all));
        item.setFilterKey("");
        filterList.add(item);

        item = new FilteItem();
        item.setFilterName(getResources().getString(R.string.online));
        item.setFilterKey(Device.ONLINE);
        filterList.add(item);

        item = new FilteItem();
        item.setFilterName(getResources().getString(R.string.offline));
        item.setFilterKey(Device.OFFLINE);
        filterList.add(item);

        item = new FilteItem();
        item.setFilterName(getResources().getString(R.string.inactive));
        item.setFilterKey(Device.INACTIVE);
        filterList.add(item);
    }

    @Override
    protected void initData() {
        super.initData();
        requestFirstDeviceList();
    }

    private void requestFirstDeviceList() {
        mPresenter.requestRefresh("");
    }

    private void requestDeviceList() {
        mPresenter.requestDeviceList(curFilterItem == null ? "" : curFilterItem.getFilterKey());
    }

    private void clickAddDevice() {
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_ADD_DEVICE);
    }

    private void clickRefreshDevice() {
        showLoadingDialog();
        mPresenter.requestRefresh(curFilterItem == null ? "" : curFilterItem.getFilterKey());
    }

    private void clickMoreAction() {
        if (filterDialog == null) {
            filterDialog = new SelectCommonDialog(mActivity, getResources().getString(R.string.filter), filterList, item -> {
                curFilterItem = item;
                filterDialog.dismiss();
                showLoadingDialog();
                requestDeviceList();
            });

        }
        filterDialog.show();
    }

    @Override
    public void onSuccess(List<Device> deviceList) {
        hideLoadingDialog();
        if (CommonUtils.isEmpty(deviceList)) {
            doEmpty();
            return;
        }
        doSuccess();
        deviceAdapter.setNewData(deviceList);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        onError(api, errCode, errorInfo, this::requestDeviceList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ARouterPath.REQUEST_DEVICE_DETAIL && resultCode == Activity.RESULT_OK) {
            showLoadingDialog();
            requestDeviceList();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgRefreshDeviceList(DeviceEvent event) {
        mPresenter.requestRefresh(curFilterItem == null ? "" : curFilterItem.getFilterKey());
    }
}
