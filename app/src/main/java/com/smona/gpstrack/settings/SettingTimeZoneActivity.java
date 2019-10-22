package com.smona.gpstrack.settings;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.settings.adapter.TimeZoneAdapter;
import com.smona.gpstrack.settings.bean.TimeZoneItem;
import com.smona.gpstrack.settings.presenter.TimeZonePresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.PATH_TO_SETTING_TIMEZONE)
public class SettingTimeZoneActivity extends BasePresenterActivity<TimeZonePresenter, TimeZonePresenter.ITimeZoneView> implements TimeZonePresenter.ITimeZoneView {

    private TimeZoneAdapter adapter;
    private List<TimeZoneItem> itemList;

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

        RecyclerView recyclerView = findViewById(R.id.lanuageList);
        WidgetComponent.initRecyclerView(this, recyclerView);

        adapter = new TimeZoneAdapter(R.layout.adapter_item_setting);
        recyclerView.setAdapter(adapter);
        initLanuageData();
        adapter.setListener((item, pos) -> {
            if(item.isSelected()) {
                return;
            }
            showLoadingDialog();
            mPresenter.switchTimeZone(item);
        });
    }

    private void initLanuageData() {
        itemList = new ArrayList<>();

        TimeZoneItem item = new TimeZoneItem();
        item.setResId(R.string.jianti);
        item.setTimeZone(ParamConstant.LOCALE_ZH_CN);
        item.setSelected(item.getTimeZone().equals(ConfigCenter.getInstance().getConfigInfo().getTimeZone()));
        itemList.add(item);

        item = new TimeZoneItem();
        item.setResId(R.string.fanti);
        item.setTimeZone(ParamConstant.LOCALE_ZH_TW);
        item.setSelected(item.getTimeZone().equals(ConfigCenter.getInstance().getConfigInfo().getTimeZone()));
        itemList.add(item);

        item = new TimeZoneItem();
        item.setResId(R.string.english);
        item.setTimeZone(ParamConstant.LOCALE_EN);
        item.setSelected(item.getTimeZone().equals(ConfigCenter.getInstance().getConfigInfo().getTimeZone()));
        itemList.add(item);

        adapter.setNewData(itemList);
    }

    @Override
    public void onSwitchTimeZone(TimeZoneItem item) {
        hideLoadingDialog();
        ConfigCenter.getInstance().getConfigInfo().setLocale(item.getTimeZone());
        item.setSelected(true);
        for(TimeZoneItem timeZoneItem: itemList) {
            timeZoneItem.setSelected(timeZoneItem.getTimeZone().equals(item.getTimeZone()));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        ToastUtil.showShort(errorInfo.getMessage());
    }
}
