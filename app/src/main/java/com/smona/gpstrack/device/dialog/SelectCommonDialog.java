package com.smona.gpstrack.device.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.device.bean.FilteItem;

import java.util.List;

public class SelectCommonDialog extends Dialog {

    private TextView titleTv;
    private View closeIv;

    private Context mContext;
    private String title;

    private RecyclerView selectRecyclerView;
    private DialogItemAdapter itemAdapter;
    private List<FilteItem> data;

    private OnCommitListener listener;

    public SelectCommonDialog(Context context, String title, List<FilteItem> data, OnCommitListener listener) {
        super(context, R.style.filterDialog);
        this.mContext = context;
        this.title = title;
        this.data = data;
        itemAdapter = new DialogItemAdapter(R.layout.dialog_filter_item);
        itemAdapter.setNewData(data);
        this.listener = listener;
        setCanceledOnTouchOutside(false);
    }

    public SelectCommonDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_filter_layout);
        initView();
    }

    private void initView() {
        titleTv = findViewById(R.id.title);
        closeIv = findViewById(R.id.close);
        closeIv.setOnClickListener(v -> dismiss());

        selectRecyclerView = findViewById(R.id.contentList);
        WidgetComponent.initRecyclerView(getContext(), selectRecyclerView);
        selectRecyclerView.setAdapter(itemAdapter);
        itemAdapter.setListener((item, pos) -> {
            if (listener != null) {
                listener.onClick(item);
            }
        });

        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
    }

    @Override
    public void show() {
        super.show();
        try {
            // 将对话框的大小按屏幕大小的百分比设置
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = getContext().getResources().getDimensionPixelSize(R.dimen.dimen_200dp);
            getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnCommitListener {
        void onClick(FilteItem item);
    }
}
