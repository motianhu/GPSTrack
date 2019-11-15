package com.smona.gpstrack.main.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.bean.req.PageUrlBean;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.db.DeviceDecorate;
import com.smona.gpstrack.db.FenceDecorate;
import com.smona.gpstrack.db.table.Fence;
import com.smona.gpstrack.device.bean.DevicesAttachLocBean;
import com.smona.gpstrack.device.bean.RespDevice;
import com.smona.gpstrack.device.model.DevicesAttachLocModel;
import com.smona.gpstrack.login.bean.LoginPushToken;
import com.smona.gpstrack.login.model.LoginModel;
import com.smona.gpstrack.thread.WorkHandlerManager;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:44 PM
 */
public class MapPresenter extends BasePresenter<MapPresenter.IMapView> {

    private FenceDecorate<Fence> fenceDecorate = new FenceDecorate<>();
    private DeviceDecorate<RespDevice> deviceDecorate = new DeviceDecorate<>();
    private DevicesAttachLocModel mModel = new DevicesAttachLocModel();
    private LoginModel loginModel = new LoginModel();
    private int curPage = 0;

    public void requestDeviceList() {
        PageUrlBean urlBean = new PageUrlBean();
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        urlBean.setPage(curPage);
        urlBean.setPage_size(1000);
        mModel.requestDeviceList(urlBean, new OnResultListener<DevicesAttachLocBean>() {
            @Override
            public void onSuccess(DevicesAttachLocBean deviceListBean) {
                if (mView != null) {
                    if ((curPage + 1) < deviceListBean.getTtlPage()) {
                        curPage += 1;
                    } else {
                        curPage = 0;
                    }
                    mView.onSuccess(deviceListBean);
                    WorkHandlerManager.getInstance().runOnWorkerThread(() -> deviceDecorate.addAll(deviceListBean.getDatas()));
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    curPage = 0;
                    mView.onError("deviceList", stateCode, errorInfo);
                }
            }
        });
    }

    public void requestFenceAll() {
        WorkHandlerManager.getInstance().runOnWorkerThread(() -> {
            List<Fence> fenceList = fenceDecorate.listAll();
            refreshFenceUI(fenceList);
        });
    }

    private void refreshFenceUI(List<Fence> fenceList) {
        WorkHandlerManager.getInstance().runOnMainThread(() -> {
            if (mView != null) {
                mView.onFenceList(fenceList);
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

    public interface IMapView extends ICommonView {
        void onSuccess(DevicesAttachLocBean deviceList);

        void onFenceList(List<Fence> fenceList);
    }
}
