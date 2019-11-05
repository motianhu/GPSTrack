package com.smona.gpstrack.main.holder;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.device.DeviceDetailActivity;
import com.smona.gpstrack.device.bean.AvatarItem;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.widget.adapter.XViewHolder;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 2:13 PM
 */
public class DeviceHolder extends XViewHolder {

    private TextView deviceName;
    private ImageView deviceIcon;
    private ImageView deviceStatus;

    public DeviceHolder(View itemView) {
        super(itemView);
        deviceIcon = itemView.findViewById(R.id.device_icon);
        deviceName = itemView.findViewById(R.id.device_name);
        deviceStatus = itemView.findViewById(R.id.device_status);
    }

    public void bindViews(Fragment fragment, Device device) {
        deviceName.setText(device.getName());
        deviceIcon.setOnClickListener(v -> clickDevice(fragment, device));
        if (Device.ONLINE.equals(device.getStatus())) {
            deviceStatus.setImageResource(R.drawable.online);
        } else if (Device.OFFLINE.equals(device.getStatus())) {
            deviceStatus.setImageResource(R.drawable.offline);
        } else {
            deviceStatus.setImageResource(R.drawable.inactive);
        }
        AvatarItem.showDeviceIcon(device.getId(), deviceIcon);
    }

    private void clickDevice(Fragment fragment, Device device) {
        Intent intent = new Intent(itemView.getContext(), DeviceDetailActivity.class);
        intent.putExtra(ARouterPath.PATH_TO_DEVICE_DETAIL, device.getId());
        fragment.startActivityForResult(intent, ARouterPath.REQUEST_DEVICE_DETAIL);
    }
}
