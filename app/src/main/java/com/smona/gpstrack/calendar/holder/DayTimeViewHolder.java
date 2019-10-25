package com.smona.gpstrack.calendar.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smona.gpstrack.R;

public class DayTimeViewHolder extends RecyclerView.ViewHolder {

    public TextView select_txt_day;      //日期文本
    public View select_ly_day;       //父容器 ， 用于点击日期
    public TextView mDownInfoTv;       //周末或者节假日的标记
    public TextView mUpInfoTv;       //顶部信息的标记

    public DayTimeViewHolder(View itemView) {
        super(itemView);
        select_ly_day = itemView.findViewById(R.id.select_ly_day);
        select_txt_day = itemView.findViewById(R.id.select_txt_day);
        mDownInfoTv = itemView.findViewById(R.id.tv_date_info_down);
        mUpInfoTv = itemView.findViewById(R.id.tv_date_info_up);
    }
}
