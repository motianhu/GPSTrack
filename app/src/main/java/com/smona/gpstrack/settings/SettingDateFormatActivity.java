package com.smona.gpstrack.settings;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.DateFormatEvent;
import com.smona.gpstrack.settings.adapter.DateFormatAdapter;
import com.smona.gpstrack.settings.bean.DateFormatItem;
import com.smona.gpstrack.settings.presenter.DateFormatPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;
import com.smona.http.wrapper.ErrorInfo;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.PATH_TO_SETTING_DATEFORMAT)
public class SettingDateFormatActivity extends BasePresenterActivity<DateFormatPresenter, DateFormatPresenter.IDateFormatView> implements DateFormatPresenter.IDateFormatView {

    private DateFormatAdapter adapter;
    private List<DateFormatItem> itemList;
    private DateFormatItem selectItem;

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

        RecyclerView recyclerView = findViewById(R.id.dateFormatList);
        WidgetComponent.initRecyclerView(this, recyclerView);

        adapter = new DateFormatAdapter(R.layout.adapter_item_setting);
        recyclerView.setAdapter(adapter);
        initDateFormatData();
        adapter.setListener((item, pos) -> {
            if (item.isSelected()) {
                return;
            }
            selectItem = item;
            for (DateFormatItem dateFormatItem : itemList) {
                dateFormatItem.setSelected(dateFormatItem.getDateFormat().equals(item.getDateFormat()));
            }
            adapter.notifyDataSetChanged();
        });

        findViewById(R.id.selectOk).setOnClickListener(v -> clickSelect());
    }

    private void initDateFormatData() {
        itemList = new ArrayList<>();

        String[] arrays = getResources().getStringArray(R.array.date_format_array);
        DateFormatItem item;
        for (String dateFormat : arrays) {
            item = new DateFormatItem();
            item.setDateFormat(dateFormat);
            item.setSelected(item.getDateFormat().equalsIgnoreCase(ConfigCenter.getInstance().getConfigInfo().getDateFormat()));
            itemList.add(item);
        }

        adapter.setNewData(itemList);
    }

    private void clickSelect() {
        if (selectItem == null) {
            return;
        }
        showLoadingDialog();
        mPresenter.switchDateFormat(selectItem);
    }

    @Override
    public void onSwitchDateFormat(DateFormatItem item) {
        hideLoadingDialog();
        ConfigCenter.getInstance().getConfigInfo().setDateFormat(item.getDateFormat());
        SPUtils.put(SPUtils.CONFIG_INFO, GsonUtil.objToJson(ConfigCenter.getInstance().getConfigInfo()));
        NotifyCenter.getInstance().postEvent(new DateFormatEvent());
        finish();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        CommonUtils.showToastByFilter(errCode, errorInfo.getMessage());
    }
}
