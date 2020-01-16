package com.smona.gpstrack.calendar;

import com.smona.gpstrack.calendar.model.DayTimeInfo;

/**
 * 选择日期后的回调接口
 */
public interface IShouldHideListener {

    void shouldHide(DayTimeInfo dayTimeInfo);
}
