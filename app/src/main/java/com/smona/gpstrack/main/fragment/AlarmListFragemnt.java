package com.smona.gpstrack.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smona.gpstrack.R;
import com.smona.gpstrack.alarm.presenter.AlarmListPresenter;
import com.smona.gpstrack.common.BasePresenterLoadingFragment;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.datacenter.Alarm;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.main.adapter.AlarmAdapter;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.AlarmDelEvent;
import com.smona.gpstrack.notify.event.DateFormatEvent;
import com.smona.gpstrack.widget.adapter.RecycleViewDivider;
import com.smona.http.wrapper.ErrorInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:35 PM
 */
public class AlarmListFragemnt extends BasePresenterLoadingFragment<AlarmListPresenter, AlarmListPresenter.IAlertListView> implements AlarmListPresenter.IAlertListView, AlarmAdapter.OnRemoveMessageListener {

    private XRecyclerView recyclerView;
    private AlarmAdapter mAdapter;
    private TextView messageUnReadNum;
    private RespDevice device;
    private View back;

    public static AlarmListFragemnt newInstance(RespDevice device) {
        AlarmListFragemnt fragment = new AlarmListFragemnt();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AlarmListFragemnt.class.getName(), device);
        fragment.setArguments(bundle);
        return fragment;
    }

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
        initSeriliaze();
        initContentView(content);
    }

    private void initSeriliaze() {
        if(getArguments() == null) {
            return;
        }
        device = (RespDevice) getArguments().getSerializable(AlarmListFragemnt.class.getName());
        mPresenter.setDevice(device);
    }

    private void initContentView(View content) {
        back = content.findViewById(R.id.back);
        back.setVisibility(View.GONE);
        back.setOnClickListener(v -> mActivity.finish());

        TextView titleTv = content.findViewById(R.id.title);
        titleTv.setText(R.string.warningList);

        messageUnReadNum = content.findViewById(R.id.rightTv);
        messageUnReadNum.setVisibility(View.VISIBLE);

        recyclerView = content.findViewById(R.id.xrecycler_wiget);
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity, LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.white)));
        mAdapter = new AlarmAdapter(R.layout.adapter_item_alarm);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
        WidgetComponent.initXRecyclerView(mActivity, recyclerView, true, false, new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                requestData();
            }
        });

        if (device != null) {
            back.setVisibility(View.VISIBLE);
        }

        initExceptionProcess(content.findViewById(R.id.loadingresult), recyclerView);
        NotifyCenter.getInstance().registerListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotifyCenter.getInstance().unRegisterListener(this);
    }

    //设备警告初始化
    @Override
    protected void initData() {
        super.initData();
        if(device == null) {
            return;
        }
        requestData();
    }

    //首页警告初始化
    @Override
    protected void requestData() {
        mPresenter.requestAlarmList();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        onError(api, errCode, errorInfo, this::requestData);
        recyclerView.loadMoreComplete();
    }

    @Override
    public void onEmpty() {
        hideLoadingDialog();
        doEmpty();
    }

    @Override
    public void onFinishMoreLoad() {
        hideLoadingDialog();
        recyclerView.setNoMore(true);
    }

    @Override
    public void onAlarmList(int curPage, int unRead, List<Alarm> alarmList) {
        hideLoadingDialog();
        if (unRead > 0) {
            messageUnReadNum.setText(String.format(getString(R.string.unread_count), unRead + ""));
        } else {
            messageUnReadNum.setText("");
        }
        doSuccess();
        if (curPage == 0) {
            mAdapter.setNewData(alarmList);
        } else {
            mAdapter.addData(alarmList);
        }
        recyclerView.loadMoreComplete();
    }

    @Override
    public void onRemoveMessage(Alarm alarm, int position) {
        mPresenter.requestRemoveMessage(alarm, position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgDelAlarm(AlarmDelEvent event) {
        mAdapter.removeData(event.getUiPos(), event.getAlarmId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgRefreshDateFormat(DateFormatEvent event) {
        mAdapter.notifyDataSetChanged();
    }
}
