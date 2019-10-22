package com.smona.gpstrack.settings.adapter;

import com.smona.gpstrack.settings.bean.DateFormatItem;
import com.smona.gpstrack.settings.holder.DateFormatHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

public class DateFormatAdapter extends XBaseAdapter<DateFormatItem, DateFormatHolder> {

    private OnClickItemListener listener;

    public DateFormatAdapter(int resId) {
        super(resId);
    }

    public void setListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(DateFormatHolder holder, DateFormatItem item, int pos) {
        holder.bindViews(item);
        holder.itemView.setOnClickListener(v -> clickItem(item, pos));
    }

    private void clickItem(DateFormatItem item, int pos) {
        if (listener != null) {
            listener.onClickItem(item, pos);
        }
    }

    public interface OnClickItemListener {
        void onClickItem(DateFormatItem item, int pos);
    }
}
