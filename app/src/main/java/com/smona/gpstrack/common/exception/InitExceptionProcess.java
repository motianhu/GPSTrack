package com.smona.gpstrack.common.exception;

import android.util.Log;
import android.view.View;

import com.smona.gpstrack.R;
import com.smona.gpstrack.widget.LoadingResultView;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 7/23/19 4:33 PM
 */
public class InitExceptionProcess implements IExceptionProcess {

    private LoadingResultView mLoadingErrorView;
    private View[] mContentViews;
    private ExpceptionFilterExecutor mStartFilter;

    public InitExceptionProcess() {
        mStartFilter = new ExpceptionFilterExecutor();
        mStartFilter.initFilter(this);
    }

    public void initViews(LoadingResultView errView, View... views) {
        mLoadingErrorView = errView;
        mContentViews = views;

        mLoadingErrorView.setLoading();
        for (View view : mContentViews) {
            view.setVisibility(View.GONE);
        }
    }

    public void doSuccess() {
        mLoadingErrorView.setIdle();
        for (View view : mContentViews) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void doError(String api, int errCode, String errMsg, OnReloadListener listener) {
        Log.e("InitExceptionProcess", "api: " + api + ", errCode: " + errCode + ", errMsg: " + errMsg);
        //第一批必须以_first展示
        mStartFilter.executeFilter(api, errCode, errMsg, listener);
    }

    public void doEmpty() {
        mLoadingErrorView.setNoContent(mLoadingErrorView.getContext().getString(R.string.no_content), R.drawable.nodata);
        for (View view : mContentViews) {
            view.setVisibility(View.GONE);
        }
    }

    public void doEmpty(String tips, int resId) {
        mLoadingErrorView.setNoContent(tips, resId);
        for (View view : mContentViews) {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public LoadingResultView getErrorView() {
        return mLoadingErrorView;
    }

    @Override
    public View[] getContentViews() {
        return mContentViews;
    }

    public interface OnReloadListener {
        void onReload();
    }
}
