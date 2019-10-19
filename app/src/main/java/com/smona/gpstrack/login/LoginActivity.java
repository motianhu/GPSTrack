package com.smona.gpstrack.login;

import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.login.presenter.LoginPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/9/19 10:42 AM
 */

@Route(path = ARouterPath.PATH_TO_LOGIN)
public class LoginActivity extends BasePresenterActivity<LoginPresenter, LoginPresenter.ILoginView> implements LoginPresenter.ILoginView {

    private EditText emailEt;
    private EditText emailPwd;

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        setStatusBar(R.color.white);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.tv_forget_password).setOnClickListener(view -> ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_FORGETPWD));
        findViewById(R.id.tv_reigster).setOnClickListener(view -> ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_REGISTER));
        findViewById(R.id.btn_login).setOnClickListener(view -> clickLogin());

        emailEt = findViewById(R.id.et_input_email);
        emailPwd = findViewById(R.id.et_input_password);
    }

    private void clickLogin() {
        showLoadingDialog();
        mPresenter.login(emailEt.getText().toString(), emailPwd.getText().toString());
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSuccess() {
        hideLoadingDialog();
        supportFinishAfterTransition();
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_MAIN);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errMsg) {
        hideLoadingDialog();
        ToastUtil.showShort(errMsg.getMessage());
    }
}

