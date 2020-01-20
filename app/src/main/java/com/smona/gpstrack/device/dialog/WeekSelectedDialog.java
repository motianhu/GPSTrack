package com.smona.gpstrack.device.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.WindowManager;

import com.smona.gpstrack.R;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.fence.adapter.WeekAdapter;
import com.smona.gpstrack.fence.bean.WeekItem;

import java.util.ArrayList;
import java.util.List;

public class WeekSelectedDialog extends Dialog {

    private List<WeekItem> weekList = new ArrayList<>();
    private RecyclerView weekRecycler;
    private WeekAdapter weekAdapter;
    private OnWeeksListener listener;

    public WeekSelectedDialog(Context context, OnWeeksListener listener) {
        super(context, R.style.CommonDialog);
        this.listener = listener;
        setCanceledOnTouchOutside(false);
        WeekSelectedDialog.generWeekItem(context, weekList);
    }

    public void setWeekList(List<WeekItem> weekItems) {
        copyValue(weekItems, this.weekList);
        refreshData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_list_layout);
        initView();
    }

    private void initView() {
        findViewById(R.id.back).setOnClickListener(v -> dismiss());
        findViewById(R.id.ok).setOnClickListener(v -> clickOk());
        weekRecycler = findViewById(R.id.contentList);
        weekAdapter = new WeekAdapter(R.layout.adapter_item_fence_week);
        WidgetComponent.initRecyclerView(getContext(), weekRecycler);
        weekRecycler.setAdapter(weekAdapter);
        refreshData();
    }

    private void refreshData() {
        if (weekAdapter == null) {
            return;
        }
        weekAdapter.setNewData(weekList);
    }

    private void clickOk() {
        dismiss();
        if (listener == null) {
            return;
        }
        listener.onClick(weekList);
    }

    @Override
    public void show() {
        super.show();
        try {
            Resources resources = getContext().getResources();
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            getWindow().setGravity(Gravity.BOTTOM);
            lp.height = resources.getDimensionPixelSize(R.dimen.dimen_340dp);
            lp.width = resources.getDisplayMetrics().widthPixels;
            lp.y = 0;
            getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnWeeksListener {
        void onClick(List<WeekItem> list);
    }

    public static void copyValue(List<WeekItem> sourceList, List<WeekItem> targetList) {
        int index = 0;
        for (; index < sourceList.size(); index++) {
            targetList.get(index).setSelect(sourceList.get(index).isSelect());
        }
    }

    public static void generWeekItem(Context context, List<WeekItem> weekItems) {
        String[] weekDays = context.getResources().getStringArray(R.array.week_list);
        WeekItem item;
        for (int i = 0; i < weekDays.length; i++) {
            item = new WeekItem();
            item.setName(weekDays[i]);
            item.setPos(i + 1);
            item.setSelect(false);
            weekItems.add(item);
        }
    }
}