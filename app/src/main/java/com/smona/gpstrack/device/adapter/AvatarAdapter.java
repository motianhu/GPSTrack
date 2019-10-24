package com.smona.gpstrack.device.adapter;

import android.app.Activity;

import com.smona.gpstrack.device.bean.AvatarItem;
import com.smona.gpstrack.util.ActivityUtils;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

public class AvatarAdapter extends XBaseAdapter<AvatarItem, AvatarHolder> {

    private Activity activity;

    public AvatarAdapter(int resId) {
        super(resId);
    }

    public AvatarAdapter(int resId, Activity activity) {
        super(resId);
        this.activity = activity;
    }

    @Override
    protected void convert(AvatarHolder holder, AvatarItem item, int pos) {
        holder.bindViews(item, pos);
        holder.imageView.setOnClickListener(v -> {
            if (item.getResId() == 0) {
                ActivityUtils.gotoGallery(activity);
            }
        });

        holder.checkBox.setOnClickListener(v-> {
            for (AvatarItem avatarItem : mDataList) {
                avatarItem.setSelcted(avatarItem == item);
            }
            notifyDataSetChanged();
        });
    }
}
