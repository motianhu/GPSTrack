package com.smona.gpstrack.settings;

import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.settings.presenter.LanuagePresenter;
import com.smona.gpstrack.settings.presenter.MapPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.http.wrapper.ErrorInfo;

@Route(path = ARouterPath.PATH_TO_SETTING_MAP)
public class SettingMapActivity extends BasePresenterActivity<MapPresenter, MapPresenter.IMapView> implements MapPresenter.IMapView {


    private ImageView gaodeIv;
    private ImageView googeIv;

    @Override
    protected MapPresenter initPresenter() {
        return new MapPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_map;
    }

    @Override
    protected void initContentView() {
        super.initContentView();

        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.switchMap);
        findViewById(R.id.back).setOnClickListener(v -> finish());

        findViewById(R.id.gaodeMap).setOnClickListener(v -> clickGaode());
        findViewById(R.id.googleMap).setOnClickListener(v -> clickGoogle());

        gaodeIv = findViewById(R.id.selectGaode);
        googeIv = findViewById(R.id.selectGoogle);
    }

    private void clickGaode() {
        mPresenter.switchMap();
    }

    private void clickGoogle() {
        mPresenter.switchMap();
    }


    @Override
    public void onSwitchMap() {

    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
