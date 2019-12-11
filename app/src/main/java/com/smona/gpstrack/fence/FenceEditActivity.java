package com.smona.gpstrack.fence;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.device.dialog.DeviceSelectedDialog;
import com.smona.gpstrack.device.dialog.HintCommonDialog;
import com.smona.gpstrack.device.dialog.TimeCommonDialog;
import com.smona.gpstrack.device.dialog.WeekSelectedDialog;
import com.smona.gpstrack.fence.bean.DeviceItem;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.fence.bean.TimeAlarm;
import com.smona.gpstrack.fence.bean.WeekItem;
import com.smona.gpstrack.fence.presenter.FenceEditPresenter;
import com.smona.gpstrack.map.IMap;
import com.smona.gpstrack.map.IMapView;
import com.smona.gpstrack.map.MapViewProxy;
import com.smona.gpstrack.map.listener.OnMapReadyListener;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
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
public class FenceEditActivity extends BasePresenterActivity<FenceEditPresenter, FenceEditPresenter.IGeoEditView> implements FenceEditPresenter.IGeoEditView, OnMapReadyListener {

    private static final String TAG = FenceEditActivity.class.getName();
    private FenceBean geoBean;
    private IMapView mMapView;
    private IMap aMap;

    private TextView radiusTv;
    private SeekBar seekbar;

    private View mainLayout;
    private CompoundButton enterRadio;
    private CompoundButton exitRadio;
    private EditText fenceNameTv;
    private TextView enterStartTimeTv;
    private TextView enterEndTimeTv;
    private TextView exitStartTimeTv;
    private TextView exitEndTimeTv;

    private HintCommonDialog hintCommonDialog;
    private TimeCommonDialog timeWheelDialog;

    private WeekSelectedDialog weekSelectedDialog;
    private List<WeekItem> weekItemList = new ArrayList<>();

    private DeviceSelectedDialog deviceSelectedDialog;
    private List<DeviceItem> deviceItemList;

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
            geoBean.setRadius(10);
            geoBean.setStatus(FenceBean.ACTIVE);
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
        CommonUtils.setMaxLenght(fenceNameTv, CommonUtils.MAX_NAME_LENGHT);
        enterStartTimeTv = mainLayout.findViewById(R.id.enterStartTime);
        enterEndTimeTv = mainLayout.findViewById(R.id.enterEndTime);
        exitStartTimeTv = mainLayout.findViewById(R.id.exitStartTime);
        exitEndTimeTv = mainLayout.findViewById(R.id.exitEndTime);
        mainLayout.findViewById(R.id.enterTime).setOnClickListener(v -> clickEnterTime());
        mainLayout.findViewById(R.id.exitTime).setOnClickListener(v -> clickExitTime());
        mainLayout.findViewById(R.id.repeatDate).setOnClickListener(v -> clickWeekRepeat());
        mainLayout.findViewById(R.id.selectDevice).setOnClickListener(v -> clickSelectDevice());
        mainLayout.findViewById(R.id.save_geo).setOnClickListener(v -> clickSaveGeo());

