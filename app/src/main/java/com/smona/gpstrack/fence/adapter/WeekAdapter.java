package com.smona.gpstrack.fence.adapter;

import com.smona.gpstrack.fence.bean.WeekItem;
import com.smona.gpstrack.fence.holder.WeekHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

public class WeekAdapter extends XBaseAdapter< WeekItem, WeekHolder> {

    public WeekAdapter(int resId) {
        super(resId);
    }

    @Override
    protected void convert(WeekHolder holder, WeekItem item, int pos) {
        holder.bindView(item);
    }
}
