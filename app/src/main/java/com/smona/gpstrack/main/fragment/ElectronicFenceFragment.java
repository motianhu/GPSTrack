package com.smona.gpstrack.main.fragment;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.main.presenter.EleFencePresenter;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:35 PM
 */
public class ElectronicFenceFragment extends BasePresenterFragment<EleFencePresenter, EleFencePresenter.IView> implements EleFencePresenter.IView {

    @Override
    protected EleFencePresenter initPresenter() {
        return new EleFencePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ele_fance;
    }
}
