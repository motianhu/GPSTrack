package com.smona.gpstrack.main.fragment;

import android.view.View;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.main.presenter.SettingPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:34 PM
 */
public class SettingMainFragment extends BasePresenterFragment<SettingPresenter, SettingPresenter.IView> implements SettingPresenter.IView {
    @Override
    protected SettingPresenter initPresenter() {
        return new SettingPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView(View content) {
        super.initView(content);
        content.findViewById(R.id.switchLanguage).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));
        content.findViewById(R.id.switchMap).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));
        content.findViewById(R.id.switchTimeZone).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));
        content.findViewById(R.id.switchDate).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));
        content.findViewById(R.id.aboutUs).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));
        content.findViewById(R.id.modifyPwd).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));
        content.findViewById(R.id.protocal).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));
        content.findViewById(R.id.cleanCache).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));
        content.findViewById(R.id.modifyUserName).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));
        content.findViewById(R.id.logout).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void gotoActivity(String path) {
        ARouterManager.getInstance().gotoActivity(path);
    }
}


