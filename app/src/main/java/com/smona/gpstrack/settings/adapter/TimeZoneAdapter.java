package com.smona.gpstrack.settings.adapter;

import com.smona.gpstrack.settings.bean.TimeZoneItem;
import com.smona.gpstrack.settings.holder.TimeZoneHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

public class TimeZoneAdapter extends XBaseAdapter<TimeZoneItem, TimeZoneHolder> {

    private OnClickItemListener listener;

    public TimeZoneAdapter(int resId) {
        super(resId);
    }

    public void setListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(TimeZoneHolder holder, TimeZoneItem item, int pos) {
        holder.bindViews(item);
        holder.itemView.setOnClickListener(v -> clickItem(item, pos));
    }

    private void clickItem(TimeZoneItem item, int pos) {
        if (listener != null) {
            listener.onClickItem(item, pos);
        }
    }

    public interface OnClickItemListener {
        void onClickItem(TimeZoneItem item, int pos);
    }
}
