package com.smona.gpstrack.register.presenter;

import android.text.TextUtils;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.DeviceProfile;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.param.AccountCenter;
import com.smona.gpstrack.common.param.AccountInfo;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.common.param.ConfigInfo;
import com.smona.gpstrack.login.bean.LoginPushToken;
import com.smona.gpstrack.login.model.LoginModel;
import com.smona.gpstrack.register.bean.RegisterBean;
import com.smona.gpstrack.register.bean.VerifyUrlBean;
import com.smona.gpstrack.register.model.RegisterModel;
import com.smona.gpstrack.thread.WorkHandlerManager;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;
import com.smona.gpstrack.util.TimeStamUtil;
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
    private LoginModel loginModel = new LoginModel();

    public void register(String curSysLa, String userName, String email, String pwd, String cpwd) {
        UrlBean urlBean = new UrlBean();
        RegisterBean registerBean = new RegisterBean();
        registerBean.setName(userName);
        registerBean.setPwd(pwd);
        registerBean.setCpwd(cpwd);
        ConfigInfo configInfo = ConfigCenter.getInstance().getConfigInfo();
        String language = configInfo != null ? configInfo.getLocale() : "";
        if (TextUtils.isEmpty(language)) {
            Integer value = ParamConstant.LANUAGEMAP.get(curSysLa);
            if (value == null || value == 0) {
                language = ParamConstant.LOCALE_EN;
            } else {
                language = curSysLa;
            }
        }
        urlBean.setLocale(language);
        registerBean.setEmail(email);
        registerBean.setLocale(language);
        registerBean.setTimeZone(TimeStamUtil.getCurTimeZone());

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

    public void verify(String curSysLa, String email, String verifyCode) {
        VerifyUrlBean urlBean = new VerifyUrlBean();
        urlBean.setCode(verifyCode);
        urlBean.setEmail(email);
        urlBean.setImei(DeviceProfile.getIMEI());
        ConfigInfo configInfo = ConfigCenter.getInstance().getConfigInfo();
        String language = configInfo != null ? configInfo.getLocale() : "";
        if (TextUtils.isEmpty(language)) {
            Integer value = ParamConstant.LANUAGEMAP.get(curSysLa);
            if (value == null || value == 0) {
                language = ParamConstant.LOCALE_EN;
            } else {
                language = curSysLa;
            }
        }
        urlBean.setLocale(language);

        mModel.verify(urlBean, new OnResultListener<AccountInfo>() {
            @Override
            public void onSuccess(AccountInfo accountInfo) {
                clearLastAccountData();
                if (mView != null) {
                    SPUtils.put(SPUtils.LOGIN_INFO, GsonUtil.objToJson(accountInfo));
                    SPUtils.put(SPUtils.CONFIG_INFO, GsonUtil.objToJson(accountInfo));
                    AccountCenter.getInstance().setAccountInfo(accountInfo);
                    ConfigCenter.getInstance().setConfigInfo(accountInfo);
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

    public void sendPushToken(String pushToken) {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        LoginPushToken loginPushToken = new LoginPushToken();
        loginPushToken.setPushToken(pushToken);
        loginModel.sendGooglePushToken(urlBean, loginPushToken, null);
    }

    private void clearLastAccountData() {
        WorkHandlerManager.getInstance().runOnWorkerThread(CommonUtils::clearAllCache);
    }

    public interface IRegisterView extends ICommonView {
        void onRegisterSuccess();

        void onVerifySuccess();
    }
}
