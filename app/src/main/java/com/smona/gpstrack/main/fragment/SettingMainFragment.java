package com.smona.gpstrack.main.fragment;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.main.presenter.SettingPresenter;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:34 PM
 */
public class SettingMainFragment extends BasePresenterFragment<SettingPresenter, SettingPresenter.IView> implements SettingPresenter.IView {
    @Override
    protected SettingPresenter initPresenter() {
        return new SettingPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }
}
