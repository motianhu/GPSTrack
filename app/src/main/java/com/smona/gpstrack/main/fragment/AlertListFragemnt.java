package com.smona.gpstrack.main.fragment;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.main.presenter.AlertListPresenter;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:35 PM
 */
public class AlertListFragemnt extends BasePresenterFragment<AlertListPresenter, AlertListPresenter.IView> implements AlertListPresenter.IView {
    @Override
    protected AlertListPresenter initPresenter() {
        return new AlertListPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_alert_list;
    }
}
