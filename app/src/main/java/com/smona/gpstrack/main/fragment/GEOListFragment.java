package com.smona.gpstrack.main.fragment;

import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.geo.presenter.GeoListPresenter;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:35 PM
 */
public class GEOListFragment extends BasePresenterFragment<GeoListPresenter, GeoListPresenter.IGeoListView> implements GeoListPresenter.IGeoListView {

    private XRecyclerView recyclerView;

    @Override
    protected GeoListPresenter initPresenter() {
        return new GeoListPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_geo_list;
    }

    @Override
    protected void initView(View content) {
        super.initView(content);
        recyclerView = content.findViewById(R.id.xrecycler_wiget);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
