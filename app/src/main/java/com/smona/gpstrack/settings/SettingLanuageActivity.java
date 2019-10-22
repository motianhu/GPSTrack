package com.smona.gpstrack.settings;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.settings.adapter.LanuageAdapter;
import com.smona.gpstrack.settings.bean.LanuageItem;
import com.smona.gpstrack.settings.presenter.LanuagePresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.PATH_TO_SETTING_LANUAGE)
public class SettingLanuageActivity extends BasePresenterActivity<LanuagePresenter, LanuagePresenter.ILanuageView> implements LanuagePresenter.ILanuageView {
    private LanuageAdapter lanuageAdapter;
    private List<LanuageItem> lanuageItemList;

    @Override
    protected LanuagePresenter initPresenter() {
        return new LanuagePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_lanuage;
    }

    @Override
    protected void initContentView() {
        super.initContentView();

        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.switchLanguage);
        findViewById(R.id.back).setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.lanuageList);
        WidgetComponent.initRecyclerView(this, recyclerView);

        lanuageAdapter = new LanuageAdapter(R.layout.adapter_item_setting);
        recyclerView.setAdapter(lanuageAdapter);
        initLanuageData();
        lanuageAdapter.setListener((item, pos) -> {
            if (item.isSelected()) {
                return;
            }
            showLoadingDialog();
            mPresenter.switchLanuage(item);
        });
    }

    private void initLanuageData() {
        lanuageItemList = new ArrayList<>();

        LanuageItem item = new LanuageItem();
        item.setResId(R.string.jianti);
        item.setLocale(ParamConstant.LOCALE_ZH_CN);
        item.setSelected(item.getLocale().equals(ConfigCenter.getInstance().getConfigInfo().getLocale()));
        lanuageItemList.add(item);

        item = new LanuageItem();
        item.setResId(R.string.fanti);
        item.setLocale(ParamConstant.LOCALE_ZH_TW);
        item.setSelected(item.getLocale().equals(ConfigCenter.getInstance().getConfigInfo().getLocale()));
        lanuageItemList.add(item);

        item = new LanuageItem();
        item.setResId(R.string.english);
        item.setLocale(ParamConstant.LOCALE_EN);
        item.setSelected(item.getLocale().equals(ConfigCenter.getInstance().getConfigInfo().getLocale()));
        lanuageItemList.add(item);

        lanuageAdapter.setNewData(lanuageItemList);
    }

    @Override
    public void onSwitchLanuage(LanuageItem item) {
        hideLoadingDialog();
        ConfigCenter.getInstance().getConfigInfo().setLocale(item.getLocale());
        item.setSelected(true);
        for (LanuageItem lanuageItem : lanuageItemList) {
            lanuageItem.setSelected(lanuageItem.getLocale().equals(item.getLocale()));
        }
        lanuageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        ToastUtil.showShort(errorInfo.getMessage());
    }
}
