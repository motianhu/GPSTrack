package com.smona.gpstrack.main.holder;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.fence.bean.TimeAlarm;
import com.smona.gpstrack.main.adapter.FenceAdapter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.widget.adapter.XViewHolder;

import java.util.List;

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
        Context context = geoName.getContext();
        geoName.setText(bean.getName());
        itemView.setOnClickListener(v -> clickEditGeo(bean));
        geoCheck.setChecked(FenceBean.ACTIVE.equals(bean.getStatus()));
        geoCheck.setOnClickListener(v -> {
            geoCheck.setChecked(FenceBean.ACTIVE.equals(bean.getStatus()));
            listener.onGeoEnable(bean);
        });
        StringBuffer desc = new StringBuffer();
        if (!CommonUtils.isEmpty(bean.getEntryAlarm())) {
            List<TimeAlarm> enterList = bean.getEntryAlarm();
            desc.append(context.getString(R.string.entry));
            desc.append(": ");
            desc.append(enterList.get(0).getFrom());
            desc.append("-");
            desc.append(enterList.get(0).getTo());
            desc.append("-");
            for (TimeAlarm timeAlarm : enterList) {
                desc.append(CommonUtils.dayToWeek(context, timeAlarm.getDay())).append(",");
            }
            desc.delete(desc.length() - 1, desc.length());
        }
        desc.append("\n");

        if (!CommonUtils.isEmpty(bean.getLeaveAlarm())) {
            List<TimeAlarm> exitList = bean.getLeaveAlarm();
            desc.append(context.getString(R.string.exit));
            desc.append(": ");
            desc.append(exitList.get(0).getFrom());
            desc.append("-");
            desc.append(exitList.get(0).getTo());
            desc.append("-");
            for (TimeAlarm timeAlarm : exitList) {
                desc.append(CommonUtils.dayToWeek(context, timeAlarm.getDay())).append(",");
            }
            desc.delete(desc.length() - 1, desc.length());
        }
        geoTime.setText(desc.toString());
    }

    private void clickEditGeo(FenceBean bean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FenceBean.class.getName(), bean);
        ARouterManager.getInstance().gotoActivityBundle(ARouterPath.PATH_TO_EDIT_GEO, bundle);
    }
}
