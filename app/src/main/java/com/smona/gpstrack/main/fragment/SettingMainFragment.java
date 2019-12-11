package com.smona.gpstrack.main.fragment;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.smona.base.ui.fragment.BasePresenterFragment;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.common.param.ConfigInfo;
import com.smona.gpstrack.device.dialog.EditCommonDialog;
import com.smona.gpstrack.device.dialog.HintCommonDialog;
import com.smona.gpstrack.main.presenter.SettingPresenter;
import com.smona.gpstrack.thread.WorkHandlerManager;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.GsonUtil;
import com.smona.gpstrack.util.SPUtils;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 2:34 PM
 */
public class SettingMainFragment extends BasePresenterFragment<SettingPresenter, SettingPresenter.IView> implements SettingPresenter.IView {

    private boolean notInitFinish = true;

    private TextView mapTv;
    private TextView languageTv;
    private TextView timeZoneTv;
    private TextView dateFormatTv;
    private TextView userNameTv;
    private SwitchCompat appNoticeSw;

    private HintCommonDialog hintCommonDialog;
    private EditCommonDialog editCommonDialog;

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
        content.findViewById(R.id.cleanCache).setOnClickListener(v -> clickClearCache());
        content.findViewById(R.id.modifyUserName).setOnClickListener(v -> clickEditName());
        content.findViewById(R.id.logout).setOnClickListener(v -> clickLogout());

        mapTv = content.findViewById(R.id.curMap);
        languageTv = content.findViewById(R.id.curLanguage);
        timeZoneTv = content.findViewById(R.id.curTimeZone);
        dateFormatTv = content.findViewById(R.id.curDateFormat);
        appNoticeSw = content.findViewById(R.id.swtichAppNotice);
        appNoticeSw.setOnClickListener(v-> requestAppNotice());

        userNameTv = content.findViewById(R.id.userName);

        hintCommonDialog = new HintCommonDialog(mActivity);
        editCommonDialog = new EditCommonDialog(mActivity);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded()) {
            if (notInitFinish) {
                notInitFinish = false;
                mPresenter.requestViewAccount();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshConfigInfoUI(ConfigCenter.getInstance().getConfigInfo());
    }

    private void requestAppNotice() {
        showLoadingDialog();
        appNoticeSw.setChecked(ConfigCenter.getInstance().getConfigInfo().isAppNotice());
        mPresenter.editAppNotice(!ConfigCenter.getInstance().getConfigInfo().isAppNotice());
    }

    private void refreshConfigInfoUI(ConfigInfo configParam) {
        userNameTv.setText(configParam.getName());
        languageTv.setText(ParamConstant.LANUAGEMAP.get(configParam.getLocale()));
        mapTv.setText(ParamConstant.MAPMAP.get(configParam.getMapDefault().toLowerCase()));
        timeZoneTv.setText(configParam.getTimeZone());
        dateFormatTv.setText(configParam.getDateFormat().toUpperCase());
        appNoticeSw.setChecked(ConfigCenter.getInstance().getConfigInfo().isAppNotice());
    }

    private void gotoActivity(String path) {
        ARouterManager.getInstance().gotoActivity(path);
    }

    private void clickClearCache() {
        hintCommonDialog.setContent(getString(R.string.clear_cache));
        hintCommonDialog.setOnCommitListener((dialog, confirm) -> {
            dialog.dismiss();
            WorkHandlerManager.getInstance().runOnWorkerThread(CommonUtils::clearAllCache);
        });
        hintCommonDialog.show();
    }

    private void clickEditName() {
        editCommonDialog.setIv(-1);
        editCommonDialog.setMaxLength(CommonUtils.MAX_NAME_LENGHT);
        editCommonDialog.setTitle(R.string.modifyUserName);
        editCommonDialog.setContent(ConfigCenter.getInstance().getConfigInfo().getName());
        editCommonDialog.setOnCommitListener((dialog, content) -> {
            dialog.dismiss();
            showLoadingDialog();
            mPresenter.editName(content);
        });
        editCommonDialog.show();
    }

    private void clickLogout() {
        hintCommonDialog.setContent(getString(R.string.logout_ok));
        hintCommonDialog.setOnCommitListener((dialog, confirm) -> {
            dialog.dismiss();
            showLoadingDialog();
            mPresenter.logout();
        });
        hintCommonDialog.show();
    }

    @Override
    public void onViewAccount(ConfigInfo configInfo) {
        hideLoadingDialog();
        if (configInfo == null) {
            return;
        }
        ConfigCenter.getInstance().setConfigInfo(configInfo);
        refreshConfigInfoUI(configInfo);
    }

    @Override
    public void onModifyUserName(String content) {
        hideLoadingDialog();
        ConfigCenter.getInstance().getConfigInfo().setName(content);
        SPUtils.put(SPUtils.CONFIG_INFO, GsonUtil.objToJson(ConfigCenter.getInstance().getConfigInfo()));
        userNameTv.setText(content);
    }

    @Override
    public void onModifyAppNotice(boolean enable) {
        hideLoadingDialog();
        ConfigCenter.getInstance().getConfigInfo().setAppNotice(enable);
        SPUtils.put(SPUtils.CONFIG_INFO, GsonUtil.objToJson(ConfigCenter.getInstance().getConfigInfo()));
        appNoticeSw.setChecked(enable);
    }

    @Override
    public void onLogout() {
        hideLoadingDialog();
        mActivity.finish();
        SPUtils.put(SPUtils.LOGIN_INFO, "");
        SPUtils.put(SPUtils.CONFIG_INFO, "");
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_LOGIN);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        CommonUtils.showToastByFilter(errCode, errorInfo.getMessage());
    }
}


