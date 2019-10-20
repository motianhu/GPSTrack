package com.smona.gpstrack.settings.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.settings.model.SettingModel;

public class UpdatePwdPresenter extends BasePresenter<UpdatePwdPresenter.IUpdatePwdView> {

    public void updatePwd() {

    }

    public interface IUpdatePwdView extends ICommonView {
        void onUpdatePwd();
    }
}
