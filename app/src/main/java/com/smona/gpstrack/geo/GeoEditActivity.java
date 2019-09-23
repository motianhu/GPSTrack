package com.smona.gpstrack.geo;

import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.geo.presenter.GeoEditPresenter;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 1:59 PM
 */
public class GeoEditActivity extends BasePresenterActivity<GeoEditPresenter, GeoEditPresenter.IGeoEditView> implements GeoEditPresenter.IGeoEditView {
    @Override
    protected GeoEditPresenter initPresenter() {
        return new GeoEditPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_geo_edit;
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
