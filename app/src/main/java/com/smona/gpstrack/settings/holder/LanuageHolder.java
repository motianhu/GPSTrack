package com.smona.gpstrack.settings.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.settings.bean.LanuageItem;
import com.smona.gpstrack.widget.adapter.XViewHolder;

public class LanuageHolder extends XViewHolder {

    private TextView nameTv;
    private ImageView selectIv;

    public LanuageHolder(View itemView) {
        super(itemView);
        nameTv = itemView.findViewById(R.id.title);
        selectIv = itemView.findViewById(R.id.selectIv);
    }

    public void bindViews(LanuageItem item) {
        nameTv.setText(item.getResId());
        if(item.isSelected()) {
            selectIv.setImageResource(R.drawable.selected);
        } else {
            selectIv.setImageBitmap(null);
        }
    }

}
