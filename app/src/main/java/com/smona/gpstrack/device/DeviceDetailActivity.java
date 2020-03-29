package com.smona.gpstrack.device;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.BasePresenterLoadingActivity;
import com.smona.gpstrack.device.bean.AvatarItem;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.device.bean.req.ReqDeviceDetail;
import com.smona.gpstrack.device.bean.req.ShareInfo;
import com.smona.gpstrack.device.dialog.EditCommonDialog;
import com.smona.gpstrack.device.dialog.HintCommonDialog;
import com.smona.gpstrack.device.dialog.ListCommonDialog;
import com.smona.gpstrack.device.presenter.DeviceDetailPresenter;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.DeviceUpdateEvent;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.TimeStamUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.List;

/**
 * 设备详情
 */
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

    private View ownerContainer;
    private View phoneContainer;
    private View settingContainer;
    private View shareContainer;

    private View onlineContainer;
    private View sosContainer;
    private View temContainer;
    private View batContainer;
    private View vocContainer;

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
        batteryAlarm.setOnClickListener(v -> clickChecked(batteryAlarm, ReqDeviceDetail.BAT_ALARM, !deviceDetail.getConfigs().isBatAlm()));
        sosAlarm = findViewById(R.id.sosAlarm);
        sosAlarm.setOnClickListener(v -> clickChecked(sosAlarm, ReqDeviceDetail.SOS_ALARM, !deviceDetail.getConfigs().isSosAlm()));
        tamperAlarm = findViewById(R.id.tamperAlarm);
        tamperAlarm.setOnClickListener(v -> clickChecked(tamperAlarm, ReqDeviceDetail.TMPR_ALARM, !deviceDetail.getConfigs().isTmprAlm()));
        voiveAlarm = findViewById(R.id.voiveAlarm);
        voiveAlarm.setOnClickListener(v -> clickChecked(null, ReqDeviceDetail.VOCMON_ALARM, true));

        ownerContainer = findViewById(R.id.ownerContainer);
        phoneContainer = findViewById(R.id.phoneContainer);
        settingContainer = findViewById(R.id.settingContainer);
        shareContainer = findViewById(R.id.shareContainer);

        onlineContainer = findViewById(R.id.onlineContainer);
        sosContainer = findViewById(R.id.sosContainer);
        temContainer = findViewById(R.id.temContainer);
        batContainer = findViewById(R.id.batContainer);
        vocContainer = findViewById(R.id.vocContainer);

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
        editCommonDialog.setIv(-1);
        editCommonDialog.setMaxLength(CommonUtils.MAX_NAME_LENGHT);
        editCommonDialog.setTitle(getString(R.string.dialog_modify_device_name_title));
        editCommonDialog.setContent(deviceDetail.getName());
        editCommonDialog.setHint(getString(R.string.dialog_modify_device_name_hint));
        editCommonDialog.setOkName(getString(R.string.save));
        editCommonDialog.setEmptyHint(getString(R.string.toast_device_name_empty));
        editCommonDialog.setOnCommitListener((dialog, content) -> {
            dialog.dismiss();
            showLoadingDialog();
            mPresenter.updateDeviceName(deviceId, content);
        });
        editCommonDialog.show();
    }

    private void clickModifyIcon() {
        ARouterManager.getInstance().gotoActivityForResult(ARouterPath.PATH_TO_DEVICE_PIC_MODIFY, this, ARouterPath.REQUEST_DEVICE_DETAIL_MODIFY_PIC, deviceDetail.getNo());
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
        editCommonDialog.setOkName(getString(R.string.save));
        editCommonDialog.setTitle(getString(R.string.dialog_add_share));
        editCommonDialog.setIv(R.drawable.email);
        editCommonDialog.setMaxLength(CommonUtils.MAX_NAME_LENGHT);
        editCommonDialog.setContent("");
        editCommonDialog.setEmptyHint(getString(R.string.empty_email));
        editCommonDialog.setHint(getString(R.string.dialog_add_share_hint));
        editCommonDialog.setOnCommitListener((dialog, content) -> {
            if (!CommonUtils.isEmail(content)) {
                showShort(R.string.invalid_email);
                return;
            }
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
        AvatarItem.showDeviceIcon(deviceDetail.getNo(), deviceIcon);
        deviceName.setText(deviceDetail.getName());
        expireDate.setText(TimeStamUtil.timeStampToDate(deviceDetail.getExpiryDate()));
        if (deviceDetail.getOnlineDate() == 0) {
            onlineContainer.setVisibility(View.GONE);
        } else {
            onLineDate.setText(TimeStamUtil.timeStampToDate(deviceDetail.getOnlineDate()));
        }

        if (RespDevice.ONLINE.equals(deviceDetail.getStatus())) {
            status.setText(R.string.online);
        } else if (RespDevice.OFFLINE.equals(deviceDetail.getStatus())) {
            status.setText(R.string.offline);
        } else {
            status.setText(R.string.inactive);
        }

        if (deviceDetail.isOwner()) {
            ownerContainer.setVisibility(View.GONE);
            phoneContainer.setVisibility(View.VISIBLE);
            settingContainer.setVisibility(View.VISIBLE);
            shareContainer.setVisibility(View.VISIBLE);
            phoneListLL.setVisibility(View.VISIBLE);
            shareListLL.setVisibility(View.VISIBLE);

            if (deviceDetail.getConfigs().isSosAlm() == null) {
                sosContainer.setVisibility(View.GONE);
            } else {
                sosAlarm.setChecked(deviceDetail.getConfigs().isSosAlm());
            }

            if (deviceDetail.getConfigs().isBatAlm() == null) {
                batContainer.setVisibility(View.GONE);
            } else {
                batteryAlarm.setChecked(deviceDetail.getConfigs().isBatAlm());
            }

            if (deviceDetail.getConfigs().isTmprAlm() == null) {
                temContainer.setVisibility(View.GONE);
            } else {
                tamperAlarm.setChecked(deviceDetail.getConfigs().isTmprAlm());
            }

            if (deviceDetail.getConfigs().isVocMon() == null) {
                vocContainer.setVisibility(View.GONE);
            }

            phoneListLL.removeAllViews();
            int limit = deviceDetail.getConfigs().getPhnLmt();
            if(limit == 0) {
                phoneListLL.setVisibility(View.GONE);
                phoneContainer.setVisibility(View.GONE);
            }
            String phoneList = deviceDetail.getConfigs().getPhones();
            String[] phones = null;
            if (!TextUtils.isEmpty(phoneList)) {
                phones = phoneList.split(",");
            }

            if (phones != null && phones.length > 0) {
                for (String phone : phones) {
                    TextView textView = (TextView) View.inflate(this, R.layout.layout_phone, null);
                    phoneListLL.addView(textView);
                    textView.setText(phone);
                }
            }

            shareListLL.removeAllViews();
            List<ShareInfo> shareInfos = deviceDetail.getShares();
            if (shareInfos != null && shareInfos.size() > 0) {
                for (ShareInfo shareInfo : shareInfos) {
                    View view = View.inflate(this, R.layout.layout_share_email, null);
                    shareListLL.addView(view);
                    TextView textView = view.findViewById(R.id.email);
                    textView.setText(shareInfo.getEmail());
                    view.findViewById(R.id.unShare).setOnClickListener(v -> clickUnShare(shareInfo));
                    view.findViewById(R.id.setOwer).setOnClickListener(v -> clickChangeOwner(shareInfo));
                }
            }
        } else {
            ownerContainer.setVisibility(View.VISIBLE);
            deviceOwner.setText(deviceDetail.getOwner());

            phoneContainer.setVisibility(View.GONE);
            settingContainer.setVisibility(View.GONE);
            shareContainer.setVisibility(View.GONE);
            phoneListLL.setVisibility(View.GONE);
            shareListLL.setVisibility(View.GONE);
        }
    }

    private void clickChecked(CompoundButton button, int type, boolean enable) {
        showLoadingDialog();
        if (button != null) {
            button.setChecked(!enable);
        }
        mPresenter.updateAlarmSwitch(deviceId, type, enable);
    }

    private void clickUnShare(ShareInfo shareInfo) {
        showLoadingDialog();
        mPresenter.unShare(deviceId, shareInfo.getId());
    }

    private void clickChangeOwner(ShareInfo shareInfo) {
        hintCommonDialog.setHintIv(0);
        hintCommonDialog.setTitle("  ");
        hintCommonDialog.setContent(getString(R.string.change_ower));
        hintCommonDialog.setOnCommitListener((dialog, confirm) -> {
            dialog.dismiss();
            showLoadingDialog();
            mPresenter.changeOwner(deviceId, shareInfo.getId());
        });
        hintCommonDialog.show();
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
        close();
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
        if (requestCode == ARouterPath.REQUEST_DEVICE_DETAIL_MODIFY_PIC && resultCode == RESULT_OK) {
            AvatarItem.showDeviceIcon(deviceDetail.getNo(), deviceIcon);
            DeviceUpdateEvent deviceUpdateEvent = new DeviceUpdateEvent();
            deviceUpdateEvent.setDevice(deviceDetail);
            NotifyCenter.getInstance().postEvent(deviceUpdateEvent);
        }
    }
}
