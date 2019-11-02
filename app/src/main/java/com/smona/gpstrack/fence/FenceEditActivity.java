package com.smona.gpstrack.fence;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.device.dialog.EditCommonDialog;
import com.smona.gpstrack.device.dialog.HintCommonDialog;
import com.smona.gpstrack.device.dialog.TimeCommonDialog;
import com.smona.gpstrack.fence.adapter.FenceDeviceAdapter;
import com.smona.gpstrack.fence.adapter.WeekAdapter;
import com.smona.gpstrack.fence.bean.DeviceItem;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.fence.bean.TimeAlarm;
import com.smona.gpstrack.fence.bean.WeekItem;
import com.smona.gpstrack.fence.presenter.FenceEditPresenter;
import com.smona.gpstrack.map.MapAImpl;
import com.smona.gpstrack.util.AMapUtil;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.Constant;
import com.smona.gpstrack.util.SPUtils;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.logger.Logger;

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
public class FenceEditActivity extends BasePresenterActivity<FenceEditPresenter, FenceEditPresenter.IGeoEditView> implements FenceEditPresenter.IGeoEditView, AMap.OnMapClickListener {

    private static final String TAG = FenceEditActivity.class.getName();
    private FenceBean geoBean;
    private MapView mMapView;
    private AMap aMap;

    private TextView radiusTv;
    private SeekBar seekbar;

    private View mainLayout;
    private CompoundButton enterRadio;
    private CompoundButton exitRadio;
    private TextView fenceNameTv;
    private TextView enterStartTimeTv;
    private TextView enterEndTimeTv;
    private TextView exitStartTimeTv;
    private TextView exitEndTimeTv;

    private View repeatLayout;
    private RecyclerView weekRecycler;
    private WeekAdapter weekAdapter;

    private View deviceLayout;
    private RecyclerView devceRecycler;
    private FenceDeviceAdapter deviceAdapter;

    private HintCommonDialog hintCommonDialog;
    private EditCommonDialog editCommonDialog;

    private LatLng curClickLatLng;
    private Circle mCurFenceCircle;

    private TimeCommonDialog timeWheelDialog;

