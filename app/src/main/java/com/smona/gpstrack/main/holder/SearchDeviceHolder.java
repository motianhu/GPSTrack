package com.smona.gpstrack.main.holder;

import android.view.View;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.widget.adapter.XViewHolder;

public class SearchDeviceHolder extends XViewHolder {

    private TextView titleTv;

    public SearchDeviceHolder(View itemView) {
        super(itemView);
        titleTv = itemView.findViewById(R.id.title);
    }

    public void bindViews(Device item) {
        titleTv.setText(item.getName());
    }
}
