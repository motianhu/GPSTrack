package com.smona.gpstrack.settings.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.settings.bean.DateFormatItem;
import com.smona.gpstrack.widget.adapter.XViewHolder;

public class DateFormatHolder extends XViewHolder {

    private TextView nameTv;
    private ImageView selectIv;

    public DateFormatHolder(View itemView) {
        super(itemView);
        nameTv = itemView.findViewById(R.id.title);
        selectIv = itemView.findViewById(R.id.selectIv);
    }

    public void bindViews(DateFormatItem item) {
        nameTv.setText(item.getDateFormat());
        selectIv.setSelected(item.isSelected());
    }

}
