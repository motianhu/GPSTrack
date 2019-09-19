package com.smona.gpstrack.login.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ConstParam;
import com.smona.gpstrack.common.IView;
import com.smona.gpstrack.common.bean.UrlBean;
import com.smona.gpstrack.login.bean.LoginBodyBean;
import com.smona.gpstrack.login.bean.RespLoginBean;
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
public class LoginPresenter extends BasePresenter<LoginPresenter.ILoginView> {

    private LoginModel loginModel = new LoginModel();

    public void login(String email, String password) {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ConstParam.LOCALE_EN);
        LoginBodyBean bodyBean = new LoginBodyBean();
        bodyBean.setEmail(email);
        bodyBean.setPwd(password);

        loginModel.login(urlBean, bodyBean, new OnResultListener<RespLoginBean>() {
            @Override
            public void onSuccess(RespLoginBean respEmptyBean) {
                if (mView != null) {
                    mView.onSuccess();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("login", stateCode, errorInfo);
                }
            }
        });
    }

    public interface ILoginView extends IView {
        void onSuccess();
    }
}
