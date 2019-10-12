package com.smona.gpstrack.device;

import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.bean.req.ReqDeviceDetail;
import com.smona.gpstrack.device.presenter.DeviceDetailPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

@Route(path = ARouterPath.PATH_TO_DEVICE_DETAIL)
public class DeviceDetailActivity extends BasePresenterActivity<DeviceDetailPresenter, DeviceDetailPresenter.IDeviceDetailView> implements DeviceDetailPresenter.IDeviceDetailView {

    private String deviceId;

    @Override
    protected DeviceDetailPresenter initPresenter() {
        return new DeviceDetailPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_detail;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView titleTv = findViewById(R.id.title);
        titleTv.setText(R.string.deviceDetail);

        deviceId = getIntent().getStringExtra(ARouterPath.PATH_TO_DEVICE_DETAIL);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.deviceDetail(deviceId);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        ToastUtil.showShort(errorInfo.getMessage());
    }

    @Override
    public void onSuccess(ReqDeviceDetail deviceDetail) {
        ToastUtil.showShort("onSuccess  deviceDetail ");
    }
}
