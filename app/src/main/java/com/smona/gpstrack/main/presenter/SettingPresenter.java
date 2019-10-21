package com.smona.gpstrack.main.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.param.ConfigInfo;
import com.smona.gpstrack.settings.model.SettingModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:43 PM
 */
public class SettingPresenter extends BasePresenter<SettingPresenter.IView> {

    private SettingModel model = new SettingModel();

    public void requestViewAccount() {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ParamConstant.LOCALE_EN);
        model.requestViewAccount(urlBean, new OnResultListener<ConfigInfo>() {
            @Override
            public void onSuccess(ConfigInfo configInfo) {
                if(mView != null) {
                    mView.onViewAccount(configInfo);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if(mView != null) {
                    mView.onError("viewAccount", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IView extends ICommonView {
        void onViewAccount(ConfigInfo configInfo);
    }
}
