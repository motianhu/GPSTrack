package com.smona.gpstrack.main.fragment.attach;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smona.base.ui.fragment.BaseUiFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.bean.AvatarItem;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.DeviceEvent;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.PopupAnim;
import com.smona.gpstrack.util.TimeStamUtil;
import com.smona.http.config.LoadConfig;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/16/19 7:16 PM
 */
public class DevicePartFragment extends BaseUiFragment {

    private TextView deviceNameTv;
    private TextView deviceIdTv;
    private TextView lastLocationTv;
    private ImageView deviceIcon;

    private PopupAnim popupAnim = new PopupAnim();
    private View contentView;
    private View maskView;

    private RespDevice device;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map_device;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        contentView = rootView.findViewById(R.id.contentView);
        maskView = rootView.findViewById(R.id.maskView);
        maskView.setOnTouchListener((v, event) -> true);
        rootView.findViewById(R.id.routeHistory).setOnClickListener(v -> clickHistoryPath());
        rootView.findViewById(R.id.alarmList).setOnClickListener(v -> clickAlarmList());
        if(LoadConfig.appConfig != null) {
            rootView.findViewById(R.id.deviceNavigate).setVisibility(LoadConfig.appConfig.isRoute() ? View.VISIBLE:View.GONE);
        } else {
            rootView.findViewById(R.id.deviceNavigate).setVisibility(View.VISIBLE);
        }
        rootView.findViewById(R.id.deviceNavigate).setOnClickListener(v -> clickNavigation());
        rootView.findViewById(R.id.deviceeDetail).setOnClickListener(v -> clickDeviceDetail());

        deviceIcon = rootView.findViewById(R.id.device_icon);
        deviceNameTv = rootView.findViewById(R.id.device_name);
        deviceIdTv = rootView.findViewById(R.id.device_id);
        lastLocationTv = rootView.findViewById(R.id.device_last_location);

        rootView.findViewById(R.id.close_devicePart).setOnClickListener(view -> closeFragment());

        refreshUI();
        NotifyCenter.getInstance().registerListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotifyCenter.getInstance().unRegisterListener(this);
    }

    private void clickDeviceDetail() {
        ARouterManager.getInstance().gotoActivityWithString(ARouterPath.PATH_TO_DEVICE_DETAIL, ARouterPath.PATH_TO_DEVICE_DETAIL, device.getId());
    }

    private void clickHistoryPath() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARouterPath.PATH_TO_DEVICE_HISTORY, device);
        ARouterManager.getInstance().gotoActivityBundle(ARouterPath.PATH_TO_DEVICE_HISTORY, bundle);
    }

    private void clickAlarmList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARouterPath.PATH_TO_ALARM_LIST, device);
        ARouterManager.getInstance().gotoActivityBundle(ARouterPath.PATH_TO_ALARM_LIST, bundle);
    }

    private void clickNavigation() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARouterPath.PATH_TO_DEVICE_NAVIGATION, device);
        ARouterManager.getInstance().gotoActivityBundle(ARouterPath.PATH_TO_DEVICE_NAVIGATION, bundle);
    }

    public void setDevice(RespDevice device) {
        this.device = device;
    }

    public void showFragment() {
        if (contentView == null) {
            return;
        }

        View rootView = getView();
        if (rootView == null) {
            // Fragment View 还没创建
            return;
        }
        refreshUI();
        popupAnim.ejectView(true, mActivity, rootView, maskView, contentView);
    }

    public void closeFragment() {
        popupAnim.retract(true, mActivity, getView(), maskView, contentView, null);
    }

    private void refreshUI() {
        if (device == null) {
            return;
        }
        AvatarItem.showDeviceIcon(device.getNo(), deviceIcon);
        deviceNameTv.setText(device.getName());
        String id = "ID: " + device.getId();
        deviceIdTv.setText(id);
        lastLocationTv.setText(TimeStamUtil.timeStampToDate(device.getOnlineDate()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgRefreshDeviceList(DeviceEvent event) {
        if(isAdded()) {
            if(event.getActionType() == DeviceEvent.ACTION_DEL) {
                closeFragment();
            } else if(event.getActionType() == DeviceEvent.ACTION_UPDATE) {
                refreshUI();
            }
        }
    }
}
