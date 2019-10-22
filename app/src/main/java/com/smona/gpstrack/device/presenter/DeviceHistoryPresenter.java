package com.smona.gpstrack.device.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.db.table.Location;
import com.smona.gpstrack.device.bean.LocationListBean;
import com.smona.gpstrack.device.bean.req.ReqLocationList;
import com.smona.gpstrack.device.model.DeviceLocationModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/29/19 1:02 PM
 */
public class DeviceHistoryPresenter extends BasePresenter<DeviceHistoryPresenter.IDeviceHistory> {

    private DeviceLocationModel mModel = new DeviceLocationModel();

    public void requestHistoryLocation(String deviceId, String dateFrom, String dateTo) {
        ReqLocationList urlBean = new ReqLocationList();
        urlBean.setDevicePlatformId(deviceId);
        urlBean.setDateFrom(dateFrom);
        urlBean.setDateTo(dateTo);
        urlBean.setLocale(ParamConstant.LOCALE_EN);
        urlBean.setMap(ParamConstant.MAP_AMAP);
        urlBean.setPage(0);
        urlBean.setPage_size(100);

        mModel.requestHistoryLocation(urlBean, new OnResultListener<LocationListBean>() {
            @Override
            public void onSuccess(LocationListBean locationListBean) {
                if (mView != null) {
                    mView.onSuccess(locationListBean.getDatas());
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("requestHistoryLocation", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IDeviceHistory extends ICommonView {
        void onSuccess(List<Location> datas);
    }
}
