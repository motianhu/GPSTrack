package com.smona.gpstrack.device.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.TimePicker;

import com.smona.gpstrack.R;
import com.smona.gpstrack.util.ToastUtil;

public class TimeCommonDialog extends Dialog {

    private TextView titleTxt;
    private TextView submitBtn;

    private TimePicker startTimePicker;
    private TimePicker endTimePicker;

    private String title;
    private int startH;
    private int startM;
    private int endH;
    private int endM;
    private OnCommitListener listener;

    public TimeCommonDialog(Context context) {
        super(context, R.style.CommonDialog);
        setCanceledOnTouchOutside(false);
    }

    public TimeCommonDialog setTitle(String title) {
        this.title = title;
        return this;
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
        titleTxt = findViewById(R.id.tv_title);
        findViewById(R.id.close).setOnClickListener(v -> this.dismiss());

        startTimePicker = findViewById(R.id.startTime);
        endTimePicker = findViewById(R.id.endTime);

        startTimePicker.setIs24HourView(true);
        refreshEndTime(startTimePicker, startH, startM);
        startTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

        endTimePicker.setIs24HourView(true);
        refreshEndTime(endTimePicker, endH, endM);
        endTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

        submitBtn = findViewById(R.id.tv_ok);
        submitBtn.setOnClickListener(v -> clickOk());

        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        }

    }

    private void refreshEndTime(TimePicker timePicker, int endH, int endM) {
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
            ToastUtil.showShort(R.string.start_than_end);
            return;
        }
        if (startH == endH) {
            if (startM < endM) {
                ToastUtil.showShort(R.string.start_than_end);
                return;
            } else if (startM == endM) {
                ToastUtil.showShort(R.string.start_equal_end);
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
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            //lp.width = getContext().getResources().getDimensionPixelSize(R.dimen.dimen_240dp);
            getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnCommitListener {
        void onClick(Dialog dialog, int startH, int startM, int endH, int endM);
    }
}