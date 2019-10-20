package com.smona.gpstrack.device;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.presenter.DeviceAddPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

@Route(path = ARouterPath.PATH_TO_ADD_DEVICE)
public class DeviceAddActivity extends BasePresenterActivity<DeviceAddPresenter, DeviceAddPresenter.IDeviceAddView> implements DeviceAddPresenter.IDeviceAddView {

    private EditText deviceNameEt;
    private EditText deviceIdEt;
    private EditText deviceOrderNoEt;

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

        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.add_device);
        findViewById(R.id.back).setOnClickListener(v-> finish());

        View rightIv = findViewById(R.id.rightIv);
        rightIv.setVisibility(View.VISIBLE);
        rightIv.setOnClickListener(v-> clickScan());

        deviceIdEt = findViewById(R.id.device_id);
        deviceNameEt = findViewById(R.id.device_name);
        deviceOrderNoEt = findViewById(R.id.device_order_no);
        findViewById(R.id.device_add).setOnClickListener(v -> clickAddDevice());
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void clickAddDevice() {
        String deviceId = deviceIdEt.getText().toString();
        String deviceName = deviceNameEt.getText().toString();
        String deviceOrderNo = deviceOrderNoEt.getText().toString();

        if(TextUtils.isEmpty(deviceId)) {
            ToastUtil.showShort(R.string.toast_device_id_empty);
            return;
        } else if(TextUtils.isEmpty(deviceName)) {
            ToastUtil.showShort(R.string.toast_device_name_empty);
            return;
        } else if(TextUtils.isEmpty(deviceOrderNo)) {
            ToastUtil.showShort(R.string.toast_device_orderno_empty);
            return;
        }
        showLoadingDialog();
        mPresenter.addDevice(deviceId, deviceName, deviceOrderNo);
    }

    private void clickScan() {
        ARouterManager.getInstance().gotoActivityForResult(ARouterPath.PATH_TO_SCAN, this, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == RESULT_OK) {
            String deviceid = data.getStringExtra(ARouterPath.PATH_TO_SCAN);
            refreshUI(deviceid);
        }
    }

    private void refreshUI(String deviceId) {
        deviceIdEt.setText(deviceId);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        ToastUtil.showShort(errorInfo.getMessage());
    }

    @Override
    public void onSuccess() {
        hideLoadingDialog();
        ToastUtil.showShort("add success");
        finish();
    }
}
