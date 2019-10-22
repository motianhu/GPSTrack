package com.smona.gpstrack.main.adapter;

import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.main.holder.DeviceHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 1:04 PM
 */
public class DeviceAdapter extends XBaseAdapter<Device, DeviceHolder> {

    public DeviceAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(DeviceHolder holder, Device item, int pos) {
        holder.bindViews(item);
    }
}
