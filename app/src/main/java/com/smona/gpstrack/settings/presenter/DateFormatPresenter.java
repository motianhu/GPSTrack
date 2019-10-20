package com.smona.gpstrack.settings.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.settings.model.SettingModel;

public class DateFormatPresenter extends BasePresenter<DateFormatPresenter.IDateFormatView> {
    private SettingModel model = new SettingModel();

    public void switchDateFormat() {
        model.switchDateFormat();
    }

    public interface IDateFormatView extends ICommonView {
        void onSwitchDateFormat();
    }
}