        if (TextUtils.isEmpty(geoBean.getId())) {
            return;
        }
        fenceNameTv.setText(geoBean.getName());
        enterRadio.setChecked(!CommonUtils.isEmpty(geoBean.getEntryAlarm()));
        if (!CommonUtils.isEmpty(geoBean.getEntryAlarm())) {
            enterStartTimeTv.setText(geoBean.getEntryAlarm().get(0).getFrom());
            enterEndTimeTv.setText(geoBean.getEntryAlarm().get(0).getTo());
        }
        exitRadio.setChecked(!CommonUtils.isEmpty(geoBean.getLeaveAlarm()));
        if (!CommonUtils.isEmpty(geoBean.getLeaveAlarm())) {
            exitStartTimeTv.setText(geoBean.getLeaveAlarm().get(0).getFrom());
            exitEndTimeTv.setText(geoBean.getLeaveAlarm().get(0).getTo());
        }
    }

    private void initMap() {
        radiusTv = findViewById(R.id.radius);
        seekbar = findViewById(R.id.seekbar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (aMap != null && aMap.getLatitude() != -1) {
                    int process;
                    if (i <= 10) {
                        process = 10;
                    } else {
                        process = i;
                    }
                    aMap.setRadius(process);
                    String radius = process + "m";
                    radiusTv.setText(radius);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mMapView = new MapViewProxy();
        mMapView.buildMap();

        FrameLayout frameLayout = findViewById(R.id.mapContainer);
        frameLayout.addView(mMapView.getMapView(this));
        mMapView.onCreate(null);
        mMapView.setMapReadyListener(this);
        initMapReady();
    }

    private void initMapReady() {
        aMap = mMapView.getMap();
        if (aMap != null) {
            double[] curLocation = aMap.getCurLocation();
            if (TextUtils.isEmpty(geoBean.getId())) {
                if (curLocation != null && CommonUtils.isInValidLatln(curLocation[0], curLocation[1])) {
                    geoBean.setLatitude(curLocation[0]);
                    geoBean.setLongitude(curLocation[1]);
                } else if (TextUtils.isEmpty(geoBean.getId())) {
                    geoBean.setLatitude(ParamConstant.DEFAULT_POS_LA);
                    geoBean.setLongitude(ParamConstant.DEFAULT_POS_LO);
                }
            }
            aMap.setOnMapClickListener();
            int radius = (int) geoBean.getRadius();
            seekbar.setProgress(radius);
            String radiusStr = radius + "m";
            radiusTv.setText(radiusStr);
            aMap.onAutoMapClick(geoBean);
        }
    }

    private void initDevice() {
        deviceSelectedDialog = new DeviceSelectedDialog(this, list -> {
            if(CommonUtils.isEmpty(deviceItemList)) {
                return;
            }
            if(CommonUtils.isEmpty(list)) {
                return;
            }
            DeviceSelectedDialog.copyValue(list, deviceItemList);
        });
    }

    private void initRepeat() {
        weekSelectedDialog = new WeekSelectedDialog(this, list -> WeekSelectedDialog.copyValue(list, this.weekItemList));
        WeekSelectedDialog.generWeekItem(this, weekItemList);

        if (TextUtils.isEmpty(geoBean.getId())) { //增加
            for (WeekItem item1 : weekItemList) {
                item1.setSelect(true);
            }
        } else { //编辑
            if (!CommonUtils.isEmpty(geoBean.getEntryAlarm())) {
                for (TimeAlarm timeAlarm : geoBean.getEntryAlarm()) {
                    for (WeekItem item1 : weekItemList) {
                        if (item1.getPos() == timeAlarm.getDay()) {
                            item1.setSelect(true);
                            break;
                        }
                    }
                }
            } else if (!CommonUtils.isEmpty(geoBean.getLeaveAlarm())) {
                for (TimeAlarm timeAlarm : geoBean.getLeaveAlarm()) {
                    for (WeekItem item1 : weekItemList) {
                        if (item1.getPos() == timeAlarm.getDay()) {
                            item1.setSelect(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void initDialog() {
        hintCommonDialog = new HintCommonDialog(this);
        timeWheelDialog = new TimeCommonDialog(this);
    }

    private void deleteFence() {
        hintCommonDialog.setHintIv(R.drawable.wrong);
        hintCommonDialog.setContent(getString(R.string.delete_fence));
        hintCommonDialog.setOnCommitListener((dialog, confirm) -> {
            dialog.dismiss();
            showLoadingDialog();
            mPresenter.deleteFence(geoBean);
        });
        hintCommonDialog.show();
    }

    private void clickWeekRepeat() {
        weekSelectedDialog.setWeekList(weekItemList);
        weekSelectedDialog.show();
    }

    private void clickSelectDevice() {
        deviceSelectedDialog.setDeviceList(deviceItemList);
        deviceSelectedDialog.show();
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
        if (aMap == null) {
            ToastUtil.showShort(R.string.geo_no_poi);
            return;
        }

        String fenceName = fenceNameTv.getText().toString();
        if (TextUtils.isEmpty(fenceName)) {
            ToastUtil.showShort(R.string.geo_no_name);
            return;
        }

        showLoadingDialog();

        List<TimeAlarm> enterTimes = new ArrayList<>();
        List<TimeAlarm> exitTimes = new ArrayList<>();
        TimeAlarm timeAlarm;
        for (WeekItem item : weekItemList) {
            if (item.isSelect()) {
                if (enterRadio.isChecked()) {
                    timeAlarm = new TimeAlarm();
                    timeAlarm.setDay(item.getPos());
                    timeAlarm.setFrom(enterStartTimeTv.getText().toString());
                    timeAlarm.setTo(enterEndTimeTv.getText().toString());
                    enterTimes.add(timeAlarm);
                }

                if (exitRadio.isChecked()) {
                    timeAlarm = new TimeAlarm();
                    timeAlarm.setDay(item.getPos());
                    timeAlarm.setFrom(exitStartTimeTv.getText().toString());
                    timeAlarm.setTo(exitEndTimeTv.getText().toString());
                    exitTimes.add(timeAlarm);
                }
            }
        }

        List<String> deviceIds = new ArrayList<>();
        for (DeviceItem item : deviceItemList) {
            if (item.isSelect()) {
                deviceIds.add(item.getId());
            }
        }

        geoBean.setName(fenceName);
        geoBean.setLatitude(aMap.getLatitude());
        geoBean.setLongitude(aMap.getLongitude());
        geoBean.setRadius(seekbar.getProgress());

        geoBean.setDevicePlatformIds(deviceIds);
        geoBean.setEntryAlarm(enterTimes);
        geoBean.setLeaveAlarm(exitTimes);

        if (TextUtils.isEmpty(geoBean.getId())) {
            mPresenter.requestAdd(geoBean);
        } else {
            mPresenter.requestUpdate(geoBean);
        }
    }

    private String getTwo(int i) {
        return i < 10 ? "0" + i : "" + i;
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
        CommonUtils.showToastByFilter(errCode, errorInfo.getMessage());
    }

    @Override
    public void onDeviceList(List<DeviceItem> deviceList) {
        if (CommonUtils.isEmpty(deviceList)) {
            deviceItemList = new ArrayList<>();
            deviceSelectedDialog.setDeviceList(new ArrayList<>());
        } else {
            deviceItemList = deviceList;

            if (TextUtils.isEmpty(geoBean.getId())) { //添加则全选
                for (DeviceItem deviceItem : deviceItemList) {
                    deviceItem.setSelect(true);
                }
            } else { //是编辑
                if (!CommonUtils.isEmpty(geoBean.getDevicePlatformIds())) {
                    for (String deviceId : geoBean.getDevicePlatformIds()) {
                        for (DeviceItem deviceItem : deviceItemList) {
                            if (deviceItem.getId().equals(deviceId)) {
                                deviceItem.setSelect(true);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDel() {
        back();
    }

    @Override
    public void onAdd() {
        back();
    }

    @Override
    public void onUpdate() {
        back();
    }

    private void back() {
        hideLoadingDialog();
        finish();
    }

    @Override
    public void onMapReady() {
        initMapReady();
    }
}
