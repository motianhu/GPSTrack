package com.smona.gpstrack.login.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.base.ui.mvp.IBaseView;
import com.smona.gpstrack.common.bean.RespEmptyBean;
import com.smona.gpstrack.common.bean.UrlBean;
import com.smona.gpstrack.login.bean.LoginBodyBean;
import com.smona.gpstrack.login.model.LoginModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/9/19 2:22 PM
 */
public class LoginPresenter extends BasePresenter<LoginPresenter.IView> {

    private LoginModel loginModel = new LoginModel();

    public void login(String email, String password) {
        UrlBean urlBean = new UrlBean();
        urlBean.setLangauge("zh_TW");
        LoginBodyBean bodyBean = new LoginBodyBean();
        bodyBean.setEmail(email);
        bodyBean.setPwd(password);

        loginModel.login(urlBean, bodyBean, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if (mView != null) {
                    mView.onSuccess();
                }
            }

            @Override
            public void onError(String stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError(stateCode, errorInfo);
                }
            }
        });
    }

    public interface IView extends IBaseView {
        void onSuccess();

        void onError(String errCode, ErrorInfo errMsg);
    }
}
