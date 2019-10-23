package com.smona.gpstrack.main.adapter;

import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.main.holder.SearchDeviceHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

public class SearchDeviceAdapter extends XBaseAdapter<Device, SearchDeviceHolder> {

    private OnClickItemListener listener;

    public SearchDeviceAdapter(int resId) {
        super(resId);
    }

    public void setListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    private void clickItem(Device item, int pos) {
        if (listener != null) {
            listener.onClickItem(item, pos);
        }
    }

    @Override
    protected void convert(SearchDeviceHolder holder, Device item, int pos) {
        holder.bindViews(item);
        holder.itemView.setOnClickListener(v -> clickItem(item, pos));
    }

    public interface OnClickItemListener {
        void onClickItem(Device item, int pos);
    }
}
