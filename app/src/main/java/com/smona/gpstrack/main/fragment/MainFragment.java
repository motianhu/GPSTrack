package com.smona.gpstrack.main.fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.AccountCenter;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.datacenter.DeviceListCenter;
import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.device.bean.DeviceAttLocBean;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.main.fragment.attach.AmapFragment;
import com.smona.gpstrack.main.fragment.attach.DevicePartFragment;
import com.smona.gpstrack.main.fragment.attach.DeviceSearchFragment;
import com.smona.gpstrack.main.fragment.attach.GoogleMapFragment;
import com.smona.gpstrack.main.fragment.attach.IMapCallback;
import com.smona.gpstrack.main.fragment.attach.IMapController;
import com.smona.gpstrack.main.poll.OnPollListener;
import com.smona.gpstrack.main.poll.RefreshPoll;
import com.smona.gpstrack.main.presenter.MapPresenter;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.AlarmUnReadDeviceEvent;
import com.smona.gpstrack.notify.event.DeviceDelEvent;
import com.smona.gpstrack.notify.event.FenceAddEvent;
import com.smona.gpstrack.notify.event.FenceAllEvent;
import com.smona.gpstrack.notify.event.FenceDelEvent;
import com.smona.gpstrack.notify.event.FenceUpdateEvent;
import com.smona.gpstrack.notify.event.ForgroudEvent;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * description:
 * 实时定位页
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:30 PM
 */
public class MainFragment extends BasePresenterFragment<MapPresenter, MapPresenter.IMapView> implements MapPresenter.IMapView, IMapCallback {

    private DevicePartFragment partFragment = new DevicePartFragment();
    private DeviceSearchFragment searchFragment = new DeviceSearchFragment();
    //地图控制器
    private IMapController mapViewController;

    private TextView refreshCountDownTv;
    private RefreshPoll refreshPoll = new RefreshPoll();
    private boolean isBackground = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map_main;
    }

    @Override
    protected MapPresenter initPresenter() {
        return new MapPresenter();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        //根据持久化的信息，构建地图对应的地图
        if (ParamConstant.MAP_GOOGLE.equals(ConfigCenter.getInstance().getConfigInfo().getMapDefault())) {
            mapViewController = new GoogleMapFragment();
        } else {
            mapViewController = new AmapFragment();
        }
        mapViewController.setMapCallback(this);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapView, mapViewController.getMapFragment());
        fragmentTransaction.commitAllowingStateLoss();

        rootView.findViewById(R.id.searchDevices).setOnClickListener(view -> showDeviceSearch());

        rootView.findViewById(R.id.leftDevice).setOnClickListener(view -> {
            mapViewController.leftDevice();
        });

        rootView.findViewById(R.id.rightDevice).setOnClickListener(view -> {
            mapViewController.rightDevice();
        });

        rootView.findViewById(R.id.location).setOnClickListener(view -> {
            mapViewController.location();
        });

        refreshCountDownTv = rootView.findViewById(R.id.refreshCountDown);
        NotifyCenter.getInstance().registerListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotifyCenter.getInstance().unRegisterListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        loopNetData();
        mPresenter.requestGeoList();
        refreshPoll.setParam(new OnPollListener() {
            @Override
            public void onFinish() {
                if (mPresenter != null) {
                    loopNetData();
                    refreshTv(AccountCenter.getInstance().getAccountInfo().getRefreshInterval());
                    refreshPoll.starPoll();
                }
            }

            @Override
            public void onTick(int mills) {
                refreshTv(mills);
            }

            @Override
            public void cancle() {

            }
        });
        refreshPoll.starPoll();
    }

    private void loopNetData() {
        mPresenter.requestUnRead("");
        mPresenter.requestDeviceList();
    }

    private void refreshTv(int i) {
        String time = i + "s";
        refreshCountDownTv.setText(time);
    }

    public boolean backpressed() {
        if (partFragment != null && partFragment.isVisible()) {
            partFragment.closeFragment();
            return true;
        }
        if (searchFragment != null && searchFragment.isVisible()) {
            searchFragment.closeFragment();
            return true;
        }
        return super.backpressed();
    }

    /**
     * 切换fragment会调用
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isAdded()) {
            return;
        }
        mapViewController.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 点击进其他Activity会调用
     */
    @Override
    public void onResume() {
        super.onResume();
        mapViewController.onResume();
        if(isBackground) {
            isBackground = false;
            loopNetData();
            refreshPoll.starPoll();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (refreshPoll != null) {
            refreshPoll.cancleTimer();
            refreshPoll = null;
        }
        mapViewController.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapViewController.onPause();
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onSuccess(int curPage, DeviceAttLocBean deviceList) {
        if (curPage == 0) {
            DeviceListCenter.getInstance().clearAll();
        }
        DeviceListCenter.getInstance().addData(deviceList.getDatas());
        refreshDevice(deviceList);
    }

    @Override
    public void onFenceList(List<Fence> fenceList) {
        if (fenceList == null || fenceList.isEmpty()) {
            return;
        }
        mapViewController.drawFences(fenceList);
    }

    private void refreshDevice(DeviceAttLocBean deviceList) {
        if (deviceList.getDatas().size() > 0) {
            mapViewController.drawDevices(deviceList.getDatas());
        }
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        CommonUtils.showToastByFilter(errCode, errorInfo.getError());
    }

    @Override
    public void clickMark(RespDevice device) {
        showDevicePart(device);
    }

    private void showDevicePart(RespDevice device) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.anchorFragment, partFragment);
        fragmentTransaction.commit();
        partFragment.setDevice(device);
        partFragment.showFragment();
    }

    private void showDeviceSearch() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.anchorFragment, searchFragment);
        fragmentTransaction.commit();
        searchFragment.setListener(device -> {
            for (RespDevice respDevice : DeviceListCenter.getInstance().getDeviceList()) {
                if (device.getId().equals(respDevice.getId())) {
                    if (respDevice.getLocation() == null) {
                        CommonUtils.showShort(mActivity, R.string.no_location);
                        break;
                    }
                    mapViewController.setCurDevice(respDevice);
                    break;
                }
            }
        });
        searchFragment.showFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgDelFence(FenceDelEvent event) {
        if (!isAdded()) {
            return;
        }
        mapViewController.removeFence(event.getFenceId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgAddFence(FenceAddEvent event) {
        if (!isAdded()) {
            return;
        }
        mapViewController.addFence(event.getAddFence());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgUpdateFence(FenceUpdateEvent event) {
        if (!isAdded()) {
            return;
        }
        mapViewController.updateFence(event.getUpdateFence());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgDelDevice(DeviceDelEvent event) {
        if (!isAdded()) {
            return;
        }
        DeviceListCenter.getInstance().removeDevice(event.getDeviceId());
        mapViewController.removeDevice(event.getDeviceId());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgUnReadDeviceAlarm(AlarmUnReadDeviceEvent event) {
        if (!isAdded()) {
            return;
        }
        mPresenter.requestUnRead(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgFenceAllEvent(FenceAllEvent event) {
        if (!isAdded()) {
            return;
        }
        mPresenter.requestFenceAll();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgWatchForground(ForgroudEvent event) {
        if (!isAdded()) {
            return;
        }
        Logger.e("motianhu", "bgWatchForground");
        isBackground = true;
        refreshPoll.cancleTimer();
    }
}
