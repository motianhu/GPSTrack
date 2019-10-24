package com.smona.gpstrack.main.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.BasePresenterLoadingFragment;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.geo.bean.FenceBean;
import com.smona.gpstrack.geo.presenter.FenceListPresenter;
import com.smona.gpstrack.main.adapter.FenceAdapter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.http.wrapper.ErrorInfo;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:35 PM
 */
public class FenceListFragment extends BasePresenterLoadingFragment<FenceListPresenter, FenceListPresenter.IGeoListView> implements FenceListPresenter.IGeoListView, FenceAdapter.IOnGoeEnableListener {

    private FenceAdapter fenceAdapter;

    @Override
    protected FenceListPresenter initPresenter() {
        return new FenceListPresenter();
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
        fenceAdapter = new FenceAdapter(R.layout.adapter_item_geo);
        fenceAdapter.setOnGeoEnable(this);
        recyclerView.setAdapter(fenceAdapter);

        WidgetComponent.initXRecyclerView(mActivity, recyclerView);

        initExceptionProcess(content.findViewById(R.id.loadingresult), recyclerView);
    }


    private void clickAddGeo() {
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_EDIT_GEO);
    }

    @Override
    protected void initData() {
        super.initData();
        requestFenceList();
    }

    private void requestFenceList() {
        mPresenter.requestGeoList();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        onError(api, errCode, errorInfo, this::requestFenceList);
    }

    @Override
    public void onSuccess(List<FenceBean> datas) {
        if (datas == null || datas.isEmpty()) {
            doEmpty();
            return;
        }
        doSuccess();
        fenceAdapter.addData(datas);
    }

    @Override
    public void onGeoEnable(boolean enable, FenceBean geoBean) {
        mPresenter.refreshGeoList();
    }
}
