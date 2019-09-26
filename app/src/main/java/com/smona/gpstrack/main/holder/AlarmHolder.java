package com.smona.gpstrack.main.holder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.db.table.Alarm;
import com.smona.gpstrack.widget.adapter.XViewHolder;

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

    public AlarmHolder(View itemView) {
        super(itemView);
        alarmIcon = itemView.findViewById(R.id.alarm_icon);
        titleTv = itemView.findViewById(R.id.alarm_title);
        timeTv = itemView.findViewById(R.id.alarm_time);
        contentTv = itemView.findViewById(R.id.alarm_content);
    }

    public void bindViews(Alarm bean) {
        titleTv.setText(bean.getCategory());
        timeTv.setText(bean.getDate() + "");
        contentTv.setText(bean.getContent());
    }
}
