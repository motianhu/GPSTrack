package com.smona.gpstrack.login.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.param.ParamCenter;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.UrlBean;
import com.smona.gpstrack.common.param.ConfigParam;
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

        loginModel.login(urlBean, bodyBean, new OnResultListener<ConfigParam>() {
            @Override
            public void onSuccess(ConfigParam configParam) {
                if (mView != null) {
                    SPUtils.put("login_user", GsonUtil.objToJson(configParam));
                    ParamCenter.getInstance().setConfigParam(configParam);
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
