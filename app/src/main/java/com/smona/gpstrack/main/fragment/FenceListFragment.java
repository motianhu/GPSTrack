package com.smona.gpstrack.main.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.BasePresenterLoadingFragment;
import com.smona.gpstrack.common.param.AccountCenter;
import com.smona.gpstrack.component.WidgetComponent;
import com.smona.gpstrack.fence.bean.FenceBean;
import com.smona.gpstrack.fence.presenter.FenceListPresenter;
import com.smona.gpstrack.main.adapter.FenceAdapter;
import com.smona.gpstrack.notify.NotifyCenter;
import com.smona.gpstrack.notify.event.FenceAddEvent;
import com.smona.gpstrack.notify.event.FenceDelEvent;
import com.smona.gpstrack.notify.event.FenceUpdateEvent;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.http.wrapper.ErrorInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:35 PM
 */
public class FenceListFragment extends BasePresenterLoadingFragment<FenceListPresenter, FenceListPresenter.IGeoListView> implements FenceListPresenter.IGeoListView, FenceAdapter.IOnGoeEnableListener {

    private XRecyclerView recyclerView;
    private FenceAdapter fenceAdapter;
    private ImageView addView;

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
        addView = content.findViewById(R.id.back);
        addView.setVisibility(View.VISIBLE);
        addView.setImageResource(R.drawable.addition);
        addView.setOnClickListener(v -> clickAddGeo());

        TextView titleTv = content.findViewById(R.id.title);
        titleTv.setText(R.string.ele_fence);
    }

    private void initViews(View content) {
        recyclerView = content.findViewById(R.id.xrecycler_wiget);
        fenceAdapter = new FenceAdapter(R.layout.adapter_item_geo);
        fenceAdapter.setOnGeoEnable(this);
        recyclerView.setAdapter(fenceAdapter);

        WidgetComponent.initXRecyclerView(mActivity, recyclerView);

        initExceptionProcess(content.findViewById(R.id.loadingresult), recyclerView);
        NotifyCenter.getInstance().registerListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotifyCenter.getInstance().unRegisterListener(this);
    }

    private void clickAddGeo() {
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_EDIT_GEO);
    }

    @Override
    protected void requestData() {
        mPresenter.requestGeoList();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        onError(api, errCode, errorInfo, this::requestData);
        if ("".equalsIgnoreCase(api)) {
            fenceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEmpty() {
        hideLoadingDialog();
        doEmpty();
    }

    @Override
    public void onFinishMoreLoad() {
        hideLoadingDialog();
        recyclerView.setNoMore(true);
    }

    @Override
    public void onFenceList(int curPage, List<FenceBean> datas) {
        hideLoadingDialog();
        doSuccess();
        if (curPage == 0) {
            fenceAdapter.setNewData(datas);
        } else {
            fenceAdapter.addData(datas);
        }
        recyclerView.loadMoreComplete();

        if (fenceAdapter.getItemCount() >= AccountCenter.getInstance().getAccountInfo().getGeoFenceLimit()) {
            addView.setVisibility(View.GONE);
        } else {
            addView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUpdate() {
        hideLoadingDialog();
        fenceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGeoEnable(FenceBean geoBean) {
        showLoadingDialog();
        geoBean.setStatus(FenceBean.STATUS_ENABLE.equals(geoBean.getStatus()) ? FenceBean.STATUS_DISABLE : FenceBean.STATUS_ENABLE);
        mPresenter.updateGeoInfo(geoBean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgDelFence(FenceDelEvent event) {
        fenceAdapter.removeFence(event.getFenceId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgAddFence(FenceAddEvent event) {
        fenceAdapter.addFence(event.getAddFence());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bgAddFence(FenceUpdateEvent event) {
        fenceAdapter.updateFence(event.getUpdateFence());
    }
}
