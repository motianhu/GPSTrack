package com.smona.gpstrack.main.adapter;

import com.smona.gpstrack.geo.bean.GeoBean;
import com.smona.gpstrack.main.holder.GEOHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 5:12 PM
 */
public class GEOAdapter extends XBaseAdapter<GeoBean, GEOHolder> {

    public GEOAdapter(int resId) {
        super(resId);
    }

    @Override
    protected void convert(GEOHolder holder, GeoBean item) {
        holder.bindViews(item);
    }
}
