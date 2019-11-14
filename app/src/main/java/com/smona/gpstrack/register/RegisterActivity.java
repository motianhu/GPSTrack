package com.smona.gpstrack.register;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.dialog.HintCommonDialog;
import com.smona.gpstrack.register.presenter.RegisterPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.gpstrack.widget.PwdEditText;
import com.smona.http.wrapper.ErrorInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/9/19 11:24 AM
 */

@Route(path = ARouterPath.PATH_TO_REGISTER)
public class RegisterActivity extends BasePresenterActivity<RegisterPresenter, RegisterPresenter.IRegisterView> implements RegisterPresenter.IRegisterView {

    private View registerLL;
    private View verifyLL;

    private EditText userNameEt;
    private EditText userEmailEt;
    private EditText userPwdEt;
    private EditText userConfirmPwdEt;
    private CompoundButton checkBox;

    private TextView emailCodeTv;

    private PwdEditText verifyEt;
    private HintCommonDialog hintCommonDialog;

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
        findViewById(R.id.back).setOnClickListener(view -> finish());
        TextView titleTv = findViewById(R.id.title);
        titleTv.setText(R.string.register);

        registerLL = findViewById(R.id.register_ll);
        verifyLL = findViewById(R.id.verify_ll);
        verifyLL.setVisibility(View.GONE);

        userNameEt = findViewById(R.id.user_name);
        userEmailEt = findViewById(R.id.user_email);
        userPwdEt = findViewById(R.id.user_password);
        userConfirmPwdEt = findViewById(R.id.confirm_password);
        verifyEt = findViewById(R.id.et_email_code);
        checkBox = findViewById(R.id.cb_protocal);
        findViewById(R.id.tv_protocal).setOnClickListener(v -> ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_SETTING_PROTOCAL));

        emailCodeTv = findViewById(R.id.tv_email_hint);

        findViewById(R.id.btn_register).setOnClickListener(view -> clickRegister(userNameEt.getText().toString(), userEmailEt.getText().toString(), userPwdEt.getText().toString(), userConfirmPwdEt.getText().toString()));
        findViewById(R.id.btn_verify).setOnClickListener(view -> clickVerify(userEmailEt.getText().toString(), verifyEt.getText().toString()));

        hintCommonDialog = new HintCommonDialog(this);
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
        if (!isEmail(email)) {
            ToastUtil.showShort(R.string.invalid_email);
            return;
        }
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(cpwd)) {
            ToastUtil.showShort(R.string.empty_pwd);
            return;
        }
        if(pwd.length() < 8 || cpwd.length() < 8) {
            ToastUtil.showShort(R.string.no_than_pwd);
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

    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private void clickVerify(String email, String code) {
        if (TextUtils.isEmpty(code) || code.length() != 6) {
            ToastUtil.showShort(R.string.email_code_error);
            return;
        }
        showLoadingDialog();
        mPresenter.verify(email, code);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        ToastUtil.showShort(errorInfo.getMessage());
    }

    @Override
    public void onRegisterSuccess() {
        hideLoadingDialog();
        registerLL.setVisibility(View.GONE);
        verifyLL.setVisibility(View.VISIBLE);
        String content = String.format(getString(R.string.receive_email_code), userEmailEt.getText().toString());
        emailCodeTv.setText(content);
    }

    @Override
    public void onVerifySuccess() {
        hideLoadingDialog();
        hintCommonDialog.setContent(getString(R.string.register_success));
        hintCommonDialog.setOkName(getString(R.string.goto_login));
        hintCommonDialog.setOnCommitListener((dialog, confirm) -> {
            ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_MAIN, Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        });
        hintCommonDialog.show();
    }
}
