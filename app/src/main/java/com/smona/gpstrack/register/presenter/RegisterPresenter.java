package com.smona.gpstrack.register.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.DeviceProfile;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.register.bean.RegisterBean;
import com.smona.gpstrack.register.bean.VerifyUrlBean;
import com.smona.gpstrack.register.model.RegisterModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/9/19 2:25 PM
 */
public class RegisterPresenter extends BasePresenter<RegisterPresenter.IRegisterView> {
    private RegisterModel mModel = new RegisterModel();

    public void register(String userName, String email, String pwd, String cpwd) {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ParamConstant.LOCALE_EN);

        RegisterBean registerBean = new RegisterBean();
        registerBean.setName(userName);
        registerBean.setPwd(pwd);
        registerBean.setCpwd(cpwd);
        registerBean.setLocale(ParamConstant.LOCALE_EN);
        registerBean.setEmail(email);
        registerBean.setTimeZone(ParamConstant.TIME_ZONE_HK);

        mModel.register(urlBean, registerBean, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if (mView != null) {
                    mView.onRegisterSuccess();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("register", stateCode, errorInfo);
                }
            }
        });
    }

    public void verify(String email, String verifyCode) {
        VerifyUrlBean urlBean = new VerifyUrlBean();
        urlBean.setCode(verifyCode);
        urlBean.setEmail(email);
        urlBean.setImei(DeviceProfile.getIMEI());
        urlBean.setLocale(ParamConstant.LOCALE_EN);

        mModel.register(urlBean, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if (mView != null) {
                    mView.onVerifySuccess();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("verify", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IRegisterView extends ICommonView {
        void onRegisterSuccess();

        void onVerifySuccess();
    }
}
