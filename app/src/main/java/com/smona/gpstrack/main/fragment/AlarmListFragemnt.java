package com.smona.gpstrack.main.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smona.gpstrack.R;
import com.smona.gpstrack.alarm.presenter.AlarmListPresenter;
import com.smona.gpstrack.common.BasePresenterLoadingFragment;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.db.table.Alarm;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.main.adapter.AlarmAdapter;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.AlarmEvent;
import com.smona.gpstrack.notify.event.DeviceEvent;
import com.smona.gpstrack.widget.adapter.RecycleViewDivider;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:35 PM
 */
public class AlarmListFragemnt extends BasePresenterLoadingFragment<AlarmListPresenter, AlarmListPresenter.IAlertListView> implements AlarmListPresenter.IAlertListView, AlarmAdapter.OnRemoveMessageListener {

    private AlarmAdapter mAdapter;
    private TextView messageUnReadNum;
    private RespDevice device;
    private View back;

    @Override
    protected AlarmListPresenter initPresenter() {
        return new AlarmListPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_alert_list;
    }

    @Override
    protected void initView(View content) {
        super.initView(content);

        back = content.findViewById(R.id.back);
        back.setVisibility(View.GONE);
        back.setOnClickListener(v -> mActivity.finish());

        TextView titleTv = content.findViewById(R.id.title);
        titleTv.setText(R.string.warningList);

        messageUnReadNum = content.findViewById(R.id.rightTv);
        messageUnReadNum.setVisibility(View.VISIBLE);

        XRecyclerView recyclerView = content.findViewById(R.id.xrecycler_wiget);
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity, LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.white)));
        mAdapter = new AlarmAdapter(R.layout.adapter_item_alarm);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
        WidgetComponent.initXRecyclerView(mActivity, recyclerView);
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    List<String> unReadLsit = new ArrayList<>();
                    Alarm alarm;
                    Logger.e("motianhu", "firstItemPosition: " + firstItemPosition + ", lastItemPosition: " + lastItemPosition);
                    for (int index = firstItemPosition; index <= lastItemPosition; index++) {
                        alarm = mAdapter.getItem(index);
                        if(alarm != null && Alarm.STATUS_NEW.equals(alarm.getStatus())) {
                            unReadLsit.add(alarm.getId());
                        }
                    }
                    Logger.e("motianhu", "unReadLsit: " + unReadLsit);
                    if(unReadLsit.size() > 0) {
                        mPresenter.updateAlarmStatus(unReadLsit);
                    }
                }
            }
        });
        refreshUI();

        initExceptionProcess(content.findViewById(R.id.loadingresult), recyclerView);
        NotifyCenter.getInstance().registerListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotifyCenter.getInstance().unRegisterListener(this);
    }

    public void setDevice(RespDevice device) {
        this.device = device;
        mPresenter.setDevice(device);
        refreshUI();
    }

    private void refreshUI() {
        if (device == null) {
            return;
        }
        back.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        requestAlarmList();
    }

    private void requestAlarmList() {
        mPresenter.requestAlarmList();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        onError(api, errCode, errorInfo, this::requestAlarmList);
    }

    @Override
    public void onAlarmList(int unRead, List<Alarm> alarmList) {
        if (alarmList == null || alarmList.isEmpty()) {
            doEmpty();
            return;
        }
        doSuccess();
        if (unRead > 0) {
            messageUnReadNum.setText(String.format(getString(R.string.unread_count), unRead + ""));
        } else {
            messageUnReadNum.setText("");
        }
        mAdapter.setNewData(alarmList);
    }

    @Override
    public void onUpdateAlarm() {
        mPresenter.requestAlarmList();
    }

    @Override
    public void onRemoveMessage(int pos) {
        mAdapter.removeData(pos);
    }

    @Override
    public void onRemoveMessage(Alarm alarm, int position) {
        mPresenter.requestRemoveMessage(alarm, position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgRefreshDeviceList(AlarmEvent event) {
        if(device ==null) {
            requestAlarmList();
        }
    }
}
