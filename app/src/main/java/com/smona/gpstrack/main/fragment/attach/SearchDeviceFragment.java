package com.smona.gpstrack.main.fragment.attach;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.main.presenter.SearchDevicePresenter;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/16/19 7:17 PM
 */
public class SearchDeviceFragment extends BasePresenterFragment<SearchDevicePresenter, SearchDevicePresenter.IView> implements SearchDevicePresenter.IView {
    @Override
    protected SearchDevicePresenter initPresenter() {
        return new SearchDevicePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map_search;
    }
}
