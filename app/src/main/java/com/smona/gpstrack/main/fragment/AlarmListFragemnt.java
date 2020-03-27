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
import com.smona.gpstrack.device.dialog.HintCommonDialog;
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
 * 全部/具体设备报警列表
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:35 PM
 */
public class AlarmListFragemnt extends BasePresenterLoadingFragment<AlarmListPresenter, AlarmListPresenter.IAlertListView> implements AlarmListPresenter.IAlertListView, AlarmAdapter.OnRemoveMessageListener {

    private XRecyclerView recyclerView;
    private AlarmAdapter mAdapter;
    private TextView messageUnReadNum;
    //通过Device判定是全部还是指定设备报警列表
    private RespDevice device;
    private View back;
    private HintCommonDialog hintCommonDialog;

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
        initDialog();
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
        recyclerView.setFootViewText(null, "   ");
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

    private void initDialog(){
        hintCommonDialog = new HintCommonDialog(mActivity);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded()) {
            if(device == null && !notInitFinish) {
                notInitFinish = true;
            }
        }
    }

    //首页警告初始化
    @Override
    protected void requestData() {
        mPresenter.requestAlarmList();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
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
            messageUnReadNum.setText(String.format(getString(R.string.unread_count), "0"));
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
        hintCommonDialog.setHintIv(R.drawable.wrong);
        hintCommonDialog.setContent(getString(R.string.delete_device));
        hintCommonDialog.setOnCommitListener((dialog, confirm) -> {
            dialog.dismiss();
            showLoadingDialog();
            mPresenter.requestRemoveMessage(alarm, position);
        });
        hintCommonDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgDelAlarm(AlarmDelEvent event) {
        if(!isAdded()) {
            return;
        }
        hideLoadingDialog();
        if(mAdapter.getItemCount() == 1) {
            doEmpty();
        }
        mAdapter.removeData(event.getUiPos(), event.getAlarmId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgRefreshDateFormat(DateFormatEvent event) {
        if(!isAdded()) {
            return;
        }
        mAdapter.notifyDataSetChanged();
    }
}
