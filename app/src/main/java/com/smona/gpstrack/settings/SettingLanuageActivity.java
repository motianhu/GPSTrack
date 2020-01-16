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
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;
import com.smona.http.wrapper.ErrorInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 语言设置页
 */
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
        //刷新内存语言
        ConfigCenter.getInstance().getConfigInfo().setLocale(item.getLocale());
        //持久化语言
        SPUtils.put(SPUtils.CONFIG_INFO, GsonUtil.objToJson(ConfigCenter.getInstance().getConfigInfo()));
        //设置应用语言
        if (ParamConstant.LOCALE_ZH_CN.equals(item.getLocale())) {
            setAppLanguage(Locale.SIMPLIFIED_CHINESE);
        } else if (ParamConstant.LOCALE_ZH_TW.equals(item.getLocale())) {
            setAppLanguage(Locale.TAIWAN);
        } else {
            setAppLanguage(Locale.ENGLISH);
        }
        //关闭所有页面
        CommonUtils.sendCloseAllActivity(this);
        //启动首页并进入我的页面
        ARouterManager.getInstance().gotoActivityWithString(ARouterPath.PATH_TO_MAIN, ARouterPath.PATH_TO_MAIN, ARouterPath.PATH_TO_MAIN);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        CommonUtils.showToastByFilter(errCode, errorInfo.getMessage());
    }
}
