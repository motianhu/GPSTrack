package com.smona.gpstrack.device.dialog;

import com.smona.gpstrack.device.bean.FilteItem;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;


public class DialogItemAdapter extends XBaseAdapter<FilteItem, ItemHolder> {

    public DialogItemAdapter(int resId) {
        super(resId);
    }

    @Override
    protected void convert(ItemHolder holder, FilteItem item, int pos) {
        holder.bindViews(item);
        holder.itemView.setOnClickListener(v -> clickItem(item, pos));
    }

    private void clickItem(FilteItem item, int pos) {
        if (listener != null) {
            listener.onClickItem(item, pos);
        }
    }

    private OnClickItemListener listener;

    public void setListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    public interface OnClickItemListener {
        void onClickItem(FilteItem item, int pos);
    }
}
