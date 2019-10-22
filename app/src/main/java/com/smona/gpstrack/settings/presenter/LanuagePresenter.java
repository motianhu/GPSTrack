package com.smona.gpstrack.settings.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.settings.bean.LanuageItem;
import com.smona.gpstrack.settings.model.SettingModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

public class LanuagePresenter extends BasePresenter<LanuagePresenter.ILanuageView> {
    private SettingModel model = new SettingModel();

    public void switchLanuage(LanuageItem item) {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        model.switchLanuage(urlBean, item, new OnResultListener<RespEmptyBean>() {

            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if (mView != null) {
                    mView.onSwitchLanuage(item);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("switchLanuage", stateCode, errorInfo);
                }
            }
        });
    }

    public interface ILanuageView extends ICommonView {
        void onSwitchLanuage(LanuageItem item);
    }
}
