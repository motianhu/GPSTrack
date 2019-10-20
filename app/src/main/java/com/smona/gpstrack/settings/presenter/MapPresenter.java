package com.smona.gpstrack.settings.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.settings.model.SettingModel;

public class MapPresenter extends BasePresenter<MapPresenter.IMapView> {
    private SettingModel model = new SettingModel();

    public void switchMap() {
        model.switchMap();
    }

    public interface IMapView extends ICommonView {
        void onSwitchMap();
    }
}
