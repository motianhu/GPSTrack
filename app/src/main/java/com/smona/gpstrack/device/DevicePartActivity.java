package com.smona.gpstrack.device;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;

/**
 * description:
 * 设备部分信息。
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/27/19 1:40 PM
 */
@Deprecated
@Route(path = ARouterPath.PATH_TO_DEVICE_PART)
public class DevicePartActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_part);
        findViewById(R.id.routeHistory).setOnClickListener(v -> ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_DEVICE_HISTORY));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
