package com.smona.gpstrack.settings.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.settings.bean.TimeZoneItem;
import com.smona.gpstrack.widget.adapter.XViewHolder;

public class TimeZoneHolder extends XViewHolder {

    private TextView nameTv;
    private ImageView selectIv;

    public TimeZoneHolder(View itemView) {
        super(itemView);
        nameTv = itemView.findViewById(R.id.title);
        selectIv = itemView.findViewById(R.id.selectIv);
    }

    public void bindViews(TimeZoneItem item) {
        nameTv.setText(item.getTimeZone());
        if(item.isSelected()) {
            selectIv.setImageResource(R.drawable.selected);
        } else {
            selectIv.setImageBitmap(null);
        }
    }

}
