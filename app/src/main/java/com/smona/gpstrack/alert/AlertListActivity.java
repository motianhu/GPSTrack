package com.smona.gpstrack.alert;

import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.alert.presenter.AlertListPresenter;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/16/19 7:21 PM
 */
public class AlertListActivity extends BasePresenterActivity<AlertListPresenter, AlertListPresenter.IAlertListView> implements AlertListPresenter.IAlertListView {
    @Override
    protected AlertListPresenter initPresenter() {
        return new AlertListPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alert_list;
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
