package com.smona.gpstrack.alarm.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.alarm.bean.AlarmListBean;
import com.smona.gpstrack.alarm.bean.ReqAlarmDelete;
import com.smona.gpstrack.alarm.bean.ReqAlarmList;
import com.smona.gpstrack.alarm.model.AlarmListModel;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.AlarmDecorate;
import com.smona.gpstrack.db.table.Alarm;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.thread.WorkHandlerManager;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/16/19 7:22 PM
 */
public class AlarmListPresenter extends BasePresenter<AlarmListPresenter.IAlertListView> {

    private AlarmDecorate alarmDecorate = new AlarmDecorate();
    private AlarmListModel alarmListModel = new AlarmListModel();
    private Device device;
    private int curPage = 0;

    public void setDevice(Device device) {
        this.device = device;
    }

    public void requestAlarmList() {
        ReqAlarmList pageUrlBean = new ReqAlarmList();
        pageUrlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        pageUrlBean.setPage(curPage);
        pageUrlBean.setPage_size(100);
        pageUrlBean.setDate_from(0);

        alarmListModel.requestAlarmList(pageUrlBean, new OnResultListener<AlarmListBean>() {
            @Override
            public void onSuccess(AlarmListBean alarmListBean) {
                saveDataToDb(alarmListBean.getDatas());
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError(curPage == 0 ? "":"requestAlarmList", stateCode, errorInfo);
                }
            }
        });
    }

    private void saveDataToDb(List<Alarm> data) {
        WorkHandlerManager.getInstance().runOnWorkerThread(() -> {
            alarmDecorate.addAll(data);
            if (device == null) {
                refreshUi(data);
            } else {
                List<Alarm> deviceAlarm = alarmDecorate.listAll(device.getId());
                refreshUi(deviceAlarm);
            }
        });
    }

    private void refreshUi(final List<Alarm> deviceAlarm) {
        WorkHandlerManager.getInstance().runOnMainThread(() -> {
            if (mView != null) {
                mView.onAlarmList(deviceAlarm);
            }
        });

    }

    public void refreshAlarmList() {
        curPage = 0;
        requestAlarmList();
    }

    public void requestRemoveMessage(Alarm alarm, int position) {
        ReqAlarmDelete delAlarm = new ReqAlarmDelete();
        delAlarm.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        delAlarm.setAlarmId(alarm.getId());
        alarmListModel.requestRemoveMessage(delAlarm, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean alarmListBean) {
                if (mView != null) {
                    mView.onRemoveMessage(position);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("requestRemoveMessage", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IAlertListView extends ICommonView {
        void onAlarmList(List<Alarm> alarmList);

        void onRemoveMessage(int pos);
    }
}
