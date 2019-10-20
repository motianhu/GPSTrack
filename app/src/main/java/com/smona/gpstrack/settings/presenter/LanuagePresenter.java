package com.smona.gpstrack.settings.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.settings.model.SettingModel;

public class LanuagePresenter extends BasePresenter<LanuagePresenter.ILanuageView> {
    private SettingModel model = new SettingModel();

    public void switchLanuage() {
        model.switchLanuage();
    }

    public interface ILanuageView extends ICommonView {
        void onSwitchLanuage();
    }
}
