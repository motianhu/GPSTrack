package com.smona.gpstrack.alarm.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.alarm.bean.AlarmListBean;
import com.smona.gpstrack.alarm.bean.ReqAlarmDelete;
import com.smona.gpstrack.alarm.bean.ReqAlarmList;
import com.smona.gpstrack.alarm.bean.ReqAlarmRead;
import com.smona.gpstrack.alarm.model.AlarmListModel;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.AlarmDecorate;
import com.smona.gpstrack.db.table.Alarm;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.AlarmEvent;
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
        pageUrlBean.setPage_size(1000);
        pageUrlBean.setDate_from(0);

        alarmListModel.requestAlarmList(pageUrlBean, new OnResultListener<AlarmListBean>() {
            @Override
            public void onSuccess(AlarmListBean alarmListBean) {
                saveDataToDb(alarmListBean.getTtlUnRead(), alarmListBean.getDatas());
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError(curPage == 0 ? "" : "requestAlarmList", stateCode, errorInfo);
                }
            }
        });
    }

    private void saveDataToDb(int unRead, List<Alarm> data) {
        WorkHandlerManager.getInstance().runOnWorkerThread(() -> {
            alarmDecorate.addAll(data);
            if (device == null) {
                refreshUi(unRead, data);
            } else {
                List<Alarm> deviceAlarm = alarmDecorate.listAll(device.getId());
                int filterUnRead = alarmDecorate.listAll(device.getId(), Alarm.STATUS_NEW);
                refreshUi(filterUnRead, deviceAlarm);
                NotifyCenter.getInstance().postEvent(new AlarmEvent());
            }
        });
    }

    private void refreshUi(int unRead, final List<Alarm> deviceAlarm) {
        WorkHandlerManager.getInstance().runOnMainThread(() -> {
            if (mView != null) {
                mView.onAlarmList(unRead, deviceAlarm);
            }
        });
    }

    public void updateAlarmStatus(List<String> ids) {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());

        ReqAlarmRead alarmRead = new ReqAlarmRead();
        alarmRead.setReadIds(ids);
        alarmListModel.requestUpdateUnreadAlarm(urlBean, alarmRead, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean emptyBean) {
                if (mView != null) {
                    mView.onUpdateAlarm();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("updateAlarmStatus", stateCode, errorInfo);
                }
            }
        });
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
        void onAlarmList(int unRead, List<Alarm> alarmList);

        void onUpdateAlarm();

        void onRemoveMessage(int pos);
    }
}
