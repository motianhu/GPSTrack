package com.smona.gpstrack.device;

import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.presenter.DeviceAddPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

@Route(path = ARouterPath.PATH_TO_ADD_DEVICE)
public class DeviceAddActivity extends BasePresenterActivity<DeviceAddPresenter, DeviceAddPresenter.IDeviceAddView> implements DeviceAddPresenter.IDeviceAddView {

    private EditText devieNameEt;
    private EditText devieIdEt;
    private EditText devieOrderNoEt;

    @Override
    protected DeviceAddPresenter initPresenter() {
        return new DeviceAddPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_add;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        devieIdEt = findViewById(R.id.device_id);
        devieNameEt = findViewById(R.id.device_name);
        devieOrderNoEt = findViewById(R.id.device_order_no);
        findViewById(R.id.device_add).setOnClickListener(v -> clickAddDevice());
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void clickAddDevice() {
        String deviceId = devieIdEt.getText().toString();
        String deviceName = devieNameEt.getText().toString();
        String deviceOrderNo = devieOrderNoEt.getText().toString();
        mPresenter.addDevice(deviceId, deviceName, deviceOrderNo);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        ToastUtil.showShort(errorInfo.getMessage());
    }

    @Override
    public void onSuccess() {
        ToastUtil.showShort("add success");
    }
}
