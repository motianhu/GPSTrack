package com.smona.gpstrack.alarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

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
        AlarmListFragemnt alarmListFragemnt = AlarmListFragemnt.newInstance(device);
        replaceFragment(R.id.llRootView, alarmListFragemnt);
    }

    protected void replaceFragment(int fragmentresId, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(fragmentresId, fragment);
        ft.commitAllowingStateLoss();
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
