package com.smona.gpstrack.main.adapter;

import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.main.holder.GEOHolder;
import com.smona.gpstrack.widget.adapter.XBaseAdapter;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 5:12 PM
 */
public class FenceAdapter extends XBaseAdapter<FenceBean, GEOHolder> {

    private IOnGoeEnableListener listener;

    public FenceAdapter(int resId) {
        super(resId);
    }

    public void setOnGeoEnable(IOnGoeEnableListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(GEOHolder holder, FenceBean item, int pos) {
        holder.bindViews(item, listener);
    }

    public interface IOnGoeEnableListener {
        void onGeoEnable(boolean enable, FenceBean geoBean);
    }
}
