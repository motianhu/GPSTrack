package com.smona.gpstrack.fence.holder;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.fence.bean.WeekItem;
import com.smona.gpstrack.widget.adapter.XViewHolder;

public class WeekHolder extends XViewHolder {

    private TextView weekTv;
    private CompoundButton selctBox;
    public WeekHolder(View itemView) {
        super(itemView);
        weekTv = itemView.findViewById(R.id.name);
        selctBox = itemView.findViewById(R.id.radio);
    }

    public void bindView(WeekItem item) {
        weekTv.setText(item.getName());
    }
}
