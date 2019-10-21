package com.smona.gpstrack.settings;

import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.settings.presenter.DateFormatPresenter;
import com.smona.gpstrack.settings.presenter.MapPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.http.wrapper.ErrorInfo;

@Route(path = ARouterPath.PATH_TO_SETTING_DATEFORMAT)
public class SettingDateFormatActivity extends BasePresenterActivity<DateFormatPresenter, DateFormatPresenter.IDateFormatView> implements DateFormatPresenter.IDateFormatView {

    private ImageView ddmmxIv;
    private ImageView mmddxIv;
    private ImageView yyyyxIv;

    private ImageView ddmmhIv;
    private ImageView mmddhIv;

    @Override
    protected DateFormatPresenter initPresenter() {
        return new DateFormatPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_dateformat;
    }

    @Override
    protected void initContentView() {
        super.initContentView();

        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.switchDate);
        findViewById(R.id.back).setOnClickListener(v -> finish());

        findViewById(R.id.ddmmx).setOnClickListener(v -> clickDDMMX());
        findViewById(R.id.mmddx).setOnClickListener(v -> clickMMDDX());
        findViewById(R.id.yyyyx).setOnClickListener(v -> clickDDMMX());
        findViewById(R.id.ddmmh).setOnClickListener(v -> clickMMDDX());
        findViewById(R.id.mmddh).setOnClickListener(v -> clickDDMMX());

        ddmmxIv = findViewById(R.id.selectddmmx);
        mmddxIv = findViewById(R.id.selectmmddx);
        yyyyxIv = findViewById(R.id.selectyyyyx);

        ddmmhIv = findViewById(R.id.selectddmmh);
        mmddhIv = findViewById(R.id.selectmmddh);
    }

    private void clickDDMMX() {

    }

    private void clickMMDDX() {

    }


    @Override
    public void onSwitchDateFormat() {

    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
