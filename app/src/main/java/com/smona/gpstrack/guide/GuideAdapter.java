package com.smona.gpstrack.guide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smona.gpstrack.R;

import java.util.ArrayList;
import java.util.List;

public class GuideAdapter extends PagerAdapter {
    private Context mContext;
    private List<Integer> mData;

    public GuideAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    public void setData(List<Integer> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int resId = mData.get(position);
        ImageView imageView = (ImageView) View.inflate(mContext, R.layout.adapter_item_guide, null);
        imageView.setImageResource(resId);
        container.addView(imageView);
        return imageView;
    }
}
