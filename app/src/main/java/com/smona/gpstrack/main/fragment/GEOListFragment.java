package com.smona.gpstrack.main.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.geo.bean.GeoBean;
import com.smona.gpstrack.geo.presenter.GeoListPresenter;
import com.smona.gpstrack.main.adapter.GEOAdapter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:35 PM
 */
public class GEOListFragment extends BasePresenterFragment<GeoListPresenter, GeoListPresenter.IGeoListView> implements GeoListPresenter.IGeoListView, GEOAdapter.IOnGoeEnableListener {

    private GEOAdapter geoAdapter;

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
        initHeader(content);
        initViews(content);
    }

    private void initHeader(View content) {
        content.findViewById(R.id.back).setVisibility(View.GONE);
        TextView titleTv = content.findViewById(R.id.title);
        titleTv.setText(R.string.ele_fence);
        ImageView rightView = content.findViewById(R.id.rightIv);
        rightView.setVisibility(View.VISIBLE);
        rightView.setImageResource(R.drawable.addition);
        rightView.setOnClickListener(v -> clickAddGeo());
    }

    private void initViews(View content) {
        XRecyclerView recyclerView = content.findViewById(R.id.xrecycler_wiget);
        geoAdapter = new GEOAdapter(R.layout.adapter_item_geo);
        geoAdapter.setOnGeoEnable(this);
        recyclerView.setAdapter(geoAdapter);

        WidgetComponent.initXRecyclerView(mActivity, recyclerView, new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshGeoList();
            }

            @Override
            public void onLoadMore() {
                requestGeoList();
            }
        });
    }


    private void clickAddGeo() {
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_EDIT_GEO);
    }

    @Override
    protected void initData() {
        super.initData();
        requestGeoList();
    }

    private void requestGeoList() {
        mPresenter.requestGeoList();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        ToastUtil.showShort(errorInfo.getMessage());
    }

    @Override
    public void onSuccess(List<GeoBean> datas) {
        geoAdapter.addData(datas);
    }

    @Override
    public void onGeoEnable(boolean enable, GeoBean geoBean) {
        mPresenter.refreshGeoList();
    }
}
