package com.smona.gpstrack.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smona.gpstrack.R;
import com.smona.gpstrack.calendar.CalendarCache;
import com.smona.gpstrack.calendar.CalendarConstans;
import com.smona.gpstrack.calendar.IShouldHideListener;
import com.smona.gpstrack.calendar.holder.DayTimeViewHolder;
import com.smona.gpstrack.calendar.model.DayTimeInfo;

import java.util.ArrayList;


public class DayTimeAdapter extends RecyclerView.Adapter<DayTimeViewHolder> {

    private ArrayList<DayTimeInfo> mDays;
    private Context mContext;
    private boolean mOnlyChooseOneDay;
    private IShouldHideListener mShouldHideListener;

    public DayTimeAdapter(ArrayList<DayTimeInfo> days, Context context, boolean onlyChooseOneDay, IShouldHideListener shouldHideListener) {
        this.mDays = days;
        this.mContext = context;
        this.mOnlyChooseOneDay = onlyChooseOneDay;
        this.mShouldHideListener = shouldHideListener;
    }

    @Override
    public DayTimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DayTimeViewHolder ret = null;
        // 不需要检查是否复用，因为只要进入此方法，必然没有复用
        // 因为RecyclerView 通过Holder检查复用
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_selectday, parent, false);
        ret = new DayTimeViewHolder(v);

        return ret;
    }

    @Override
    public void onBindViewHolder(final DayTimeViewHolder holder, int position) {
        final DayTimeInfo dayTimeInfo = mDays.get(position);
        CalendarCache.initDay();

        //显示日期
        if (dayTimeInfo.getDay() != 0) {
            holder.select_txt_day.setText(dayTimeInfo.getDay() + "");
            holder.select_ly_day.setEnabled(true);
        } else {
            holder.select_ly_day.setEnabled(false);
        }

        holder.select_ly_day.setOnClickListener(v -> {
            if (mOnlyChooseOneDay) {
                CalendarCache.sTheOnlyChooseDay = dayTimeInfo;
                holder.select_ly_day.setBackgroundResource(R.drawable.bg_date_seleted);
                if (mShouldHideListener != null) {
                    mShouldHideListener.shouldHide(dayTimeInfo);
                }
                return;
            }

            if (CalendarCache.sStartDay.getYear() > 0 && CalendarCache.sStopDay.getYear() == -1) {      //已经点击了开始 ，点击结束位置，（默认结束位置-1,-1,-1,-1 说明还没有点击结束位置）
                //如果选中的年份大于开始的年份，说明结束日期肯定大于开始日期 ，合法的 ，将该item的天数的 信息  赋给 结束日期
                if (dayTimeInfo.getYear() > CalendarCache.sStartDay.getYear()) {
                    setEndDate(position, dayTimeInfo);
                } else if (dayTimeInfo.getYear() == CalendarCache.sStartDay.getYear()) {
                    //如果选中的年份 等于 选中的年份
                    if (dayTimeInfo.getMonth() > CalendarCache.sStartDay.getMonth()) {
                        //如果改item的天数的月份大于开始日期的月份，说明结束日期肯定大于开始日期 ，合法的 ，将该item的天数的 信息  赋给 结束日期
                        setEndDate(position, dayTimeInfo);
                    } else if (dayTimeInfo.getMonth() == CalendarCache.sStartDay.getMonth()) {
                        //年份月份 都相等
                        if (dayTimeInfo.getDay() > CalendarCache.sStartDay.getDay()) {
                            //判断天数 ，如果 该item的天数的 日子大于等于 开始日期的 日子 ，说明结束日期合法的 ，将该item的天数的 信息  赋给 结束日期
                            setEndDate(position, dayTimeInfo);
                        } else if (dayTimeInfo.getDay() == CalendarCache.sStartDay.getDay()) {
                            //判断天数 ，如果 该item的天数的 日子等于 开始日期的 日子 ，说明是再次点击了开始的日子，不响应
                            return;
                        } else {
                            //天数小与初始  从新选择开始  ，结束日期重置，开始日期为当前的位置的天数的信息
                            setStartDate(position, dayTimeInfo);
                        }
                    } else {
                        //选中的月份 比开始日期的月份还小，说明 结束位置不合法，结束日期重置，开始日期为当前的位置的天数的信息
                        setStartDate(position, dayTimeInfo);
                    }

                } else {
                    //选中的年份 比开始日期的年份还小，说明 结束位置不合法，结束日期重置，开始日期为当前的位置的天数的信息
                    setStartDate(position, dayTimeInfo);
                }
            } else if (CalendarCache.sStartDay.getYear() > 0 && CalendarCache.sStopDay.getYear() > 1) {      //已经点击开始和结束   第三次点击 ，重新点击开始
                setStartDate(position, dayTimeInfo);
            }
            sendBroadcastUI();
        });

        if (mOnlyChooseOneDay && CalendarCache.sTheOnlyChooseDay != null) {
            if (CalendarCache.sTheOnlyChooseDay.getYear() == dayTimeInfo.getYear() && CalendarCache.sTheOnlyChooseDay.getMonth() == dayTimeInfo.getMonth() && CalendarCache.sTheOnlyChooseDay.getDay() == dayTimeInfo.getDay()) {
                holder.select_ly_day.setBackgroundResource(R.drawable.bg_date_seleted);
            } else if (dayTimeInfo.isBeforeToday()) {
                holder.select_ly_day.setEnabled(false);
                holder.select_txt_day.setTextColor(mContext.getResources().getColor(R.color.color_A4A9AF));
                holder.mDownInfoTv.setTextColor(mContext.getResources().getColor(R.color.color_A4A9AF));
            } else {
                holder.select_ly_day.setBackgroundResource(R.color.white);
                holder.select_txt_day.setTextColor(mContext.getResources().getColor(R.color.color_222A37));
                holder.mDownInfoTv.setTextColor(mContext.getResources().getColor(R.color.color_FDC810));
            }
            return;
        }

        boolean select = false;
        if (CalendarCache.sStartDay.getYear() == dayTimeInfo.getYear() && CalendarCache.sStartDay.getMonth() == dayTimeInfo.getMonth() && CalendarCache.sStartDay.getDay() == dayTimeInfo.getDay()
                && CalendarCache.sStopDay.getYear() == dayTimeInfo.getYear() && CalendarCache.sStopDay.getMonth() == dayTimeInfo.getMonth() && CalendarCache.sStopDay.getDay() == dayTimeInfo.getDay()) {
            //开始和结束同一天   //一般是不会走到这里，已经屏蔽了同一天的情况
            holder.select_ly_day.setBackgroundResource(R.drawable.bg_date_seleted);
            select = true;
        } else if (CalendarCache.sStartDay.getYear() == dayTimeInfo.getYear() && CalendarCache.sStartDay.getMonth() == dayTimeInfo.getMonth() && CalendarCache.sStartDay.getDay() == dayTimeInfo.getDay()) {
            //该item是 开始日期
            holder.select_ly_day.setBackgroundResource(R.drawable.bg_date_seleted);
            holder.mUpInfoTv.setText("入住");
            select = true;
        } else if (CalendarCache.sStopDay.getYear() == dayTimeInfo.getYear() && CalendarCache.sStopDay.getMonth() == dayTimeInfo.getMonth() && CalendarCache.sStopDay.getDay() == dayTimeInfo.getDay()) {
            //该item是 结束日期
            holder.select_ly_day.setBackgroundResource(R.drawable.bg_date_seleted);
            holder.mUpInfoTv.setText("离店");
            select = true;
        } else if (dayTimeInfo.getMonthPosition() >= CalendarCache.sStartDay.getMonthPosition() && dayTimeInfo.getMonthPosition() <= CalendarCache.sStopDay.getMonthPosition()) {
            //处于开始和结束之间的点
            if (dayTimeInfo.getMonthPosition() == CalendarCache.sStartDay.getMonthPosition() && dayTimeInfo.getMonthPosition() == CalendarCache.sStopDay.getMonthPosition()) {
                //开始和结束是一个月份
                if (dayTimeInfo.getDay() > CalendarCache.sStartDay.getDay() && dayTimeInfo.getDay() < CalendarCache.sStopDay.getDay()) {
                    holder.select_ly_day.setBackgroundResource(R.color.color_FDC810_20);
                    select = true;
                } else {
                    holder.select_ly_day.setBackgroundResource(R.color.white);
                }
            } else if (CalendarCache.sStartDay.getMonthPosition() != CalendarCache.sStopDay.getMonthPosition()) {
                // 日期和 开始 不是一个月份
                if (dayTimeInfo.getMonthPosition() == CalendarCache.sStartDay.getMonthPosition() && dayTimeInfo.getDay() > CalendarCache.sStartDay.getDay()) {
                    //和初始相同月  天数往后
                    holder.select_ly_day.setBackgroundResource(R.color.color_FDC810_20);
                    select = true;
                } else if (dayTimeInfo.getMonthPosition() == CalendarCache.sStopDay.getMonthPosition() && dayTimeInfo.getDay() < CalendarCache.sStopDay.getDay()) {
                    //和结束相同月   天数往前
                    if (dayTimeInfo.getDay() <= 0) {
                        holder.select_ly_day.setBackgroundResource(R.color.white);
                    } else {
                        holder.select_ly_day.setBackgroundResource(R.color.color_FDC810_20);
                        select = true;
                    }
                } else if (dayTimeInfo.getMonthPosition() != CalendarCache.sStartDay.getMonthPosition() && dayTimeInfo.getMonthPosition() != CalendarCache.sStopDay.getMonthPosition()) {
                    //和 开始结束都不是同一个月
                    holder.select_ly_day.setBackgroundResource(R.color.color_FDC810_20);
                    select = true;
                } else {
                    holder.select_ly_day.setBackgroundResource(R.color.white);
                }
            }
        } else {
            holder.select_ly_day.setBackgroundResource(R.color.white);
        }

        if (dayTimeInfo.isBeforeToday()) {
            holder.select_ly_day.setEnabled(false);
            holder.select_txt_day.setTextColor(mContext.getResources().getColor(R.color.color_A4A9AF));
            holder.select_txt_day.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            holder.mDownInfoTv.setTextColor(mContext.getResources().getColor(R.color.color_A4A9AF));
        } else {
            holder.select_txt_day.setTextColor(mContext.getResources().getColor(R.color.color_222A37));
            holder.select_txt_day.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            if (select) {
                holder.mDownInfoTv.setTextColor(mContext.getResources().getColor(R.color.color_222A37));
            } else {
                holder.mDownInfoTv.setTextColor(mContext.getResources().getColor(R.color.color_FDC810));
            }
        }

    }

    private void setEndDate(int position, DayTimeInfo dayTimeInfo) {
        CalendarCache.sStopDay.setDay(dayTimeInfo.getDay());
        CalendarCache.sStopDay.setMonth(dayTimeInfo.getMonth());
        CalendarCache.sStopDay.setYear(dayTimeInfo.getYear());
        CalendarCache.sStopDay.setMonthPosition(dayTimeInfo.getMonthPosition());
        CalendarCache.sStopDay.setDayPosition(position);

        if (mShouldHideListener != null) {
            mShouldHideListener.shouldHide(dayTimeInfo);
        }
        sendBroadcastBg();
    }

    private void setStartDate(int position, DayTimeInfo dayTimeInfo) {
        CalendarCache.sStartDay.setDay(dayTimeInfo.getDay());
        CalendarCache.sStartDay.setMonth(dayTimeInfo.getMonth());
        CalendarCache.sStartDay.setYear(dayTimeInfo.getYear());
        CalendarCache.sStartDay.setMonthPosition(dayTimeInfo.getMonthPosition());
        CalendarCache.sStartDay.setDayPosition(position);
        CalendarCache.sStopDay.setDay(-1);
        CalendarCache.sStopDay.setMonth(-1);
        CalendarCache.sStopDay.setYear(-1);
        CalendarCache.sStopDay.setMonthPosition(-1);
        CalendarCache.sStopDay.setDayPosition(-1);
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (mDays != null) {
            ret = mDays.size();
        }
        return ret;
    }

    private void sendBroadcastUI() {
        if (mContext == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(CalendarConstans.ACTION_UPDATE_UI);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private void sendBroadcastBg() {
        if (mContext == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(CalendarConstans.ACTION_UPDATE_BG);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