    private List<WeekItem> weekItems;
    private List<DeviceItem> deviceItems;

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
        if (geoBean == null) {
            geoBean = new FenceBean();
        }
        Logger.d(TAG, "geoBean: " + geoBean);
    }

    private void initHeader() {
        TextView titleTv = findViewById(R.id.title);
        if (TextUtils.isEmpty(geoBean.id)) {
            titleTv.setText(R.string.add_geo);
        } else {
            titleTv.setText(R.string.edit_geo);
        }
        findViewById(R.id.back).setOnClickListener(v -> finish());
        ImageView delete = findViewById(R.id.rightIv);
        delete.setImageResource(R.drawable.delete);
        delete.setVisibility(TextUtils.isEmpty(geoBean.getId()) ? View.GONE : View.VISIBLE);
        delete.setOnClickListener(v -> deleteFence());
    }

    private void initViews() {
        initMap();
        initMain();
        initRepeat();
        initDevice();
    }

    private void initMain() {
        mainLayout = findViewById(R.id.layout_fence_edit_main);
        mainLayout.setOnTouchListener((view, motionEvent) -> true);
        enterRadio = mainLayout.findViewById(R.id.enterRadio);
        exitRadio = mainLayout.findViewById(R.id.exitRadio);
        fenceNameTv = mainLayout.findViewById(R.id.geoName);
        enterStartTimeTv = mainLayout.findViewById(R.id.enterStartTime);
        enterEndTimeTv = mainLayout.findViewById(R.id.enterEndTime);
        exitStartTimeTv = mainLayout.findViewById(R.id.exitStartTime);
        exitEndTimeTv = mainLayout.findViewById(R.id.exitEndTime);
        mainLayout.findViewById(R.id.enterTime).setOnClickListener(v -> clickEnterTime());
        mainLayout.findViewById(R.id.exitTime).setOnClickListener(v -> clickExitTime());
        mainLayout.findViewById(R.id.repeatDate).setOnClickListener(v -> clickRepeat());
        mainLayout.findViewById(R.id.selectDevice).setOnClickListener(v -> clickSelectDevice());
        mainLayout.findViewById(R.id.geoInfo).setOnClickListener(v -> clickSetFenceName());
        mainLayout.findViewById(R.id.save_geo).setOnClickListener(v -> clickSaveGeo());

        if(TextUtils.isEmpty(geoBean.getId())) {
            return;
        }
        fenceNameTv.setText(geoBean.getName());
        enterRadio.setChecked(!CommonUtils.isEmpty(geoBean.getEntryAlarm()));
        if(!CommonUtils.isEmpty(geoBean.getEntryAlarm())) {
            enterStartTimeTv.setText(geoBean.getEntryAlarm().get(0).getFrom());
            enterEndTimeTv.setText(geoBean.getEntryAlarm().get(0).getTo());
        }
        exitRadio.setChecked(!CommonUtils.isEmpty(geoBean.getLeaveAlarm()));
        if(!CommonUtils.isEmpty(geoBean.getLeaveAlarm())) {
            exitStartTimeTv.setText(geoBean.getLeaveAlarm().get(0).getFrom());
            exitEndTimeTv.setText(geoBean.getLeaveAlarm().get(0).getTo());
        }
    }

    private void initMap() {
        radiusTv = findViewById(R.id.radius);
        seekbar = findViewById(R.id.seekbar);
        if (!TextUtils.isEmpty(geoBean.getId())) {
            seekbar.setProgress((int) geoBean.getRadius());
            radiusTv.setText(seekbar.getProgress() + "m");
        }
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mCurFenceCircle != null) {
                    if (i <= 10) {
                        mCurFenceCircle.setRadius(10);
                    } else {
                        mCurFenceCircle.setRadius(i);
                    }
                    radiusTv.setText((int)mCurFenceCircle.getRadius() + "m");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mMapView = findViewById(R.id.map);
        mMapView.onCreate(null);
        if (aMap == null) {
            aMap = mMapView.getMap();

            MapAImpl.initMap(aMap, AMapUtil.wgsToCjg(this, ParamConstant.DEFAULT_POS.latitude, ParamConstant.DEFAULT_POS.longitude));
            aMap.setOnMapClickListener(this);

            if(!TextUtils.isEmpty(geoBean.getId())) {
                LatLng latLng = AMapUtil.wgsToCjg(this, geoBean.getLatitude(), geoBean.getLongitude());
                onMapClick(latLng);
            }
        }
    }

    private void initDevice() {
        deviceLayout = findViewById(R.id.layout_fence_edit_device);
        deviceLayout.setOnTouchListener((view, motionEvent) -> true);
        devceRecycler = deviceLayout.findViewById(R.id.device_list);
        deviceAdapter = new FenceDeviceAdapter(R.layout.adapter_item_fence_device);
        WidgetComponent.initRecyclerView(this, devceRecycler);
        devceRecycler.setAdapter(deviceAdapter);

        deviceLayout.findViewById(R.id.back).setOnClickListener(v -> clickDeviceBack());
    }

    private void initRepeat() {
        repeatLayout = findViewById(R.id.layout_fence_edit_repeat);
        repeatLayout.setOnTouchListener((view, motionEvent) -> true);
        repeatLayout.findViewById(R.id.back).setOnClickListener(v -> clickRepeatBack());
        weekRecycler = repeatLayout.findViewById(R.id.week_list);
        weekAdapter = new WeekAdapter(R.layout.adapter_item_fence_week);
        WidgetComponent.initRecyclerView(this, weekRecycler);
        weekRecycler.setAdapter(weekAdapter);
        String[] weekDays = getResources().getStringArray(R.array.week_list);
        weekItems = new ArrayList<>();
        WeekItem item;
        for (int i = 0; i < weekDays.length; i++) {
            item = new WeekItem();
            item.setName(getString(R.string.week) + weekDays[i]);
            item.setPos(i + 1);
            weekItems.add(item);
        }

        if(!TextUtils.isEmpty(geoBean.getId()) && !CommonUtils.isEmpty(geoBean.getEntryAlarm())) {
            for(TimeAlarm timeAlarm: geoBean.getEntryAlarm()) {
                for(WeekItem item1: weekItems) {
                    if(item1.getPos() == timeAlarm.getDay()) {
                        item1.setSelect(true);
                        break;
                    }
                }
            }
        }
        weekAdapter.setNewData(weekItems);
    }

    private void initDialog() {
        hintCommonDialog = new HintCommonDialog(this);
        editCommonDialog = new EditCommonDialog(this);
        timeWheelDialog = new TimeCommonDialog(this);
    }

    private void deleteFence() {
        hintCommonDialog.setHintIv(R.drawable.wrong);
        hintCommonDialog.setContent(getString(R.string.delete_fence));
        hintCommonDialog.setOnCommitListener((dialog, confirm) -> {
            dialog.dismiss();
            showLoadingDialog();
            mPresenter.deleteFence(geoBean.getId());
        });
        hintCommonDialog.show();
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
        editCommonDialog.setTitle(getString(R.string.input_fence_name));
        editCommonDialog.setHint(getString(R.string.input_fence_name));
        editCommonDialog.setOnCommitListener((dialog, content) -> {
            dialog.dismiss();
            fenceNameTv.setText(content);
        });
        editCommonDialog.show();
    }

    private void clickEnterTime() {
        String time = enterStartTimeTv.getText().toString();
        String[] times = time.split(":");
        int initStartH = Integer.valueOf(times[0]);
        int initStartM = Integer.valueOf(times[1]);
        time = enterEndTimeTv.getText().toString();
        times = time.split(":");
        int initEndH = Integer.valueOf(times[0]);
        int initEndM = Integer.valueOf(times[1]);
        timeWheelDialog.setTimeBetween(initStartH, initStartM, initEndH, initEndM);
        timeWheelDialog.setOnCommitListener((dialog, startH, startM, endH, endM) -> {
            dialog.dismiss();
            String enterStartTime = getTwo(startH) + ":" + getTwo(startM);
            enterStartTimeTv.setText(enterStartTime);
            String enterEndTime = getTwo(endH) + ":" + getTwo(endM);
            enterEndTimeTv.setText(enterEndTime);

        });
        timeWheelDialog.show();
    }

    private void clickExitTime() {
        String time = exitStartTimeTv.getText().toString();
        String[] times = time.split(":");
        int initStartH = Integer.valueOf(times[0]);
        int initStartM = Integer.valueOf(times[1]);
        time = exitEndTimeTv.getText().toString();
        times = time.split(":");
        int initEndH = Integer.valueOf(times[0]);
        int initEndM = Integer.valueOf(times[1]);
        timeWheelDialog.setTimeBetween(initStartH, initStartM, initEndH, initEndM);
        timeWheelDialog.setOnCommitListener((dialog, startH, startM, endH, endM) -> {
            dialog.dismiss();
            String exitStartTime = getTwo(startH) + ":" + getTwo(startM);
            exitStartTimeTv.setText(exitStartTime);
            String exitEndTime = getTwo(endH) + ":" + getTwo(endM);
            exitEndTimeTv.setText(exitEndTime);
        });
        timeWheelDialog.show();
    }

    private void clickSaveGeo() {
        if (mCurFenceCircle == null) {
            ToastUtil.showShort(R.string.geo_no_poi);
            return;
        }

        String fenceName = fenceNameTv.getText().toString();
        if (TextUtils.isEmpty(fenceName)) {
            ToastUtil.showShort(R.string.geo_no_name);
            return;
        }

        List<TimeAlarm> enterTimes = new ArrayList<>();
        List<TimeAlarm> exitTimes = new ArrayList<>();
        TimeAlarm timeAlarm;
        for (WeekItem item : weekItems) {
            if (item.isSelect()) {
                if (enterRadio.isChecked()) {
                    timeAlarm = new TimeAlarm();
                    timeAlarm.setDay(item.getPos());
                    timeAlarm.setFrom(enterStartTimeTv.getText().toString());
                    timeAlarm.setTo(enterEndTimeTv.getText().toString());
                    enterTimes.add(timeAlarm);
                }

                if (enterRadio.isChecked()) {
                    timeAlarm = new TimeAlarm();
                    timeAlarm.setDay(item.getPos());
                    timeAlarm.setFrom(exitStartTimeTv.getText().toString());
                    timeAlarm.setTo(exitEndTimeTv.getText().toString());
                    exitTimes.add(timeAlarm);
                }
            }
        }

        List<String> deviceIds = new ArrayList<>();
        for (DeviceItem item : deviceItems) {
            if (item.isSelect()) {
                deviceIds.add(item.getId());
            }
        }

        geoBean.setName(fenceName);
        geoBean.setLatitude(mCurFenceCircle.getCenter().latitude);
        geoBean.setLongitude(mCurFenceCircle.getCenter().longitude);
        geoBean.setRadius(seekbar.getProgress());

        geoBean.setDevicePlatformIds(deviceIds);
        geoBean.setEntryAlarm(enterTimes);
        geoBean.setLeaveAlarm(exitTimes);

        if(TextUtils.isEmpty(geoBean.getId())) {
            mPresenter.requestAdd(geoBean);
        } else {
            mPresenter.requestUpdate(geoBean);
        }
    }

    private String getTwo(int i) {
        return i < 10 ? "0" + i : "" + i;
    }

    @Override
    public void onBackPressed() {
        if (repeatLayout.getVisibility() == View.VISIBLE) {
            repeatLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        } else if (deviceLayout.getVisibility() == View.VISIBLE) {
            deviceLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.requestAllDevice();
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
        hideLoadingDialog();
        ToastUtil.showShort(errorInfo.getMessage());
    }

    @Override
    public void onMapClick(LatLng latLng) {
        aMap.clear();
        mCurFenceCircle = null;
        curClickLatLng = latLng;
        mCurFenceCircle = MapAImpl.drawFence(aMap, latLng, seekbar.getProgress());
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    @Override
    public void onDeviceList(List<DeviceItem> deviceList) {
        if (deviceList == null || deviceList.isEmpty()) {
            deviceItems = new ArrayList<>();
            deviceAdapter.setNewData(new ArrayList<>());
        } else {
            deviceItems = deviceList;

            if(!TextUtils.isEmpty(geoBean.getId()) && !CommonUtils.isEmpty(geoBean.getDevicePlatformIds())) {
                for(String deviceId: geoBean.getDevicePlatformIds()) {
                    for(DeviceItem deviceItem: deviceItems) {
                        if(deviceItem.getId().equals(deviceId)) {
                            deviceItem.setSelect(true);
                            break;
                        }
                    }
                }
            }

            deviceAdapter.setNewData(deviceList);
        }
    }

    @Override
    public void onDel() {
        hideLoadingDialog();
        hintCommonDialog.setHintIv(R.drawable.wrong);
        hintCommonDialog.setContent(getString(R.string.dialog_title_del_success));
        hintCommonDialog.setOnCommitListener((dialog, confirm) -> {
            dialog.dismiss();
            finish();
        });
        hintCommonDialog.show();
    }

    @Override
    public void onAdd() {
        ToastUtil.showShort(R.string.geo_add_success);
        supportFinishAfterTransition();
    }

    @Override
    public void onUpdate() {
        ToastUtil.showShort(R.string.geo_update_success);
        supportFinishAfterTransition();
    }
}
