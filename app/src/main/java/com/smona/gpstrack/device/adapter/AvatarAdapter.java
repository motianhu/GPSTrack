package com.smona.gpstrack.device.adapter;

import com.smona.gpstrack.device.bean.AvatarItem;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

public class AvatarAdapter extends XBaseAdapter<AvatarItem, AvatarHolder> {

    public AvatarAdapter(int resId) {
        super(resId);
    }

    @Override
    protected void convert(AvatarHolder holder, AvatarItem item, int pos) {
        holder.bindViews(item, pos);
    }
}
