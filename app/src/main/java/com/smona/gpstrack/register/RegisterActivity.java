package com.smona.gpstrack.register;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.firebase.iid.FirebaseInstanceId;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.BaseLanuagePresenterActivity;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.register.presenter.RegisterPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.gpstrack.widget.PwdEditText;
import com.smona.http.wrapper.ErrorInfo;

import java.util.Locale;

/**
 * description:
 * 注册页
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/9/19 11:24 AM
 */

@Route(path = ARouterPath.PATH_TO_REGISTER)
public class RegisterActivity extends BaseLanuagePresenterActivity<RegisterPresenter, RegisterPresenter.IRegisterView> implements RegisterPresenter.IRegisterView {

    private View registerLL;
    private View verifyLL;

    private  TextView titleTv;

    private EditText userNameEt;
    private EditText userEmailEt;
    private EditText userPwdEt;
    private EditText userConfirmPwdEt;
    private CompoundButton checkBox;

    private TextView emailCodeTv;

    private PwdEditText verifyEt;

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
        findViewById(R.id.back).setOnClickListener(view -> onBackPressed());
        titleTv = findViewById(R.id.title);
        titleTv.setText(R.string.register);

        registerLL = findViewById(R.id.register_ll);
        verifyLL = findViewById(R.id.verify_ll);
        verifyLL.setVisibility(View.GONE);

        userNameEt = findViewById(R.id.user_name);
        CommonUtils.setMaxLenght(userNameEt, CommonUtils.MAX_NAME_LENGHT);
        userEmailEt = findViewById(R.id.user_email);
        CommonUtils.setMaxLenght(userEmailEt, CommonUtils.MAX_NAME_LENGHT);
        userPwdEt = findViewById(R.id.user_password);
        CommonUtils.disableEditTextCopy(userPwdEt);
        CommonUtils.setMaxLenght(userPwdEt, CommonUtils.MAX_PWD_LENGHT);
        userConfirmPwdEt = findViewById(R.id.confirm_password);
        CommonUtils.disableEditTextCopy(userConfirmPwdEt);
        CommonUtils.setMaxLenght(userConfirmPwdEt, CommonUtils.MAX_PWD_LENGHT);
        verifyEt = findViewById(R.id.et_email_code);
        checkBox = findViewById(R.id.cb_protocal);
        findViewById(R.id.tv_protocal).setOnClickListener(v -> ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_SETTING_PROTOCAL));

        emailCodeTv = findViewById(R.id.tv_email_hint);

        findViewById(R.id.btn_register).setOnClickListener(view -> clickRegister(userNameEt.getText().toString(), userEmailEt.getText().toString(), userPwdEt.getText().toString(), userConfirmPwdEt.getText().toString()));
        findViewById(R.id.btn_verify).setOnClickListener(view -> clickVerify(userEmailEt.getText().toString(), verifyEt.getText().toString()));
    }

    private void clickRegister(String userName, String email, String pwd, String cpwd) {
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showShort(R.string.empty_username);
            return;
        }
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
        if (TextUtils.isEmpty(cpwd)) {
            ToastUtil.showShort(R.string.empty_cpwd);
            return;
        }
        if (cpwd.length() < 8) {
            ToastUtil.showShort(R.string.no_than_c_pwd);
            return;
        }
        if (!pwd.equals(cpwd)) {
            ToastUtil.showShort(R.string.not_pwd_common);
            return;
        }
        if (!checkBox.isChecked()) {
            ToastUtil.showShort(R.string.dont_protocal);
            return;
        }
        showLoadingDialog();
        mPresenter.register(userName, email, pwd, cpwd);
    }

    private void clickVerify(String email, String code) {
        if (TextUtils.isEmpty(code) || code.length() != 6) {
            ToastUtil.showShort(R.string.email_code_error);
            return;
        }
        showLoadingDialog();
        mPresenter.verify( email, code);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        CommonUtils.showToastByFilter(errCode, errorInfo.getMessage());
    }

    @Override
    public void onRegisterSuccess() {
        hideLoadingDialog();
        showVerify();
        emailCodeTv.setText(R.string.receive_email_code);
    }

    /**
     * 隐藏注册页，显示验证页
     */
    private void showVerify() {
        registerLL.setVisibility(View.GONE);
        verifyLL.setVisibility(View.VISIBLE);
        titleTv.setText(R.string.verification);
    }

    /**
     * 隐藏验证页，显示注册页
     */
    private void hideVerify() {
        registerLL.setVisibility(View.VISIBLE);
        verifyLL.setVisibility(View.GONE);
        titleTv.setText(R.string.register);
        verifyEt.setText("");
    }

    @Override
    public void onBackPressed() {
        if (registerLL.getVisibility() == View.GONE) {
            hideVerify();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onVerifySuccess() {
        hideLoadingDialog();
        registerGooglePush();
        ToastUtil.showShort(R.string.register_success);

        setLanguage(ConfigCenter.getInstance().getConfigInfo().getLocale());
        sendCloseAllActivity();
        ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_MAIN);
        finish();
    }

    //销毁所有Activity，进入首页
    private void sendCloseAllActivity() {
        CommonUtils.sendCloseAllActivity(this);
        ARouterManager.getInstance().gotoActivityWithString(ARouterPath.PATH_TO_MAIN, ARouterPath.PATH_TO_MAIN, ARouterPath.PATH_TO_MAIN);
    }

    //设置应用语言
    private void setLanguage(String language) {
        Locale locale = Locale.ENGLISH;
        if(ParamConstant.LOCALE_ZH_CN.equals(language)) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else  if(ParamConstant.LOCALE_ZH_TW.equals(language)) {
            locale = Locale.TAIWAN;
        }
        setAppLanguage(locale);
    }

    //注册Push
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
}
