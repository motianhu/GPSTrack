package com.smona.gpstrack.settings.adapter;

import com.smona.gpstrack.settings.bean.LanuageItem;
import com.smona.gpstrack.settings.holder.LanuageHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

public class LanuageAdapter extends XBaseAdapter<LanuageItem, LanuageHolder> {

    private OnClickItemListener listener;

    public LanuageAdapter(int resId) {
        super(resId);
    }

    public void setListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(LanuageHolder holder, LanuageItem item, int pos) {
        holder.bindViews(item);
        holder.itemView.setOnClickListener(v-> clickItem(item, pos));
    }

    private void clickItem(LanuageItem item, int pos) {
        if(listener != null) {
            listener.onClickItem(item, pos);
        }
    }

    public interface OnClickItemListener{
        void onClickItem(LanuageItem item, int pos);
    }
}
