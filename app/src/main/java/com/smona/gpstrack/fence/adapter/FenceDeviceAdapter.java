package com.smona.gpstrack.fence.adapter;

import com.smona.gpstrack.fence.bean.DeviceItem;
import com.smona.gpstrack.fence.holder.FenceDeviceHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

public class FenceDeviceAdapter extends XBaseAdapter<DeviceItem, FenceDeviceHolder> {

    public FenceDeviceAdapter(int resId) {
        super(resId);
    }

    @Override
    protected void convert(FenceDeviceHolder holder, DeviceItem item, int pos) {
        holder.bindViews(item);
    }
}
