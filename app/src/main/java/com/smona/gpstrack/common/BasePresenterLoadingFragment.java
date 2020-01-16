package com.smona.gpstrack.common;

import android.view.View;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.base.ui.mvp.BasePresenter;
import com.smona.base.ui.mvp.IView;
import com.smona.gpstrack.common.exception.InitExceptionProcess;
import com.smona.gpstrack.widget.LoadingResultView;
import com.smona.http.wrapper.ErrorInfo;

/**
 * 统一Loading加载，异常处理的Fragment。比如alarm列表/Device列表
 * @param <P>
 * @param <V>
 */
public abstract class BasePresenterLoadingFragment<P extends BasePresenter<V>, V extends IView> extends BasePresenterFragment<P, V> {

    private InitExceptionProcess initExceptionProcess;
    protected boolean notInitFinish = true;

    @Override
    protected void initView(View content) {
        super.initView(content);
        initExceptionProcess = new InitExceptionProcess();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded()) {
            if (notInitFinish) {
                notInitFinish = false;
                requestData();
            }
        }
    }

    protected void requestData() {
    }

    public void initExceptionProcess(LoadingResultView loadingResultView, View... views) {
        initExceptionProcess.initViews(loadingResultView, views);
    }

    protected void doEmpty() {
        initExceptionProcess.doEmpty();
    }

    protected void doSuccess() {
        initExceptionProcess.doSuccess();
    }

    public void onError(String api, int errCode, ErrorInfo errorInfo, InitExceptionProcess.OnReloadListener listener) {
        initExceptionProcess.doError(api, errCode, errorInfo.getMessage(), listener);
    }
}
