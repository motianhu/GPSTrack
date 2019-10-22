package com.smona.gpstrack.settings.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.settings.bean.DateFormatItem;
import com.smona.gpstrack.settings.model.SettingModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

public class DateFormatPresenter extends BasePresenter<DateFormatPresenter.IDateFormatView> {
    private SettingModel model = new SettingModel();

    public void switchDateFormat(DateFormatItem item) {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        model.switchDateFormat(urlBean, item, new OnResultListener<RespEmptyBean>() {

            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if (mView != null) {
                    mView.onSwitchDateFormat(item);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("switchMap", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IDateFormatView extends ICommonView {
        void onSwitchDateFormat(DateFormatItem item);
    }
}
