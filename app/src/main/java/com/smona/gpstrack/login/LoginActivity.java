package com.smona.gpstrack.login;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.login.presenter.LoginPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/9/19 10:42 AM
 */

@Route(path = ARouterPath.PATH_TO_LOGIN)
public class LoginActivity extends BasePresenterActivity<LoginPresenter, LoginPresenter.IView> implements LoginPresenter.IView {

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
        initViews();
    }

    private void initViews() {
        findViewById(R.id.tv_forget_password).setOnClickListener(view -> ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_FORGETPWD));
        findViewById(R.id.tv_reigster).setOnClickListener(view -> ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_REGISTER));
        findViewById(R.id.btn_login).setOnClickListener(view -> clickLogin());
    }

    private void clickLogin() {
        supportFinishAfterTransition();
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_MAIN);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

