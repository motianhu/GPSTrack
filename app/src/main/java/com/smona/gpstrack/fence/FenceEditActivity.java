package com.smona.gpstrack.fence;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.device.dialog.EditCommonDialog;
import com.smona.gpstrack.fence.adapter.DeviceAdapter;
import com.smona.gpstrack.fence.adapter.WeekAdapter;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.fence.bean.WeekItem;
import com.smona.gpstrack.fence.presenter.FenceEditPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.Constant;
import com.smona.gpstrack.util.SPUtils;
import com.smona.http.wrapper.ErrorInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 1:59 PM
 */

@Route(path = ARouterPath.PATH_TO_EDIT_GEO)
public class FenceEditActivity extends BasePresenterActivity<FenceEditPresenter, FenceEditPresenter.IGeoEditView> implements FenceEditPresenter.IGeoEditView, AMap.OnMapClickListener{

    private FenceBean geoBean;
    private MapView mMapView;
    private AMap aMap;

    private SeekBar seekbar;

    private View mainLayout;
    private TextView fenceNameTv;


    private View repeatLayout;
    private RecyclerView weekRecycler;
    private WeekAdapter weekAdapter;

    private View deviceLayout;
    private RecyclerView devceRecycler;
    private DeviceAdapter deviceAdapter;

    private EditCommonDialog editCommonDialog;

    private LatLng curClickLatLng;
    private Circle mCurFenceCircle;

    @Override
    protected FenceEditPresenter initPresenter() {
        return new FenceEditPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_geo_edit;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        initSerialize();
        initHeader();
        initViews();
        initDialog();
    }

    private void initSerialize() {
        Bundle bundle = getIntent().getBundleExtra(ARouterPath.PATH_TO_EDIT_GEO);
        if (bundle != null) {
            geoBean = (FenceBean) bundle.getSerializable(FenceBean.class.getName());
        }
    }

    private void initHeader() {
        TextView titleTv = findViewById(R.id.title);
        titleTv.setText(R.string.edit_geo);
        findViewById(R.id.back).setOnClickListener(v -> finish());
    }

    private void initViews() {
        initMap();
        initMain();
        initRepeat();
        initDevice();
    }

    private void initMain() {
        mainLayout = findViewById(R.id.layout_fence_edit_main);
        fenceNameTv = mainLayout.findViewById(R.id.geoName);
        mainLayout.findViewById(R.id.repeatDate).setOnClickListener(v->clickRepeat());
        mainLayout.findViewById(R.id.selectDevice).setOnClickListener(v->clickSelectDevice());
        mainLayout.findViewById(R.id.geoInfo).setOnClickListener(v -> clickSetFenceName());
    }

    private void initMap() {
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(null);
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
            myLocationStyle.interval(2000);
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.getUiSettings().setMyLocationButtonEnabled(false);
            aMap.animateCamera(CameraUpdateFactory.changeLatLng(ParamConstant.DEFAULT_POS));
            aMap.setMyLocationEnabled(true);
            aMap.setOnMapClickListener(this);
            String language = (String) SPUtils.get(Constant.SP_KEY_LANGUAGE, Constant.VALUE_LANGUAGE_ZH_CN);
            if (Constant.VALUE_LANGUAGE_EN.equals(language)) {
                aMap.setMapLanguage(AMap.ENGLISH);
            } else {
                aMap.setMapLanguage(AMap.CHINESE);
            }
        }

        seekbar = findViewById(R.id.seekbar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mCurFenceCircle != null) {
                    mCurFenceCircle.setRadius(i*10);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initDevice() {
        deviceLayout = findViewById(R.id.layout_fence_edit_device);
        devceRecycler = deviceLayout.findViewById(R.id.device_list);
        deviceAdapter = new DeviceAdapter(R.layout.adapter_item_fence_device);
        WidgetComponent.initRecyclerView(this, devceRecycler);
        devceRecycler.setAdapter(deviceAdapter);

        deviceLayout.findViewById(R.id.back).setOnClickListener(v->clickDeviceBack());
    }

    private void initRepeat() {
        repeatLayout = findViewById(R.id.layout_fence_edit_repeat);
        weekRecycler = repeatLayout.findViewById(R.id.week_list);
        weekAdapter = new WeekAdapter(R.layout.adapter_item_fence_week);
        WidgetComponent.initRecyclerView(this, weekRecycler);
        weekRecycler.setAdapter(weekAdapter);
        String[] weekDays = getResources().getStringArray(R.array.week_list);
        List<WeekItem> weekItems = new ArrayList<>();
        WeekItem item;
        for (int i = 0; i < weekDays.length; i++) {
            item = new WeekItem();
            item.setName(getString(R.string.week) + weekDays[i]);
            item.setPos(i + 1);
            weekItems.add(item);
        }
        weekAdapter.setNewData(weekItems);
        repeatLayout.findViewById(R.id.back).setOnClickListener(v->clickRepeatBack());
    }

    private void initDialog() {
        editCommonDialog = new EditCommonDialog(this);
    }

    private void clickRepeat() {
        repeatLayout.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    private void clickRepeatBack() {
        repeatLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
    }

    private void clickSelectDevice() {
        deviceLayout.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    private void clickDeviceBack() {
        deviceLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
    }

    private void clickSetFenceName() {
        editCommonDialog.setHint(getString(R.string.input_fence_name));
        editCommonDialog.setOnCommitListener((dialog, content) -> {
            dialog.dismiss();
            fenceNameTv.setText(content);
        });
        editCommonDialog.show();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        aMap.clear();
        mCurFenceCircle = null;

        curClickLatLng = latLng;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.destination));
        markerOptions.position(latLng);
        aMap.addMarker(markerOptions);

        drawFence(10, latLng);
    }


    private void drawFence(double radius, LatLng latLng) {
        mCurFenceCircle = aMap.addCircle(new CircleOptions().
                center(latLng).
                radius(radius).
                strokeWidth(15));
    }
}
