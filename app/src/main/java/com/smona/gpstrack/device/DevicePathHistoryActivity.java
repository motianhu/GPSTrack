package com.smona.gpstrack.device;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.calendar.fragment.CalendarSelectFragment;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.db.table.Location;
import com.smona.gpstrack.device.bean.AvatarItem;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.device.presenter.DeviceHistoryPresenter;
import com.smona.gpstrack.map.IMap;
import com.smona.gpstrack.map.IMapView;
import com.smona.gpstrack.map.MapViewProxy;
import com.smona.gpstrack.map.listener.OnMapReadyListener;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.TimeStamUtil;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/29/19 12:59 PM
 */

@Route(path = ARouterPath.PATH_TO_DEVICE_HISTORY)
public class DevicePathHistoryActivity extends BasePresenterActivity<DeviceHistoryPresenter, DeviceHistoryPresenter.IDeviceHistory> implements DeviceHistoryPresenter.IDeviceHistory, OnMapReadyListener {

    private ImageView device_icon;
    private TextView device_name;
    private TextView device_id;

    private TextView oneHour;
    private TextView twoHour;
    private TextView sixHour;
    private TextView today;
    private TextView otherDay;

    private IMapView mMapView;
    private IMap aMap;

    private RespDevice device;

    private CalendarSelectFragment calendarSelectFragment;

    @Override
    protected DeviceHistoryPresenter initPresenter() {
        return new DeviceHistoryPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_location_hitory;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        initSeralize();
        initHeader();
        initViews();
        initCalendar();
    }

    private void initSeralize() {
        Bundle bundle = getIntent().getBundleExtra(ARouterPath.PATH_TO_DEVICE_HISTORY);
        if (bundle == null) {
            finish();
            return;
        }
        device = (RespDevice) bundle.getSerializable(ARouterPath.PATH_TO_DEVICE_HISTORY);
        if (device == null) {
            finish();
        }
    }

    private void initHeader() {
        findViewById(R.id.back).setOnClickListener(view -> finish());
        TextView titleTv = findViewById(R.id.title);
        titleTv.setText(R.string.pathHistory);
    }

    private void initViews() {
        initMap();

        device_icon = findViewById(R.id.device_icon);
        AvatarItem.showDeviceIcon(device.getId(), device_icon);
        device_name = findViewById(R.id.device_name);
        device_name.setText(device.getName());
        device_id = findViewById(R.id.device_id);
        device_id.setText(device.getId());

        oneHour = findViewById(R.id.oneHourTv);
        oneHour.setSelected(true);
        oneHour.setOnClickListener(v -> clickHour(1));
        twoHour = findViewById(R.id.twoHourTv);
        twoHour.setOnClickListener(v -> clickHour(2));
        sixHour = findViewById(R.id.sixHourTv);
        sixHour.setOnClickListener(v -> clickHour(6));
        today = findViewById(R.id.todayTv);
        today.setOnClickListener(v -> clickDay(3));
        otherDay = findViewById(R.id.otherTv);
        otherDay.setOnClickListener(v -> clickDay(4));
    }

    private void initMap() {
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
            aMap.animateCamera(ParamConstant.DEFAULT_POS_LA, ParamConstant.DEFAULT_POS_LO);
        }
    }

    private void initCalendar() {
        calendarSelectFragment = new CalendarSelectFragment(true, dayTimeInfo -> {
            calendarSelectFragment.hide(true);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = dayTimeInfo.getYear() + "-" + dayTimeInfo.getMonth() + "-" + dayTimeInfo.getDay();
            otherDay.setText(date);
            String start =date  + " 00:00:00";
            String end =date + " 23:59:59";
            try {
                long startTime = simpleDateFormat.parse(start).getTime();
                long endTime = simpleDateFormat.parse(end).getTime();
                mPresenter.requestHistoryLocation(device.getId(), startTime + "", endTime + "");
            } catch (ParseException e) {
                e.printStackTrace();
            }


        },R.string.select_device_date);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.calendar_container, calendarSelectFragment);
        fragmentTransaction.commit();
    }


    private void clickHour(int hour) {
        if(hour == 1) {
            refreshUI(0, oneHour, twoHour, sixHour, today, otherDay);
        } else if(hour == 2) {
            refreshUI(1, oneHour, twoHour, sixHour, today, otherDay);
        } else if(hour == 6) {
            refreshUI(2, oneHour, twoHour, sixHour, today, otherDay);
        }
        long curTime = System.currentTimeMillis();
        long preTime = TimeStamUtil.getBeforeByHourTime(hour);
        requestHistoryLocation(preTime, curTime);
    }

    private void refreshUI(int pos, View... view) {
        int i = 0;
        for(View v: view) {
            v.setSelected(pos == i);
            i++;
        }

        if(!otherDay.isSelected()) {
            otherDay.setText(R.string.other_day);
        }
    }

    private void clickDay(int day) {
        refreshUI(day, oneHour, twoHour, sixHour, today, otherDay);
        if(day == 3) {
            long startTime = TimeStamUtil.getToday0();
            long endTime = System.currentTimeMillis();
            mPresenter.requestHistoryLocation(device.getId(), startTime + "", endTime + "");
        } else {
            calendarSelectFragment.show(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (calendarSelectFragment != null && calendarSelectFragment.isVisible()) {
            calendarSelectFragment.hide(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        clickHour(1);
    }

    private void requestHistoryLocation(long startTime, long endTime) {
        showLoadingDialog();
        mPresenter.requestHistoryLocation(device.getId(), startTime + "", endTime + "");
    }

    @Override
    public void onSuccess(List<Location> datas) {
        hideLoadingDialog();
        drawTrackOnMap(datas);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        ToastUtil.showShort(errorInfo.getMessage());
        aMap.clear();
    }

    private void drawTrackOnMap(List<Location> points) {
        if(CommonUtils.isEmpty(points)) {
            return;
        }
        aMap.clear();
        aMap.drawTrack(points);
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
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onMapReady() {
        initMapReady();
    }
}
