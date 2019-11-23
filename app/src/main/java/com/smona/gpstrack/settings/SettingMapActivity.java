package com.smona.gpstrack.settings;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.settings.adapter.MapAdapter;
import com.smona.gpstrack.settings.bean.MapItem;
import com.smona.gpstrack.settings.presenter.MapPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.PATH_TO_SETTING_MAP)
public class SettingMapActivity extends BasePresenterActivity<MapPresenter, MapPresenter.IMapView> implements MapPresenter.IMapView {

    private MapAdapter adapter;
    private List<MapItem> itemList;
    private MapItem selectItem;

    @Override
    protected MapPresenter initPresenter() {
        return new MapPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_map;
    }

    @Override
    protected void initContentView() {
        super.initContentView();

        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.switchMap);
        findViewById(R.id.back).setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.mapList);
        WidgetComponent.initRecyclerView(this, recyclerView);

        adapter = new MapAdapter(R.layout.adapter_item_setting);
        recyclerView.setAdapter(adapter);
        initMapData();
        adapter.setListener((item, pos) -> {
            if (item.isSelected()) {
                return;
            }
            selectItem = item;
            for (MapItem mapItem : itemList) {
                mapItem.setSelected(mapItem.getMapDefault().equals(item.getMapDefault()));
            }
            adapter.notifyDataSetChanged();
        });
        findViewById(R.id.selectOk).setOnClickListener(v->clickSelect());
    }

    private void clickSelect() {
        if(selectItem == null) {
            return;
        }
        showLoadingDialog();
        mPresenter.switchMap(selectItem);
    }

    private void initMapData() {
        itemList = new ArrayList<>();

        MapItem item = new MapItem();
        item.setResId(R.string.gaodemap);
        item.setMapDefault(ParamConstant.MAP_AMAP);
        item.setSelected(item.getMapDefault().equals(ConfigCenter.getInstance().getConfigInfo().getMapDefault()));
        itemList.add(item);

        item = new MapItem();
        item.setResId(R.string.googlemap);
        item.setMapDefault(ParamConstant.MAP_GOOGLE);
        item.setSelected(item.getMapDefault().equals(ConfigCenter.getInstance().getConfigInfo().getMapDefault()));
        itemList.add(item);

        adapter.setNewData(itemList);
    }

    @Override
    public void onSwitchMap(MapItem item) {
        hideLoadingDialog();
        ConfigCenter.getInstance().getConfigInfo().setMapDefault(item.getMapDefault());
        SPUtils.put(SPUtils.CONFIG_INFO, GsonUtil.objToJson(ConfigCenter.getInstance().getConfigInfo()));
        sendCloseAllActivity();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        CommonUtils.showToastByFilter(errCode, errorInfo.getMessage());
    }

    private void sendCloseAllActivity() {
        Intent closeAllIntent = new Intent(ACTION_BASE_ACTIVITY);
        closeAllIntent.putExtra(ACTION_BASE_ACTIVITY_EXIT_KEY, ACTION_BASE_ACTIVITY_EXIT_VALUE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(closeAllIntent);
        ARouterManager.getInstance().gotoActivityWithString(ARouterPath.PATH_TO_MAIN, ARouterPath.PATH_TO_MAIN, ARouterPath.PATH_TO_MAIN);
    }

}
