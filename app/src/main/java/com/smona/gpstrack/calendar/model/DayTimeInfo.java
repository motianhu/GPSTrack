package com.smona.gpstrack.calendar.model;

import com.smona.gpstrack.calendar.helper.DateUtil;

/**
 * 日历中每一个月中的 每一个天数
 */
public class DayTimeInfo {
    private int day;           //日期，几号
    private int month;           //属于的月份
    private int year;           //属于的年份

    private boolean isBeforeToday; //是否比今天更早

    private int monthPosition;    //属于的月份位置，注意是该日期属于的月份在外层列表中的position，不是月份
    private int dayPosition;      //属于的日期位置，注意是该日期在每个月（内层列表）中的位置

    private String mFestivalOrWeekendStr = "";

    public DayTimeInfo(int day, int month, int year, int monthPosition) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.monthPosition = monthPosition;
    }


    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {

        this.year = year;
    }

    public int getMonthPosition() {
        return monthPosition;
    }

    public void setMonthPosition(int monthPosition) {
        this.monthPosition = monthPosition;
    }

    public int getDayPosition() {
        return dayPosition;
    }

    public void setDayPosition(int dayPosition) {
        this.dayPosition = dayPosition;
    }

    public boolean isBeforeToday() {
        return isBeforeToday;
    }

    public void setBeforeToday(boolean beforeToday) {
        isBeforeToday = beforeToday;
    }
}
