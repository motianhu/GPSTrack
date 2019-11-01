package com.smona.gpstrack.device;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.BasePresenterLoadingActivity;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.device.bean.req.ReqDeviceDetail;
import com.smona.gpstrack.device.bean.req.ShareInfo;
import com.smona.gpstrack.device.dialog.EditCommonDialog;
import com.smona.gpstrack.device.dialog.HintCommonDialog;
import com.smona.gpstrack.device.dialog.ListCommonDialog;
import com.smona.gpstrack.device.presenter.DeviceDetailPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.TimeStamUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.List;

@Route(path = ARouterPath.PATH_TO_DEVICE_DETAIL)
public class DeviceDetailActivity extends BasePresenterLoadingActivity<DeviceDetailPresenter, DeviceDetailPresenter.IDeviceDetailView> implements DeviceDetailPresenter.IDeviceDetailView {

    private String deviceId;

    private ImageView deviceIcon;
    private TextView deviceName;
    private TextView deviceOwner;
    private TextView expireDate;
    private TextView onLineDate;
    private TextView status;

    private SwitchCompat sosAlarm;
    private SwitchCompat batteryAlarm;
    private SwitchCompat tamperAlarm;
    private TextView voiveAlarm;

    private LinearLayout phoneListLL;
    private LinearLayout shareListLL;

    private HintCommonDialog hintCommonDialog;
    private EditCommonDialog editCommonDialog;
    private ListCommonDialog listCommonDialog;

