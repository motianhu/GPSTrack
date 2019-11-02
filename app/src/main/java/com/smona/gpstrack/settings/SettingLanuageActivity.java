package com.smona.gpstrack.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Route(path = ARouterPath.PATH_TO_SETTING_LANUAGE)
public class SettingLanuageActivity extends BasePresenterActivity<LanuagePresenter, LanuagePresenter.ILanuageView> implements LanuagePresenter.ILanuageView {

    private LanuageAdapter adapter;
    private List<LanuageItem> itemList;

    private LanuageItem selectItem;

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

        adapter = new LanuageAdapter(R.layout.adapter_item_setting);
        recyclerView.setAdapter(adapter);
        initLanuageData();
        adapter.setListener((item, pos) -> {
            if (item.isSelected()) {
                return;
            }
            selectItem = item;
            for (LanuageItem lanuageItem : itemList) {
                lanuageItem.setSelected(lanuageItem.getLocale().equals(item.getLocale()));
            }
            adapter.notifyDataSetChanged();
        });
        findViewById(R.id.selectOk).setOnClickListener(v->clickSelect());
    }

    private void initLanuageData() {
        itemList = new ArrayList<>();

        LanuageItem item = new LanuageItem();
        item.setResId(R.string.jianti);
        item.setLocale(ParamConstant.LOCALE_ZH_CN);
        item.setSelected(item.getLocale().equals(ConfigCenter.getInstance().getConfigInfo().getLocale()));
        itemList.add(item);

        item = new LanuageItem();
        item.setResId(R.string.fanti);
        item.setLocale(ParamConstant.LOCALE_ZH_TW);
        item.setSelected(item.getLocale().equals(ConfigCenter.getInstance().getConfigInfo().getLocale()));
        itemList.add(item);

        item = new LanuageItem();
        item.setResId(R.string.english);
        item.setLocale(ParamConstant.LOCALE_EN);
        item.setSelected(item.getLocale().equals(ConfigCenter.getInstance().getConfigInfo().getLocale()));
        itemList.add(item);

        adapter.setNewData(itemList);
    }

    private void clickSelect() {
        if(selectItem == null) {
            return;
        }
        showLoadingDialog();
        mPresenter.switchLanuage(selectItem);
    }

    @Override
    public void onSwitchLanuage(LanuageItem item) {
        hideLoadingDialog();
        ConfigCenter.getInstance().getConfigInfo().setLocale(item.getLocale());
        SPUtils.put(SPUtils.CONFIG_INFO, GsonUtil.objToJson(ConfigCenter.getInstance().getConfigInfo()));
        if(item.getLocale().equals(ParamConstant.LOCALE_EN)) {
            switchLanguage(Locale.ENGLISH);
        } else  if(item.getLocale().equals(ParamConstant.LOCALE_ZH_CN)) {
            switchLanguage(Locale.SIMPLIFIED_CHINESE);
        } else  if(item.getLocale().equals(ParamConstant.LOCALE_ZH_TW)) {
            switchLanguage(Locale.TAIWAN);
        }
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        ToastUtil.showShort(errorInfo.getMessage());
    }

    private void switchLanguage(Locale locale) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, metrics);

        sendCloseAllActivity();
    }

    private void sendCloseAllActivity() {
        Intent closeAllIntent = new Intent(ACTION_BASE_ACTIVITY);
        closeAllIntent.putExtra(ACTION_BASE_ACTIVITY_EXIT_KEY, ACTION_BASE_ACTIVITY_EXIT_VALUE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(closeAllIntent);
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_SPLASH);
    }
}
