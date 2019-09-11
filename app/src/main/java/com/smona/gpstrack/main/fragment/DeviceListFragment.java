package com.smona.gpstrack.main.fragment;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.main.presenter.DeviceListPresenter;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:34 PM
 */
public class DeviceListFragment extends BasePresenterFragment<DeviceListPresenter, DeviceListPresenter.IView> implements DeviceListPresenter.IView {
    @Override
    protected DeviceListPresenter initPresenter() {
        return new DeviceListPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_device_list;
    }
}
