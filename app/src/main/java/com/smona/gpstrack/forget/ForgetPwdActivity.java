package com.smona.gpstrack.forget;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.R;
import com.smona.gpstrack.util.ARouterPath;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/9/19 2:29 PM
 */

@Route(path = ARouterPath.PATH_TO_FORGETPWD)
public class ForgetPwdActivity extends BasePresenterActivity {
    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgetpwd;
    }
}
