package com.smona.gpstrack.main.holder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.device.bean.RespDevice;
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

    public DeviceHolder(View itemView) {
        super(itemView);
        deviceIcon = itemView.findViewById(R.id.device_icon);
        deviceName = itemView.findViewById(R.id.device_name);
    }

    public void bindViews(RespDevice device) {
        deviceName.setText(device.getName());
        itemView.setOnClickListener(v -> clickDevice(device));
    }

    private void clickDevice(RespDevice device) {
        ARouterManager.getInstance().gotoActivityWithString(ARouterPath.PATH_TO_DEVICE_DETAIL, ARouterPath.PATH_TO_DEVICE_DETAIL, device.getId());
    }
}
