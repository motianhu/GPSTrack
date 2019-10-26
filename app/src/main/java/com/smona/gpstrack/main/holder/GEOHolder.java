package com.smona.gpstrack.main.holder;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.main.adapter.FenceAdapter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.widget.adapter.XViewHolder;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/26/19 5:13 PM
 */
public class GEOHolder extends XViewHolder {

    private TextView geoTime;
    private TextView geoName;
    private SwitchCompat geoCheck;

    public GEOHolder(View itemView) {
        super(itemView);

        geoName = itemView.findViewById(R.id.geo_name);
        geoTime = itemView.findViewById(R.id.geo_time);
        geoCheck = itemView.findViewById(R.id.geo_check);
    }

    public void bindViews(FenceBean bean, FenceAdapter.IOnGoeEnableListener listener) {
        geoName.setText(bean.getName());
        geoName.setOnClickListener(v -> clickEditGeo(bean));
        geoCheck.setChecked(FenceBean.STATUS_ENABLE.equals(bean.getStatus()));
        geoCheck.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onGeoEnable(isChecked, bean));
    }

    private void clickEditGeo(FenceBean bean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FenceBean.class.getName(), bean);
        ARouterManager.getInstance().gotoActivityBundle(ARouterPath.PATH_TO_EDIT_GEO, bundle);
    }
}
