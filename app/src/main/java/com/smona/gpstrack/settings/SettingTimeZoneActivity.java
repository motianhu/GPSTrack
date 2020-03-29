package com.smona.gpstrack.settings;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.BaseLanuagePresenterActivity;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.DateFormatEvent;
import com.smona.gpstrack.settings.adapter.TimeZoneAdapter;
import com.smona.gpstrack.settings.bean.TimeZoneItem;
import com.smona.gpstrack.settings.presenter.TimeZonePresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;
import com.smona.gpstrack.util.TimeStamUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 时区设置页面
 */
@Route(path = ARouterPath.PATH_TO_SETTING_TIMEZONE)
public class SettingTimeZoneActivity extends BaseLanuagePresenterActivity<TimeZonePresenter, TimeZonePresenter.ITimeZoneView> implements TimeZonePresenter.ITimeZoneView {

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
        EditText editTExt = findViewById(R.id.edit_search_timezone);
        editTExt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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
            item.setSelected(item.getTimeZone().equalsIgnoreCase(ConfigCenter.getInstance().getConfigInfo().getTimeZone()));
            itemList.add(item);
        }

        adapter.setNewData(itemList);
    }

    @Override
    public void onSwitchTimeZone(TimeZoneItem item) {
        hideLoadingDialog();
        //刷新内存时区
        ConfigCenter.getInstance().getConfigInfo().setTimeZone(item.getTimeZone().toLowerCase());
        //持久化时区
        SPUtils.put(SPUtils.CONFIG_INFO, GsonUtil.objToJson(ConfigCenter.getInstance().getConfigInfo()));
        //通知时间格式变化
        NotifyCenter.getInstance().postEvent(new DateFormatEvent());
        finish();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        CommonUtils.showToastByFilter(errCode, errorInfo.getMessage());
    }
}
