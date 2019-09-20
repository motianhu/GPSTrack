package com.smona.gpstrack.changepwd;

import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.changepwd.presenter.ChangePwdPreseneter;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 11:43 AM
 */
public class ChangePwdActivity extends BasePresenterActivity<ChangePwdPreseneter, ChangePwdPreseneter.IChangePwdView> implements ChangePwdPreseneter.IChangePwdView {
    @Override
    protected ChangePwdPreseneter initPresenter() {
        return new ChangePwdPreseneter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_changepwd;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
