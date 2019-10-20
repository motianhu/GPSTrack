package com.smona.gpstrack.settings;

import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.settings.presenter.UpdatePwdPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.http.wrapper.ErrorInfo;

@Route(path = ARouterPath.PATH_TO_SETTING_UPDATE_PWD)
public class UpdatePwdActivity extends BasePresenterActivity<UpdatePwdPresenter, UpdatePwdPresenter.IUpdatePwdView> implements UpdatePwdPresenter.IUpdatePwdView {

    @Override
    protected UpdatePwdPresenter initPresenter() {
        return new UpdatePwdPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_pwd;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.modifyPwd);
        findViewById(R.id.back).setOnClickListener(v-> finish());
    }

    @Override
    public void onUpdatePwd() {

    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
