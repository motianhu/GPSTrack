package com.smona.gpstrack.device.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.fence.adapter.FenceDeviceAdapter;
import com.smona.gpstrack.fence.bean.DeviceItem;
import com.smona.gpstrack.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class DeviceSelectedDialog extends Dialog {

    private List<DeviceItem> deviceList = new ArrayList<>();
    private RecyclerView deviceRecycler;
    private FenceDeviceAdapter deviceAdapter;

    private OnDevicesListener listener;

    public DeviceSelectedDialog(Context context, OnDevicesListener listener) {
        super(context, R.style.CommonDialog);
        this.listener = listener;
        setCanceledOnTouchOutside(false);
    }

    public void setDeviceList(List<DeviceItem> deviceItems) {
        this.deviceList.clear();
        if (CommonUtils.isEmpty(deviceItems)) {
            refreshData();
            return;
        }
        initTargetList(deviceItems, this.deviceList);
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
        TextView titletv = findViewById(R.id.title);
        titletv.setText(R.string.select_device);
        deviceRecycler = findViewById(R.id.contentList);
        deviceAdapter = new FenceDeviceAdapter(R.layout.adapter_item_fence_device);
        WidgetComponent.initRecyclerView(getContext(), deviceRecycler);
        deviceRecycler.setAdapter(deviceAdapter);
        refreshData();
    }

    private void refreshData() {
        if (deviceAdapter == null) {
            return;
        }
        deviceAdapter.setNewData(deviceList);
    }

    private void clickOk() {
        dismiss();
        if (listener == null) {
            return;
        }
        listener.onClick(deviceList);
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

    public interface OnDevicesListener {
        void onClick(List<DeviceItem> list);
    }

    private static void initTargetList(List<DeviceItem> sourceList, List<DeviceItem> targetList) {
        DeviceItem target;
        for (DeviceItem item : sourceList) {
            target = new DeviceItem();
            target.copy(item);
            targetList.add(target);
        }
    }


    public static void copyValue(List<DeviceItem> sourceList, List<DeviceItem> targetList) {
        for (DeviceItem item : sourceList) {
            for (DeviceItem target : targetList) {
                if (item.getId().equalsIgnoreCase(target.getId())) {
                    target.setSelect(item.isSelect());
                }
            }
        }
    }
}