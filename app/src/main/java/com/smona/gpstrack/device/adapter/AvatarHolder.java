package com.smona.gpstrack.device.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.device.bean.AvatarItem;
import com.smona.gpstrack.widget.adapter.XViewHolder;

public class AvatarHolder extends XViewHolder
{

    private ImageView imageView;
    private CheckBox checkBox;
    public AvatarHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.avatarIcon);
        checkBox = itemView.findViewById(R.id.checkbox);
    }

    public void bindViews(AvatarItem item, int pos) {
        if(item.getResId() > 0) {
            imageView.setImageResource(item.getResId());
            checkBox.setChecked(item.isSelcted());
        } else {

        }
    }
}
