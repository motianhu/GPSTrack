package com.smona.gpstrack.main.fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.data.MemoryDeviceManager;
import com.smona.gpstrack.device.bean.DeviceListBean;
import com.smona.gpstrack.main.fragment.attach.DeviceDetailFragment;
import com.smona.gpstrack.main.fragment.attach.IMapController;
import com.smona.gpstrack.main.fragment.attach.MapViewFragment;
import com.smona.gpstrack.main.poll.OnPollListener;
import com.smona.gpstrack.main.poll.RefreshPoll;
import com.smona.gpstrack.main.presenter.MapPresenter;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:30 PM
 */
public class MapContainerFragment extends BasePresenterFragment<MapPresenter, MapPresenter.IMapView> implements MapPresenter.IMapView {

    private DeviceDetailFragment deviceDetailFragment;
    private IMapController mapViewController;

    private TextView refreshCountDownTv;
    private RefreshPoll refreshPoll = new RefreshPoll();

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
        mapViewController = new MapViewFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapView, mapViewController.getMapFragment());
        fragmentTransaction.commitAllowingStateLoss();

        rootView.findViewById(R.id.searchDevices).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rootView.findViewById(R.id.previousDevice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDevicePart();
            }
        });

        rootView.findViewById(R.id.nextDevice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rootView.findViewById(R.id.location).setOnClickListener(view -> mapViewController.location());

        refreshCountDownTv = rootView.findViewById(R.id.refreshCountDown);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.requestDeviceList();
        refreshPoll.setParam(new OnPollListener() {
            @Override
            public void onFinish() {
                mPresenter.requestDeviceList();
                refreshTv(11);
                refreshPoll.starPoll();
            }

            @Override
            public void onTick(int mills) {
                refreshTv(mills - 1);
            }

            @Override
            public void cancle() {

            }
        });
        refreshPoll.starPoll();
    }

    private void refreshTv(int i) {
        String time = i + "";
        refreshCountDownTv.setText(time);
    }


    private void showDevicePart() {
        if (deviceDetailFragment == null) {
            deviceDetailFragment = new DeviceDetailFragment();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.anchorFragment, deviceDetailFragment);
            fragmentTransaction.commit();
        }
        deviceDetailFragment.setDevice(MemoryDeviceManager.getInstance().getDevice(0));
        deviceDetailFragment.showFragment();
    }

    /**
     * 切换fragment会调用
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 点击进其他Activity会调用
     */
    @Override
    public void onResume() {
        super.onResume();
        mapViewController.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapViewController.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapViewController.onPause();
    }

    @Override
    public void onSuccess(DeviceListBean deviceList) {
        MemoryDeviceManager.getInstance().addDeviceList(deviceList.getDatas());
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        ToastUtil.showShort(errorInfo.getMessage());
    }
}
