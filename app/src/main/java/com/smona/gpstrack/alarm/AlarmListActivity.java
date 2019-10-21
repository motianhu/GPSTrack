package com.smona.gpstrack.alarm;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.main.fragment.AlarmListFragemnt;
import com.smona.gpstrack.util.ARouterPath;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/16/19 7:21 PM
 */

@Route(path = ARouterPath.PATH_TO_ALARM_LIST)
public class AlarmListActivity extends BaseActivity {

    private RespDevice device;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_list);
        initSeralize();
        initViews();

    }

    private void initViews() {
        AlarmListFragemnt alarmListFragemnt = (AlarmListFragemnt)getSupportFragmentManager().findFragmentById(R.id.alarmList);
        alarmListFragemnt.setDevice(device);
    }

    private void initSeralize() {
        Bundle bundle = getIntent().getBundleExtra(ARouterPath.PATH_TO_ALARM_LIST);
        if (bundle == null) {
            finish();
            return;
        }
        device = (RespDevice) bundle.getSerializable(ARouterPath.PATH_TO_ALARM_LIST);
        if (device == null) {
            finish();
        }
    }
}
