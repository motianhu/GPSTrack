package com.smona.gpstrack.main.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.alarm.presenter.AlarmListPresenter;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.db.table.Alarm;
import com.smona.gpstrack.main.adapter.AlarmAdapter;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.gpstrack.widget.adapter.RecycleViewDivider;
import com.smona.http.wrapper.ErrorInfo;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:35 PM
 */
public class AlarmListFragemnt extends BasePresenterFragment<AlarmListPresenter, AlarmListPresenter.IAlertListView> implements AlarmListPresenter.IAlertListView {

    private AlarmAdapter mAdapter;

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
        XRecyclerView recyclerView = content.findViewById(R.id.xrecycler_wiget);
        mAdapter = new AlarmAdapter(R.layout.adapter_item_alarm);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity, LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.white)));

        WidgetComponent.initXRecyclerView(mActivity, recyclerView, new XRecyclerView.LoadingListener() {

            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.requestAlarmList();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        ToastUtil.showShort(errorInfo.getMessage());
    }

    @Override
    public void onAlarmList(List<Alarm> alarmList) {
        mAdapter.addData(alarmList);
    }

    @Override
    public void onRemoveMessage(int pos) {
        mAdapter.notifyItemRemoved(pos);
    }
}
