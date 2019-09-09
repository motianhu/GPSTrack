package com.smona.gpstrack.register;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.register.presenter.RegisterPresenter;
import com.smona.gpstrack.util.ARouterPath;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/9/19 11:24 AM
 */

@Route(path = ARouterPath.PATH_TO_REGISTER)
public class RegisterActivity extends BasePresenterActivity<RegisterPresenter, RegisterPresenter.IView> implements RegisterPresenter.IView {

    @Override
    protected RegisterPresenter initPresenter() {
        return new RegisterPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
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
