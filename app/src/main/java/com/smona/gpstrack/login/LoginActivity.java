package com.smona.gpstrack.login;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.firebase.iid.FirebaseInstanceId;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.device.dialog.HintCommonDialog;
import com.smona.gpstrack.login.presenter.LoginPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

import java.util.Locale;

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
    private HintCommonDialog hintCommonDialog;

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
        findViewById(R.id.btn_login).setOnClickListener(view -> clickLogin(emailEt.getText().toString(), emailPwd.getText().toString()));

        emailEt = findViewById(R.id.et_input_email);
        emailPwd = findViewById(R.id.et_input_password);

        hintCommonDialog = new HintCommonDialog(this);
    }

    private void clickLogin(String email, String pwd) {
        if (TextUtils.isEmpty(email)) {
            ToastUtil.showShort(R.string.empty_email);
            return;
        }
        if (!CommonUtils.isEmail(email)) {
            ToastUtil.showShort(R.string.invalid_email);
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showShort(R.string.empty_pwd);
            return;
        }
        if (pwd.length() < 8) {
            ToastUtil.showShort(R.string.no_than_pwd);
            return;
        }
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
        registerGooglePush();
        if (ParamConstant.LOCALE_EN.equals(ConfigCenter.getInstance().getConfigInfo().getLocale())) {
            switchLanguage(Locale.ENGLISH);
        } else if (ParamConstant.LOCALE_ZH_CN.equals(ConfigCenter.getInstance().getConfigInfo().getLocale())) {
            switchLanguage(Locale.SIMPLIFIED_CHINESE);
        } else if (ParamConstant.LOCALE_ZH_TW.equals(ConfigCenter.getInstance().getConfigInfo().getLocale())) {
            switchLanguage(Locale.TAIWAN);
        }
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_MAIN);
        finish();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errMsg) {
        hideLoadingDialog();
        CommonUtils.showToastByFilter(errCode, errMsg.getMessage());
    }

    private void registerGooglePush() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("motianhu", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    mPresenter.sendPushToken(token);
                });
    }

    private void switchLanguage(Locale locale) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, metrics);
    }
}

