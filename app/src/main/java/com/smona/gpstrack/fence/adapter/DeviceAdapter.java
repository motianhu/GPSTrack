package com.smona.gpstrack.fence.adapter;

import com.smona.gpstrack.fence.bean.DeviceItem;
import com.smona.gpstrack.fence.holder.DeviceHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

public class DeviceAdapter extends XBaseAdapter<DeviceItem, DeviceHolder> {

    public DeviceAdapter(int resId) {
        super(resId);
    }

    @Override
    protected void convert(DeviceHolder holder, DeviceItem item, int pos) {
        holder.bindViews(item);
    }
}
