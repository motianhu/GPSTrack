package com.smona.gpstrack.settings.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.settings.bean.MapItem;
import com.smona.gpstrack.settings.model.SettingModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

public class MapPresenter extends BasePresenter<MapPresenter.IMapView> {
    private SettingModel model = new SettingModel();

    public void switchMap(MapItem item) {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ConfigCenter.getInstance().getConfigInfo().getLocale());
        model.switchMap(urlBean, item, new OnResultListener<RespEmptyBean>() {

            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if (mView != null) {
                    mView.onSwitchMap(item);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("switchMap", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IMapView extends ICommonView {
        void onSwitchMap(MapItem item);
    }
}
