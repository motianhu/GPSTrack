package com.smona.gpstrack.settings.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.settings.model.SettingModel;

public class TimeZonePresenter extends BasePresenter<TimeZonePresenter.ITimeZoneView> {
    private SettingModel model = new SettingModel();

    public void switchTimeZone() {
        model.switchTimeZone();
    }

    public interface ITimeZoneView extends ICommonView {
        void onSwitchTimeZone();
    }
}
