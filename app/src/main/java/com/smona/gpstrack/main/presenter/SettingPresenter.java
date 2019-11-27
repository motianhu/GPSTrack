package com.smona.gpstrack.main.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.DeviceProfile;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.common.param.ConfigInfo;
import com.smona.gpstrack.settings.bean.AppNoticeItem;
import com.smona.gpstrack.settings.bean.LogoutItem;
import com.smona.gpstrack.settings.bean.UserNameItem;
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
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        model.requestViewAccount(urlBean, new OnResultListener<ConfigInfo>() {
            @Override
            public void onSuccess(ConfigInfo configInfo) {
                if (mView != null) {
                    mView.onViewAccount(configInfo);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("viewAccount", stateCode, errorInfo);
                }
            }
        });
    }

    public void editName(String name) {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());

        UserNameItem userNameItem = new UserNameItem();
        userNameItem.setName(name);

        model.modifyUserName(urlBean,userNameItem, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean emptyBean) {
                if (mView != null) {
                    mView.onModifyUserName(name);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("editName", stateCode, errorInfo);
                }
            }
        });
    }

    public void editAppNotice(boolean appNotice) {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());

        AppNoticeItem appNoticeItem = new AppNoticeItem();
        appNoticeItem.setAppNotice(appNotice);

        model.modifyAppNotice(urlBean,appNoticeItem, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean emptyBean) {
                if (mView != null) {
                    mView.onModifyAppNotice(appNotice);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("editAppNotice", stateCode, errorInfo);
                }
            }
        });
    }

    public void logout() {
        LogoutItem logoutItem = new LogoutItem();
        logoutItem.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        logoutItem.setImei(DeviceProfile.getIMEI());
        model.logout(logoutItem, new OnResultListener<RespEmptyBean>(){

            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if(mView != null) {
                    mView.onLogout();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("logout", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IView extends ICommonView {
        void onViewAccount(ConfigInfo configInfo);
        void onModifyUserName(String content);
        void onModifyAppNotice(boolean enable);
        void onLogout();
    }
}
