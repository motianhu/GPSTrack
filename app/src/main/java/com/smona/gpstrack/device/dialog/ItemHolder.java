package com.smona.gpstrack.device.dialog;

import android.view.View;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.device.bean.FilteItem;
import com.smona.gpstrack.widget.adapter.XViewHolder;

public class ItemHolder extends XViewHolder {

    private TextView nameTv;

    public ItemHolder(View itemView) {
        super(itemView);
        nameTv = itemView.findViewById(R.id.filter_name);
    }

    public void bindViews(FilteItem item) {
        nameTv.setText(item.getFilterName());
    }

}
