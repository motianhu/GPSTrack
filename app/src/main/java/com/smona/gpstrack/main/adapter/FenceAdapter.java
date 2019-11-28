package com.smona.gpstrack.main.adapter;

import com.smona.gpstrack.db.table.Fence;
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
        void onGeoEnable(FenceBean geoBean);
    }

    public void removeFence(String delFenceId) {
        Fence fence;
        for (int i = 0; i < mDataList.size(); i++) {
            fence = mDataList.get(i);
            if (fence.getId().equalsIgnoreCase(delFenceId)) {
                mDataList.remove(i);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void addFence(FenceBean addFence) {
        mDataList.add(0, addFence);
        notifyDataSetChanged();
    }

    public void updateFence(FenceBean addFence) {
        Fence fence;
        for (int i = 0; i < mDataList.size(); i++) {
            fence = mDataList.get(i);
            if (fence.getId().equalsIgnoreCase(addFence.getId())) {
                notifyItemChanged(i);
                break;
            }
        }
    }
}
