package com.smona.gpstrack.login.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.param.AccountCenter;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.param.AccountInfo;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.AlarmDecorate;
import com.smona.gpstrack.db.DeviceDecorate;
import com.smona.gpstrack.db.FenceDecorate;
import com.smona.gpstrack.db.LocationDecorate;
import com.smona.gpstrack.db.table.Alarm;
import com.smona.gpstrack.db.table.Device;
import com.smona.gpstrack.login.bean.LoginBodyBean;
import com.smona.gpstrack.login.bean.LoginPushToken;
import com.smona.gpstrack.login.model.LoginModel;
import com.smona.gpstrack.thread.WorkHandlerManager;
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

    private DeviceDecorate<Device> deviceDecorate = new DeviceDecorate<>();
    private AlarmDecorate alarmDecorate = new AlarmDecorate();
    private LocationDecorate locationDecorate = new LocationDecorate();
    private FenceDecorate fenceDecorate = new FenceDecorate();

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
                clearLastAccountData();
                if (mView != null) {
                    SPUtils.put(SPUtils.LOGIN_INFO, GsonUtil.objToJson(accountInfo));
                    SPUtils.put(SPUtils.CONFIG_INFO, GsonUtil.objToJson(accountInfo));
                    AccountCenter.getInstance().setAccountInfo(accountInfo);
                    ConfigCenter.getInstance().setConfigInfo(accountInfo);
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

    private void clearLastAccountData() {
        WorkHandlerManager.getInstance().runOnWorkerThread(() -> {
            deviceDecorate.deleteAll();
            alarmDecorate.deleteAll();
            locationDecorate.deleteAll();
            fenceDecorate.deleteAll();
        });
    }

    public interface ILoginView extends ICommonView {
        void onSuccess();
    }
}
