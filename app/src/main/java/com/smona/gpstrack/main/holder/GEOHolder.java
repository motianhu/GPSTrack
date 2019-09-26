package com.smona.gpstrack.main.holder;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.geo.bean.GeoBean;
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

    private ImageView geoIv;
    private TextView geoName;

    public GEOHolder(View itemView) {
        super(itemView);

        geoIv = itemView.findViewById(R.id.geo_icon);
        geoName = itemView.findViewById(R.id.geo_name);
    }

    public void bindViews(GeoBean bean) {
        geoName.setText(bean.getName());
        itemView.setOnClickListener(v -> clickEditGeo(bean));
    }

    private void clickEditGeo(GeoBean bean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(GeoBean.class.getName(), bean);
        ARouterManager.getInstance().gotoActivityBundle(ARouterPath.PATH_TO_EDIT_GEO, bundle);
    }
}
