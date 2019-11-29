package com.smona.gpstrack.alarm.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.alarm.bean.AlarmListBean;
import com.smona.gpstrack.alarm.bean.ReqAlarmDelete;
import com.smona.gpstrack.alarm.bean.ReqAlarmList;
import com.smona.gpstrack.alarm.model.AlarmListModel;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.datacenter.Alarm;
import com.smona.gpstrack.datacenter.AlarmListCenter;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.AlarmDelEvent;
import com.smona.gpstrack.notify.event.AlarmUnReadEvent;
import com.smona.gpstrack.util.CommonUtils;
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
        pageUrlBean.setPage_size(10);
        pageUrlBean.setDevicePlatformId(device == null ? "" : device.getId());

        alarmListModel.requestAlarmList(pageUrlBean, new OnResultListener<AlarmListBean>() {
            @Override
            public void onSuccess(AlarmListBean alarmListBean) {
                if (mView != null) {
                    if (curPage == 0) {
                        //没数据
                        if (CommonUtils.isEmpty(alarmListBean.getDatas())) {
                            mView.onEmpty();
                            return;
                        }
                    }
                    if(device == null) {
                        AlarmUnReadEvent alarmUnReadEvent = new AlarmUnReadEvent();
                        alarmUnReadEvent.setUnReadCount(alarmListBean.getTtlUnRead());
                        NotifyCenter.getInstance().postEvent(alarmUnReadEvent);
                    }
                    //有数据
                    mView.onAlarmList(curPage, alarmListBean.getTtlUnRead(), alarmListBean.getDatas());

                    if ((curPage + 1) < alarmListBean.getTtlPage()) {
                        curPage += 1;
                    } else {
                        curPage = 0;
                        //最后一页
                        mView.onFinishMoreLoad();
                    }
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError(curPage == 0 ? "" : "requestAlarmList", stateCode, errorInfo);
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
                AlarmDelEvent delEvent = new AlarmDelEvent();
                delEvent.setAlarmId(alarm.getId());
                delEvent.setUiPos(position);
                //涉及多个页面删除动作
                AlarmListCenter.getInstance().removeAlarm(alarm.getId());
                NotifyCenter.getInstance().postEvent(delEvent);
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
        void onEmpty();
        void onFinishMoreLoad();
        void onAlarmList(int curPage, int unReadCount, List<Alarm> alarmList);
    }
}
