package com.smona.gpstrack.settings;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.settings.adapter.DateFormatAdapter;
import com.smona.gpstrack.settings.bean.DateFormatItem;
import com.smona.gpstrack.settings.presenter.DateFormatPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.PATH_TO_SETTING_DATEFORMAT)
public class SettingDateFormatActivity extends BasePresenterActivity<DateFormatPresenter, DateFormatPresenter.IDateFormatView> implements DateFormatPresenter.IDateFormatView {

    private DateFormatAdapter adapter;
    private List<DateFormatItem> itemList;

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

        RecyclerView recyclerView = findViewById(R.id.lanuageList);
        WidgetComponent.initRecyclerView(this, recyclerView);

        adapter = new DateFormatAdapter(R.layout.adapter_item_setting);
        recyclerView.setAdapter(adapter);
        initLanuageData();
        adapter.setListener((item, pos) -> {
            if (item.isSelected()) {
                return;
            }
            showLoadingDialog();
            mPresenter.switchDateFormat(item);
        });
    }

    private void initLanuageData() {
        itemList = new ArrayList<>();

        DateFormatItem item = new DateFormatItem();
        item.setResId(R.string.jianti);
        item.setDateFormat(ParamConstant.LOCALE_ZH_CN);
        item.setSelected(item.getDateFormat().equals(ConfigCenter.getInstance().getConfigInfo().getTimeZone()));
        itemList.add(item);

        item = new DateFormatItem();
        item.setResId(R.string.fanti);
        item.setDateFormat(ParamConstant.LOCALE_ZH_TW);
        item.setSelected(item.getDateFormat().equals(ConfigCenter.getInstance().getConfigInfo().getTimeZone()));
        itemList.add(item);

        item = new DateFormatItem();
        item.setResId(R.string.english);
        item.setDateFormat(ParamConstant.LOCALE_EN);
        item.setSelected(item.getDateFormat().equals(ConfigCenter.getInstance().getConfigInfo().getTimeZone()));
        itemList.add(item);

        adapter.setNewData(itemList);
    }

    @Override
    public void onSwitchDateFormat(DateFormatItem item) {
        hideLoadingDialog();
        ConfigCenter.getInstance().getConfigInfo().setLocale(item.getDateFormat());
        item.setSelected(true);
        for (DateFormatItem dateFormatItem : itemList) {
            dateFormatItem.setSelected(dateFormatItem.getDateFormat().equals(item.getDateFormat()));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        ToastUtil.showShort(errorInfo.getMessage());
    }
}
