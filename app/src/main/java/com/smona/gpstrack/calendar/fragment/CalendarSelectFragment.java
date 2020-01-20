package com.smona.gpstrack.calendar.fragment;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.calendar.CalendarCache;
import com.smona.gpstrack.calendar.CalendarConstans;
import com.smona.gpstrack.calendar.IShouldHideListener;
import com.smona.gpstrack.calendar.adapter.CalendarSelectAdapter;
import com.smona.gpstrack.calendar.helper.CalendarDateHelper;
import com.smona.gpstrack.calendar.model.MonthInfo;

import java.util.List;

public class CalendarSelectFragment extends Fragment {

    private Dialog mDialog;

    private RecyclerView mReycycler;
    private CalendarSelectAdapter mAdapter;
    private ImageView mCloseView;

    private int mTitleResId;
    private TextView mMainTitleTv;

    private boolean mOnlyChooseOneDay = false;

    private IShouldHideListener mShouldHideListener;

    public CalendarSelectFragment(int titleResId) {
        this(false, null, titleResId);
    }

    public CalendarSelectFragment(boolean onlyChooseOneDay, IShouldHideListener listener, int titleResId) {
        this.mOnlyChooseOneDay = onlyChooseOneDay;
        this.mShouldHideListener = listener;
        mTitleResId = titleResId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();
        register();
    }

    private void initViews() {
        mDialog = new Dialog(getContext(), R.style.PopupWindow);
        WindowManager.LayoutParams attrs = mDialog.getWindow().getAttributes();
        attrs.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attrs.height = ViewGroup.LayoutParams.MATCH_PARENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mDialog.getWindow().setStatusBarColor(Color.WHITE);
            mDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        mDialog.setContentView(R.layout.fragment_calendar_select);
        View rootView = mDialog.getWindow().getDecorView();

        mCloseView = rootView.findViewById(R.id.iv_calendar_close);
        mCloseView.setOnClickListener(v-> hide(true));

        mMainTitleTv = rootView.findViewById(R.id.mainTitle);

        mReycycler = rootView.findViewById(R.id.plan_time_calender);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
        mReycycler.setLayoutManager(layoutManager);
    }

    protected void initData() {
        CalendarCache.initDay();
        List<MonthInfo> monthInfos = CalendarDateHelper.getMonthInfos(CalendarConstans.MAX_MONTH_COUNT);
        mAdapter = new CalendarSelectAdapter(monthInfos, getContext(), mOnlyChooseOneDay, getShouldHideListener());
        mReycycler.setAdapter(mAdapter);
        mMainTitleTv.setText(mTitleResId);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        show(true);
    }

    private void updateUI() {
        mAdapter.notifyDataSetChanged();
    }

    private IShouldHideListener getShouldHideListener() {
        if (mShouldHideListener != null) {
            return mShouldHideListener;
        }
        return mDefaultHideListener;
    }

    private IShouldHideListener mDefaultHideListener = (dayTimeInfo) -> hide(true);

    public void onBackPressed() {
        hide(true);
    }

    public void show(boolean animation) {
        if (mDialog == null || mDialog.isShowing()) {
            // Fragment View 还没创建
            return;
        }
        mDialog.show();
        mReycycler.smoothScrollToPosition(mAdapter.getItemCount());
        updateUI();
    }

    public void hide(boolean animation) {
        mDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegister();
    }

    private BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };

    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CalendarConstans.ACTION_UPDATE_UI);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mUpdateReceiver, filter);
    }

    private void unRegister() {
        if (mUpdateReceiver == null) {
            return;
        }
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mUpdateReceiver);
    }
}
