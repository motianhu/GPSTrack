package com.smona.gpstrack.device.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.device.bean.AvatarItem;
import com.smona.gpstrack.widget.adapter.XViewHolder;
import com.smona.image.loader.ImageLoaderDelegate;

public class AvatarHolder extends XViewHolder {

    ImageView imageView;
    CompoundButton checkBox;

    public AvatarHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.avatarIcon);
        checkBox = itemView.findViewById(R.id.checkbox);
    }

    public void bindViews(AvatarItem item, int pos) {
        if (item.getResId() > 0) {
            imageView.setImageResource(item.getResId());
            imageView.setBackgroundColor(itemView.getContext().getColor(R.color.white));
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(item.isSelcted());
        } else {
            if (TextUtils.isEmpty(item.getUrl())) {
                imageView.setBackgroundResource(R.drawable.bg_10_64bbd7_corner);
                imageView.setImageResource(R.drawable.upload);
                checkBox.setVisibility(View.INVISIBLE);
            } else {
                ImageLoaderDelegate.getInstance().showCornerImage(item.getUrl(), imageView, imageView.getContext().getResources().getDimensionPixelSize(R.dimen.dimen_10dp), 0);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(item.isSelcted());
            }
        }
    }
}
