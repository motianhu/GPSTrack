package com.smona.gpstrack.alarm;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.alarm.presenter.AlarmListPresenter;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.db.table.Alarm;
import com.smona.gpstrack.main.adapter.AlarmAdapter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.gpstrack.widget.adapter.RecycleViewDivider;
import com.smona.http.wrapper.ErrorInfo;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/16/19 7:21 PM
 */

@Route(path = ARouterPath.PATH_TO_ALARM_LIST)
public class AlarmListActivity extends BasePresenterActivity<AlarmListPresenter, AlarmListPresenter.IAlertListView> implements AlarmListPresenter.IAlertListView {

    private AlarmAdapter mAdapter;

    @Override
    protected AlarmListPresenter initPresenter() {
        return new AlarmListPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alert_list;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView titleTv = findViewById(R.id.title);
        titleTv.setText(R.string.warningList);

        XRecyclerView recyclerView = findViewById(R.id.xrecycler_wiget);
        recyclerView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.white)));
        mAdapter = new AlarmAdapter(R.layout.adapter_item_alarm);
        recyclerView.setAdapter(mAdapter);
        WidgetComponent.initXRecyclerView(this, recyclerView, new XRecyclerView.LoadingListener() {

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
}
