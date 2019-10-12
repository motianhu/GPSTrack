package com.smona.gpstrack.alarm.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.alarm.bean.AlarmListBean;
import com.smona.gpstrack.alarm.bean.ReqAlarmList;
import com.smona.gpstrack.alarm.model.AlarmListModel;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.db.table.Alarm;
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
    private int curPage = 0;

    public void requestAlarmList() {
        ReqAlarmList pageUrlBean = new ReqAlarmList();
        pageUrlBean.setLocale(ParamConstant.LOCALE_EN);
        pageUrlBean.setPage(curPage);
        pageUrlBean.setPage_size(10);
        pageUrlBean.setDate_from(0);

        alarmListModel.requestAlarmList(pageUrlBean, new OnResultListener<AlarmListBean>() {
            @Override
            public void onSuccess(AlarmListBean alarmListBean) {
                if(mView!=null) {
                    mView.onAlarmList(alarmListBean.getDatas());
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if(mView!=null) {
                    mView.onError("requestAlarmList", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IAlertListView extends ICommonView {
        void onAlarmList(List<Alarm> alarmList);
    }
}
