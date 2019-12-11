package com.smona.gpstrack.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.contrarywind.view.WheelView;
import com.smona.gpstrack.R;
import com.smona.gpstrack.widget.adapter.NumericWheelAdapter;

public class TimeSelectLayout extends LinearLayout {

    private WheelView hourWheelView;
    private WheelView minWheelView;

    public TimeSelectLayout(Context context) {
        super(context);
    }

    public TimeSelectLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeSelectLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        hourWheelView = findViewById(R.id.hour);
        hourWheelView.setAdapter(new NumericWheelAdapter(0, 23));
        hourWheelView.setGravity(Gravity.CENTER);

        minWheelView = findViewById(R.id.min);
        minWheelView.setAdapter(new NumericWheelAdapter(0, 59));
        minWheelView.setGravity(Gravity.CENTER);
    }

    public void setHour(int hour) {
        hourWheelView.setCurrentItem(hour);
    }

    public void setMinute(int min) {
        minWheelView.setCurrentItem(min);
    }

    public int getHour() {
        return hourWheelView.getCurrentItem();
    }

    public int getMinute() {
        return minWheelView.getCurrentItem();
    }


}
