package com.smona.gpstrack.settings;

import android.util.Log;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.settings.presenter.TimeZonePresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.TimeStamUtil;
import com.smona.http.wrapper.ErrorInfo;

@Route(path = ARouterPath.PATH_TO_SETTING_TIMEZONE)
public class SettingTimeZoneActivity extends BasePresenterActivity<TimeZonePresenter, TimeZonePresenter.ITimeZoneView> implements TimeZonePresenter.ITimeZoneView {
    private XRecyclerView xRecyclerView;

    @Override
    protected TimeZonePresenter initPresenter() {
        return new TimeZonePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_timezone;
    }

    @Override
    protected void initContentView() {
        super.initContentView();

        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.switchTimeZone);
        findViewById(R.id.back).setOnClickListener(v -> finish());


        Log.d("motianhu", "" + TimeStamUtil.getTimeZone().size());
        xRecyclerView = findViewById(R.id.xrecycler_wiget);

    }

    @Override
    public void onSwitchTimeZone() {

    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
