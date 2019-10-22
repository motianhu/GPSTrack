package com.smona.gpstrack.main.holder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.util.ARouterManager;
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

    public void bindViews(Device device) {
        deviceName.setText(device.getName());
        deviceIcon.setOnClickListener(v -> clickDevice(device));
        if (Device.ONLINE.equals(device.getStatus())) {
            deviceStatus.setImageResource(R.drawable.online);
        } else if (Device.OFFLINE.equals(device.getStatus())) {
            deviceStatus.setImageResource(R.drawable.offline);
        } else {
            deviceStatus.setImageResource(R.drawable.inactive);
        }
    }

    private void clickDevice(Device device) {
        ARouterManager.getInstance().gotoActivityWithString(ARouterPath.PATH_TO_DEVICE_DETAIL, ARouterPath.PATH_TO_DEVICE_DETAIL, device.getId());
    }
}
