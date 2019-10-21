package com.smona.gpstrack.login.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.param.ParamCenter;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.param.AccountInfo;
import com.smona.gpstrack.login.bean.LoginBodyBean;
import com.smona.gpstrack.login.model.LoginModel;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;
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
        urlBean.setLocale(ParamConstant.LOCALE_EN);
        LoginBodyBean bodyBean = new LoginBodyBean();
        bodyBean.setEmail(email);
        bodyBean.setPwd(password);

        loginModel.login(urlBean, bodyBean, new OnResultListener<AccountInfo>() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                if (mView != null) {
                    SPUtils.put(SPUtils.LOGIN_INFO, GsonUtil.objToJson(accountInfo));
                    SPUtils.put(SPUtils.CONFIG_INFO, GsonUtil.objToJson(accountInfo));
                    ParamCenter.getInstance().setAccountInfo(accountInfo);
                    ParamCenter.getInstance().setConfigInfo(accountInfo);
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

    public interface ILoginView extends ICommonView {
        void onSuccess();
    }
}
