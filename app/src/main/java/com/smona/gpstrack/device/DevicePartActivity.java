package com.smona.gpstrack.device;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.util.ARouterPath;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/27/19 1:40 PM
 */
@Route(path = ARouterPath.PATH_TO_DEVICE_PART)
public class DevicePartActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_part);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
