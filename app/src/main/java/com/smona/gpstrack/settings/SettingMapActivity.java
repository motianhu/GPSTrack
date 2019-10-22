package com.smona.gpstrack.settings;

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
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Route(path = ARouterPath.PATH_TO_SETTING_MAP)
public class SettingMapActivity extends BasePresenterActivity<MapPresenter, MapPresenter.IMapView> implements MapPresenter.IMapView {

    private MapAdapter mapAdapter;
    private List<MapItem> mapItemList;

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

        mapAdapter = new MapAdapter(R.layout.adapter_item_setting);
        recyclerView.setAdapter(mapAdapter);
        initMapData();
        mapAdapter.setListener((item, pos) -> {
            if (item.isSelected()) {
                return;
            }
            showLoadingDialog();
            mPresenter.switchMap(item);
        });
    }

    private void initMapData() {
        mapItemList = new ArrayList<>();

        MapItem item = new MapItem();
        item.setResId(R.string.jianti);
        item.setMapDefault(ParamConstant.MAP_AMAP);
        item.setSelected(item.getMapDefault().equals(ConfigCenter.getInstance().getConfigInfo().getLocale()));
        mapItemList.add(item);

        item = new MapItem();
        item.setResId(R.string.fanti);
        item.setMapDefault(ParamConstant.MAP_GOOGLE);
        item.setSelected(item.getMapDefault().equals(ConfigCenter.getInstance().getConfigInfo().getLocale()));
        mapItemList.add(item);

        mapAdapter.setNewData(mapItemList);
    }

    @Override
    public void onSwitchMap(MapItem item) {
        hideLoadingDialog();
        ConfigCenter.getInstance().getConfigInfo().setMapDefault(item.getMapDefault());
        item.setSelected(true);
        for (MapItem mapItem : mapItemList) {
            mapItem.setSelected(mapItem.getMapDefault().equals(item.getMapDefault()));
        }
        mapAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        ToastUtil.showShort(errorInfo.getMessage());
    }
}
