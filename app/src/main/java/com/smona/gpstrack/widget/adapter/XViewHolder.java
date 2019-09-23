package com.smona.gpstrack.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 4:20 PM
 */
public class XViewHolder extends RecyclerView.ViewHolder {

    public XViewHolder(View itemView) {
        super(itemView);
    }

    public <T extends View> T getView(int resId) {
        return itemView.findViewById(resId);
    }
}
