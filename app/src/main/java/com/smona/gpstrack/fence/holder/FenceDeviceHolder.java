package com.smona.gpstrack.fence.holder;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.fence.bean.DeviceItem;
import com.smona.gpstrack.widget.adapter.XViewHolder;

public class FenceDeviceHolder extends XViewHolder {

    private TextView nameTv;
    private CompoundButton selectBox;

    public FenceDeviceHolder(View itemView) {
        super(itemView);
        nameTv = itemView.findViewById(R.id.name);
        selectBox = itemView.findViewById(R.id.radio);
    }

    public void bindViews(DeviceItem item) {
        nameTv.setText(item.getName());
        selectBox.setChecked(item.isSelect());
        selectBox.setOnClickListener(v -> {
            item.setSelect(!item.isSelect());
            selectBox.setChecked(item.isSelect());
        });
    }
}
