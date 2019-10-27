package com.smona.gpstrack.device.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.device.bean.AvatarItem;
import com.smona.gpstrack.widget.adapter.XViewHolder;
import com.smona.image.loader.ImageLoaderDelegate;

public class AvatarHolder extends XViewHolder {

    ImageView imageView;
    TextView textView;
    CompoundButton checkBox;

    public AvatarHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.avatarIcon);
        checkBox = itemView.findViewById(R.id.checkbox);
        textView = itemView.findViewById(R.id.title);
    }

    public void bindViews(AvatarItem item, int pos) {
        if (item.getResId() > 0) {
            imageView.setImageResource(item.getResId());
            imageView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.white));
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(item.isSelcted());
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(item.getUrl())) {
                imageView.setBackgroundResource(R.drawable.bg_10_64bbd7_corner);
                checkBox.setVisibility(View.INVISIBLE);
                textView.setText(R.string.select_local_pic);
                Drawable drawable = itemView.getContext().getDrawable(R.drawable.upload);
                drawable.setBounds(0, 0, 100, 100);
                textView.setCompoundDrawables(null, drawable, null, null);
            } else {
                ImageLoaderDelegate.getInstance().showCornerImage(item.getUrl(), imageView, imageView.getContext().getResources().getDimensionPixelSize(R.dimen.dimen_10dp), 0);
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(item.isSelcted());
                textView.setText(R.string.modify_local_pic);
                Drawable drawable = itemView.getContext().getDrawable(R.drawable.update);
                drawable.setBounds(0, 0, 100, 100);
                textView.setCompoundDrawables(null, itemView.getContext().getDrawable(R.drawable.update), null, null);
            }
        }
    }
}
