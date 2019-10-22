package com.smona.gpstrack.settings.adapter;

import com.smona.gpstrack.settings.bean.MapItem;
import com.smona.gpstrack.settings.holder.MapHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

public class MapAdapter extends XBaseAdapter<MapItem, MapHolder> {

    private OnClickItemListener listener;

    public MapAdapter(int resId) {
        super(resId);
    }

    public void setListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(MapHolder holder, MapItem item, int pos) {
        holder.bindViews(item);
        holder.itemView.setOnClickListener(v-> clickItem(item, pos));
    }

    private void clickItem(MapItem item, int pos) {
        if(listener != null) {
            listener.onClickItem(item, pos);
        }
    }

    public interface OnClickItemListener{
        void onClickItem(MapItem item, int pos);
    }
}
