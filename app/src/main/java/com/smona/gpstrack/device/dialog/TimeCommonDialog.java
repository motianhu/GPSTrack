package com.smona.gpstrack.device.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import com.smona.gpstrack.R;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.widget.TimeSelectLayout;

public class TimeCommonDialog extends Dialog {

    private TimeSelectLayout startTimePicker;
    private TimeSelectLayout endTimePicker;

    private int startH;
    private int startM;
    private int endH;
    private int endM;
    private OnCommitListener listener;

    public TimeCommonDialog(Context context) {
        super(context, R.style.CommonDialog);
        setCanceledOnTouchOutside(false);
    }

    public TimeCommonDialog setTimeBetween(int startH, int startM, int endH, int endM) {
        this.startH = startH;
        this.startM = startM;
        refreshEndTime(startTimePicker, startH, startM);
        this.endH = endH;
        this.endM = endM;
        refreshEndTime(endTimePicker, endH, endM);
        return this;
    }

    public TimeCommonDialog setOnCommitListener(OnCommitListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time_layout);
        initView();
    }

    private void initView() {
        findViewById(R.id.back).setOnClickListener(v -> this.dismiss());
        findViewById(R.id.ok).setOnClickListener(v -> clickOk());

        startTimePicker = findViewById(R.id.startTime);
        endTimePicker = findViewById(R.id.endTime);

        refreshEndTime(startTimePicker, startH, startM);
        refreshEndTime(endTimePicker, endH, endM);

    }

    private void refreshEndTime(TimeSelectLayout timePicker, int endH, int endM) {
        if (timePicker == null) {
            return;
        }
        timePicker.setHour(endH);
        timePicker.setMinute(endM);
    }

    private void clickOk() {
        int startH = startTimePicker.getHour();
        int startM = startTimePicker.getMinute();
        int endH = endTimePicker.getHour();
        int endM = endTimePicker.getMinute();
        if (startH > endH) {
            CommonUtils.showShort(getContext(), R.string.start_than_end);
            return;
        }
        if (startH == endH) {
            if (startM > endM) {
                CommonUtils.showShort(getContext(), R.string.start_than_end);
                return;
            } else if (startM == endM) {
                CommonUtils.showShort(getContext(), R.string.start_equal_end);
                return;
            }
        }
        if (listener != null) {
            listener.onClick(this, startH, startM, endH, endM);
        }
    }

    @Override
    public void show() {
        super.show();
        try {
            Resources resources = getContext().getResources();
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            getWindow().setGravity(Gravity.BOTTOM);
            lp.height = resources.getDimensionPixelSize(R.dimen.dimen_300dp);
            lp.width = resources.getDisplayMetrics().widthPixels;
            lp.y = 0;
            getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnCommitListener {
        void onClick(Dialog dialog, int startH, int startM, int endH, int endM);
    }
}