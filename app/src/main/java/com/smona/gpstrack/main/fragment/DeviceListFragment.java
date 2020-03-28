package com.smona.gpstrack.main.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.smona.gpstrack.R;
import com.smona.gpstrack.common.BasePresenterLoadingFragment;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.device.bean.FilteItem;
import com.smona.gpstrack.device.dialog.SelectCommonDialog;
import com.smona.gpstrack.device.presenter.DeviceListPresenter;
import com.smona.gpstrack.main.adapter.DeviceAdapter;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.DeviceAddEvent;
import com.smona.gpstrack.notify.event.DeviceDelEvent;
import com.smona.gpstrack.notify.event.DeviceUpdateEvent;
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
 * 设备列表
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:34 PM
 */
public class DeviceListFragment extends BasePresenterLoadingFragment<DeviceListPresenter, DeviceListPresenter.IDeviceListView> implements DeviceListPresenter.IDeviceListView {

    private RecyclerView recyclerView;
    private DeviceAdapter deviceAdapter;

    private PopupWindow popupWindow;
    private String curFilterKey;

    private View filterView;

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
        filterView = content.findViewById(R.id.moreAction);
        filterView.setOnClickListener(view -> clickMoreAction());

        initExceptionProcess(content.findViewById(R.id.loadingresult), recyclerView);

        initPopwindow();
        NotifyCenter.getInstance().registerListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotifyCenter.getInstance().unRegisterListener(this);
    }

    private void initPopwindow() {
        popupWindow = new PopupWindow();
        View popContentView = View.inflate(mActivity, R.layout.filter_popwindow, null);
        popupWindow.setContentView(popContentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popContentView.findViewById(R.id.all).setOnClickListener(v -> {
            curFilterKey = "";
            requestFilter();
        });
        popContentView.findViewById(R.id.online).setOnClickListener(v -> {
            curFilterKey = Device.ONLINE;
            requestFilter();
        });
        popContentView.findViewById(R.id.offline).setOnClickListener(v -> {
            curFilterKey = Device.OFFLINE;
            requestFilter();
        });
        popContentView.findViewById(R.id.expired).setOnClickListener(v -> {
            curFilterKey = Device.INACTIVE;
            requestFilter();
        });
    }

    private void requestFilter() {
        showLoadingDialog();
        requestLocalDbList();
        popupWindow.dismiss();
    }

    @Override
    protected void requestData() {
        mPresenter.requestDeviceList();
    }

    private void requestLocalDbList() {
        mPresenter.requestDbDevices(curFilterKey);
    }

    private void clickAddDevice() {
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_ADD_DEVICE);
    }

    private void clickRefreshDevice() {
        showLoadingDialog();
        requestData();
    }

    private void clickMoreAction() {
        popupWindow.showAsDropDown(filterView,  0, -getResources().getDimensionPixelSize(R.dimen.dimen_20dp), Gravity.END);
    }

    @Override
    public void onSuccess(int curPage, List<Device> deviceList) {
        hideLoadingDialog();
        if (CommonUtils.isEmpty(deviceList) && curPage == 0) {
            doEmpty();
            return;
        }
        doSuccess();
        if (curPage == 0) {
            deviceAdapter.setNewData(deviceList);
        } else {
            deviceAdapter.addData(deviceList);
        }
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        onError(api, errCode, errorInfo, this::requestData);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgDelDevice(DeviceDelEvent event) {
        if(!isAdded()) {
            return;
        }
        if(deviceAdapter.getItemCount() == 1) {
            doEmpty();
        }
        deviceAdapter.removeDevice(event.getDeviceId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgAddDevice(DeviceAddEvent event) {
        if(!isAdded()) {
            return;
        }
        if(deviceAdapter.getItemCount() == 0) {
            doSuccess();
        }
        deviceAdapter.addDevice(event.getDevice());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgUpdateDevice(DeviceUpdateEvent event) {
        if(!isAdded()) {
            return;
        }
        deviceAdapter.updateDevice(event.getDevice());
    }
}
