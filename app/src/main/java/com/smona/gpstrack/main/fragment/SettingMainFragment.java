package com.smona.gpstrack.main.fragment;

import android.view.View;
import android.widget.TextView;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.AccountInfo;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.common.param.ConfigInfo;
import com.smona.gpstrack.main.presenter.SettingPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:34 PM
 */
public class SettingMainFragment extends BasePresenterFragment<SettingPresenter, SettingPresenter.IView> implements SettingPresenter.IView {

    private TextView mapTv;
    private TextView languageTv;
    private TextView timeZoneTv;
    private TextView dateFormatTv;

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
        TextView titleTv = content.findViewById(R.id.title);
        titleTv.setText(R.string.settings);
        content.findViewById(R.id.back).setVisibility(View.GONE);

        content.findViewById(R.id.switchLanguage).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_SETTING_LANUAGE));
        content.findViewById(R.id.switchMap).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_SETTING_MAP));
        content.findViewById(R.id.switchTimeZone).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_SETTING_TIMEZONE));
        content.findViewById(R.id.switchDate).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_SETTING_DATEFORMAT));
        content.findViewById(R.id.aboutUs).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));
        content.findViewById(R.id.modifyPwd).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_SETTING_UPDATE_PWD));
        content.findViewById(R.id.protocal).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_SETTING_PROTOCAL));
        content.findViewById(R.id.cleanCache).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));
        content.findViewById(R.id.modifyUserName).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));
        content.findViewById(R.id.logout).setOnClickListener(v -> gotoActivity(ARouterPath.PATH_TO_ABOUT));

        mapTv = content.findViewById(R.id.curMap);
        languageTv = content.findViewById(R.id.curLanguage);
        timeZoneTv = content.findViewById(R.id.curTimeZone);
        dateFormatTv = content.findViewById(R.id.curDateFormat);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.requestViewAccount();
    }

    private void refreshConfigInfoUI(ConfigInfo configParam) {
        if (configParam == null) {
            return;
        }
        ConfigCenter.getInstance().setConfigInfo(configParam);

        languageTv.setText(ParamConstant.LANUAGEMAP.get(configParam.getLocale()));
        mapTv.setText(ParamConstant.MAPMAP.get(configParam.getMapDefault()));
        timeZoneTv.setText(configParam.getTimeZone());
        dateFormatTv.setText(configParam.getDateFormat());
    }

    private void gotoActivity(String path) {
        ARouterManager.getInstance().gotoActivity(path);
    }

    @Override
    public void onViewAccount(ConfigInfo configInfo) {
        refreshConfigInfoUI(configInfo);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        ToastUtil.showShort(errorInfo.getMessage());
    }
}


