package com.smona.gpstrack.main.holder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.datacenter.Alarm;
import com.smona.gpstrack.main.adapter.AlarmAdapter;
import com.smona.gpstrack.util.TimeStamUtil;
import com.smona.gpstrack.widget.adapter.XViewHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/23/19 2:13 PM
 */
public class AlarmHolder extends XViewHolder {

    private ImageView alarmIcon;
    private TextView titleTv;
    private TextView timeTv;
    private TextView contentTv;
    private View closeView;

    private Map<String, Integer> resIdMap = new HashMap<>();

    public AlarmHolder(View itemView) {
        super(itemView);

        resIdMap.put(Alarm.C_TMP, R.drawable.tamper);
        resIdMap.put(Alarm.C_POWER, R.drawable.power);
        resIdMap.put(Alarm.C_SOS, R.drawable.sos);
        resIdMap.put(Alarm.C_BATTERY, R.drawable.battery);
        resIdMap.put(Alarm.C_GEO, R.drawable.fence);

        alarmIcon = itemView.findViewById(R.id.alarm_icon);
        titleTv = itemView.findViewById(R.id.alarm_title);
        timeTv = itemView.findViewById(R.id.alarm_time);
        contentTv = itemView.findViewById(R.id.alarm_content);
        closeView = itemView.findViewById(R.id.removeMsg);
    }

    public void bindViews(Alarm bean, int pos, AlarmAdapter.OnRemoveMessageListener listener) {
        titleTv.setText(bean.getTitle());
        timeTv.setText(TimeStamUtil.timeStampToDate(bean.getDate()));
        contentTv.setText(bean.getContent());
        Integer resId = resIdMap.get(bean.getCategory());
        if (resId != null && resId > 0) {
            alarmIcon.setImageResource(resId);
        } else {
            alarmIcon.setImageResource(R.mipmap.ic_launcher);
        }
        closeView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onRemoveMessage(bean, pos);
            }
        });
    }

}
