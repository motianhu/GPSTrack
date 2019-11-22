package com.smona.gpstrack.settings;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.DateFormatEvent;
import com.smona.gpstrack.settings.adapter.TimeZoneAdapter;
import com.smona.gpstrack.settings.bean.TimeZoneItem;
import com.smona.gpstrack.settings.presenter.TimeZonePresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;
import com.smona.gpstrack.util.TimeStamUtil;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.PATH_TO_SETTING_TIMEZONE)
public class SettingTimeZoneActivity extends BasePresenterActivity<TimeZonePresenter, TimeZonePresenter.ITimeZoneView> implements TimeZonePresenter.ITimeZoneView {

    private TimeZoneAdapter adapter;
    private List<TimeZoneItem> itemList;
    private TimeZoneItem selectItem;

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

        RecyclerView recyclerView = findViewById(R.id.timeZoneList);
        WidgetComponent.initRecyclerView(this, recyclerView);

        adapter = new TimeZoneAdapter(R.layout.adapter_item_setting);
        recyclerView.setAdapter(adapter);
        initTimeZoneData();
        adapter.setListener((item, pos) -> {
            if (item.isSelected()) {
                return;
            }
            selectItem = item;
            for (TimeZoneItem timeZoneItem : itemList) {
                timeZoneItem.setSelected(timeZoneItem.getTimeZone().equals(item.getTimeZone()));
            }
            adapter.notifyDataSetChanged();
        });
        findViewById(R.id.selectOk).setOnClickListener(v -> clickSelect());
    }

    private void clickSelect() {
        if (selectItem == null) {
            return;
        }
        showLoadingDialog();
        mPresenter.switchTimeZone(selectItem);
    }

    private void initTimeZoneData() {
        itemList = new ArrayList<>();

        List<String> allTimeZone = TimeStamUtil.getTimeZone();
        TimeZoneItem item;
        for (String timeZone : allTimeZone) {
            item = new TimeZoneItem();
            item.setTimeZone(timeZone);
            item.setSelected(item.getTimeZone().equals(ConfigCenter.getInstance().getConfigInfo().getTimeZone()));
            itemList.add(item);
        }

        adapter.setNewData(itemList);
    }

    @Override
    public void onSwitchTimeZone(TimeZoneItem item) {
        hideLoadingDialog();
        ConfigCenter.getInstance().getConfigInfo().setTimeZone(item.getTimeZone());
        SPUtils.put(SPUtils.CONFIG_INFO, GsonUtil.objToJson(ConfigCenter.getInstance().getConfigInfo()));
        NotifyCenter.getInstance().postEvent(new DateFormatEvent());
        finish();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        ToastUtil.showShort(errorInfo.getMessage());
    }
}