    private ReqDeviceDetail deviceDetail;

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
        deviceId = getIntent().getStringExtra(ARouterPath.PATH_TO_DEVICE_DETAIL);
        initHeader();
        initViews();
    }

    private void initViews() {
        deviceIcon = findViewById(R.id.logo_iv);
        deviceName = findViewById(R.id.device_name);
        deviceOwner = findViewById(R.id.device_owner);
        expireDate = findViewById(R.id.expireDate);
        onLineDate = findViewById(R.id.onLineDate);
        status = findViewById(R.id.deviceStatus);

        findViewById(R.id.modifyDeviceName).setOnClickListener(v -> clickModifyDeviceName());
        findViewById(R.id.avatarModify).setOnClickListener(v -> clickModifyIcon());

        batteryAlarm = findViewById(R.id.batteryAlarm);
        batteryAlarm.setOnClickListener(v -> clickChecked(ReqDeviceDetail.BAT_ALARM, !deviceDetail.getConfigs().isBatAlm()));
        sosAlarm = findViewById(R.id.sosAlarm);
        sosAlarm.setOnClickListener(v -> clickChecked(ReqDeviceDetail.SOS_ALARM, !deviceDetail.getConfigs().isSosAlm()));
        tamperAlarm = findViewById(R.id.tamperAlarm);
        tamperAlarm.setOnClickListener(v -> clickChecked(ReqDeviceDetail.TMPR_ALARM, !deviceDetail.getConfigs().isTmprAlm()));
        voiveAlarm = findViewById(R.id.voiveAlarm);
        voiveAlarm.setOnClickListener(v -> clickChecked(ReqDeviceDetail.VOCMON_ALARM, true));

        phoneListLL = findViewById(R.id.phoneListLL);
        shareListLL = findViewById(R.id.shareListLL);

        findViewById(R.id.addPhones).setOnClickListener(v -> clickAddPhone());
        findViewById(R.id.addShare).setOnClickListener(v -> clickAddShare());

        initExceptionProcess(findViewById(R.id.loadingresult), findViewById(R.id.scrollView));

        initDialog();
    }

    private void initHeader() {
        findViewById(R.id.back).setOnClickListener(view -> finish());
        TextView titleTv = findViewById(R.id.title);
        titleTv.setText(R.string.deviceDetail);
        ImageView delete = findViewById(R.id.rightIv);
        delete.setImageResource(R.drawable.delete);
        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(v -> deleteDevice());
    }

    private void initDialog() {
        hintCommonDialog = new HintCommonDialog(this);
        editCommonDialog = new EditCommonDialog(this);
        listCommonDialog = new ListCommonDialog(this);
    }

    @Override
    protected void initData() {
        super.initData();
        requestDeviceDetail();
    }

    private void requestDeviceDetail() {
        mPresenter.viewDeviceDetail(deviceId);
    }

    private void clickModifyDeviceName() {
        editCommonDialog.setContent(deviceDetail.getName());
        editCommonDialog.setHint(getString(R.string.dialog_add_phone_hint));
        editCommonDialog.setOnCommitListener((dialog, content) -> {
            dialog.dismiss();
            showLoadingDialog();
            mPresenter.updateDeviceName(deviceId, content);
        });
        editCommonDialog.show();
    }

    private void clickModifyIcon() {
        ARouterManager.getInstance().gotoActivityForResult(ARouterPath.PATH_TO_DEVICE_PIC_MODIFY, this, ARouterPath.REQUEST_DEVICE_DETAIL_MODIFY_PIC);
    }

    private void clickAddPhone() {
        listCommonDialog.setTitle(getString(R.string.edit_phone));
        listCommonDialog.setItemLimit(deviceDetail.getConfigs().getPhnLmt());
        listCommonDialog.setContent(deviceDetail.getConfigs().getPhones());
        listCommonDialog.setOnCommitListener((dialog, content) -> {
            dialog.dismiss();
            showLoadingDialog();
            mPresenter.updateAlarmPhones(deviceId, content);
        });
        listCommonDialog.show();
    }

    private void clickAddShare() {
        editCommonDialog.setContent("");
        editCommonDialog.setHint(getString(R.string.dialog_add_share_hint));
        editCommonDialog.setOnCommitListener((dialog, content) -> {
            dialog.dismiss();
            showLoadingDialog();
            mPresenter.addShare(deviceId, content);
        });
        editCommonDialog.show();
    }

    private void deleteDevice() {
        hintCommonDialog.setHintIv(R.drawable.wrong);
        hintCommonDialog.setContent(getString(R.string.delete_device));
        hintCommonDialog.setOnCommitListener((dialog, confirm) -> {
            dialog.dismiss();
            showLoadingDialog();
            mPresenter.deleteDevice(deviceId);
        });
        hintCommonDialog.show();
    }

    private void refreshUI(ReqDeviceDetail deviceDetail) {
        deviceName.setText(deviceDetail.getName());
        deviceOwner.setText(deviceDetail.getOwner());
        expireDate.setText(TimeStamUtil.timeStampToDate(deviceDetail.getExpiryDate()));
        onLineDate.setText(TimeStamUtil.timeStampToDate(deviceDetail.getOnlineDate()));
        if (RespDevice.ONLINE.equals(deviceDetail.getStatus())) {
            status.setText(R.string.online);
        } else if (RespDevice.OFFLINE.equals(deviceDetail.getStatus())) {
            status.setText(R.string.offline);
        } else {
            status.setText(R.string.inactive);
        }

        sosAlarm.setChecked(deviceDetail.getConfigs().isSosAlm());
        batteryAlarm.setChecked(deviceDetail.getConfigs().isBatAlm());
        tamperAlarm.setChecked(deviceDetail.getConfigs().isTmprAlm());


        phoneListLL.removeAllViews();
        String phoneList = deviceDetail.getConfigs().getPhones();
        if (!TextUtils.isEmpty(phoneList)) {
            String[] phones = phoneList.split(",");
            if (phones != null && phones.length > 0) {
                for (String phone : phones) {
                    TextView textView = (TextView)View.inflate(this, R.layout.layout_phone, null);
                    phoneListLL.addView(textView);
                    textView.setText(phone);
                }
            }
        }

        shareListLL.removeAllViews();
        List<ShareInfo> shareInfos = deviceDetail.getShares();
        if(shareInfos != null && shareInfos.size()>0) {
            for (ShareInfo shareInfo : shareInfos) {
                View view = View.inflate(this, R.layout.layout_share_email, null);
                shareListLL.addView(view);
                TextView textView = view.findViewById(R.id.email);
                textView.setText(shareInfo.getEmail());
                view.findViewById(R.id.unShare).setOnClickListener(v -> clickUnShare(shareInfo));
                view.findViewById(R.id.setOwer).setOnClickListener(v -> clickChangeOwner(shareInfo));
            }
        }
    }

    private void clickChecked(int type, boolean enable) {
        showLoadingDialog();
        mPresenter.updateAlarmSwitch(deviceId, type, enable);
    }

    private void clickUnShare(ShareInfo shareInfo) {
        showLoadingDialog();
        mPresenter.unShare(deviceId, shareInfo.getId());
    }

    private void clickChangeOwner(ShareInfo shareInfo) {
        showLoadingDialog();
        mPresenter.changeOwner(deviceId, shareInfo.getId());
    }

    @Override
    public void onViewSuccess(ReqDeviceDetail deviceDetail) {
        hideLoadingDialog();
        if (deviceDetail == null) {
            doEmpty();
            return;
        }
        doSuccess();
        this.deviceDetail = deviceDetail;
        refreshUI(deviceDetail);
    }

    @Override
    public void onDelSuccess() {
        hideLoadingDialog();
        hintCommonDialog.setHintIv(R.drawable.wrong);
        hintCommonDialog.setContent(getString(R.string.dialog_title_del_success));
        hintCommonDialog.setOnCommitListener((dialog, confirm) -> {
            dialog.dismiss();
            close();
        });
        hintCommonDialog.show();
    }

    @Override
    public void onUpdateSuccess(int type) {
        requestDeviceDetail();
    }

    @Override
    public void onAddShare() {
        requestDeviceDetail();
    }

    @Override
    public void onUnShare() {
        requestDeviceDetail();
    }

    @Override
    public void onChangeOwner() {
        requestDeviceDetail();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        onError(api, errCode, errorInfo, this::requestDeviceDetail);
    }

    private void close() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ARouterPath.REQUEST_DEVICE_DETAIL_MODIFY_PIC && resultCode == RESULT_OK) {
            //todo
        }
    }
}
