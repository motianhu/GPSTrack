package com.smona.gpstrack.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smona.gpstrack.R;
import com.smona.gpstrack.calendar.IShouldHideListener;
import com.smona.gpstrack.calendar.holder.CalendarSelectViewHolder;
import com.smona.gpstrack.calendar.model.DayTimeInfo;
import com.smona.gpstrack.calendar.model.MonthInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarSelectAdapter extends RecyclerView.Adapter<CalendarSelectViewHolder> {

    private List<MonthInfo> datas;
    private Context context;
    private boolean mOnlyChooseOneDay;
    private IShouldHideListener mShouldHideListener;

    public CalendarSelectAdapter(List<MonthInfo> datas, Context context, boolean onlyChooseOneDay, IShouldHideListener shouldHideListener) {
        this.datas = datas;
        this.context = context;
        this.mOnlyChooseOneDay = onlyChooseOneDay;
        this.mShouldHideListener = shouldHideListener;
    }


    @Override
    public CalendarSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CalendarSelectViewHolder ret = null;
        // 不需要检查是否复用，因为只要进入此方法，必然没有复用
        // 因为RecyclerView 通过Holder检查复用
        View v = LayoutInflater.from(context).inflate(R.layout.item_recycler_timeplan, parent, false);
        ret = new CalendarSelectViewHolder(v, context);
        return ret;
    }

    @Override
    public void onBindViewHolder(CalendarSelectViewHolder holder, int position) {
        MonthInfo monthTimeEntity = datas.get(position);
        String yyyy_mm = monthTimeEntity.getYear() + "-" + monthTimeEntity.getMonth();
        holder.plan_time_txt_month.setText(yyyy_mm);  //显示yyyy-mm
        int today = -1;
        if (position == 0) { //第一个月，将已过的日期进行标记
            Calendar todayCalendar = new GregorianCalendar();
            todayCalendar.setTime(new Date());
            today = todayCalendar.get(Calendar.DAY_OF_MONTH);
        }


        //得到该月份的第一天
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, monthTimeEntity.getYear());          //指定年份
        calendar.set(Calendar.MONTH, monthTimeEntity.getMonth() - 1);        //指定月份 Java月份从0开始算
        calendar.set(Calendar.DAY_OF_MONTH, 1);                           // 指定天数 ，这三行是为了得到 这一年这一月的第一天

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);             //得到该月份第一天 是星期几
        ArrayList<DayTimeInfo> days = new ArrayList<DayTimeInfo>();
        for (int i = 0; i < dayOfWeek - 1; i++) {                          //这里是添加空格的逻辑
            days.add(new DayTimeInfo(0, monthTimeEntity.getMonth(), monthTimeEntity.getYear(), position));
        }
        calendar.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        calendar.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        for (int i = 1; i <= calendar.get(Calendar.DAY_OF_MONTH); i++) {     //添加 该月份的天数   一号 到 该月的最后一天
            DayTimeInfo info = new DayTimeInfo(i, monthTimeEntity.getMonth(), monthTimeEntity.getYear(), position);
            if (today > 0 && i < today) {
                info.setBeforeToday(true);
            }
            days.add(info);
        }

        DayTimeAdapter adapter = new DayTimeAdapter(days, context, mOnlyChooseOneDay, mShouldHideListener);
        holder.plan_time_recycler_content.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }
}
