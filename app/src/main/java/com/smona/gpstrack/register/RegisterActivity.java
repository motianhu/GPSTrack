package com.smona.gpstrack.register;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.register.presenter.RegisterPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.gpstrack.widget.PwdEditText;
import com.smona.http.wrapper.ErrorInfo;

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

        findViewById(R.id.btn_register).setOnClickListener(view -> clickRegister(userNameEt.getText().toString(), userEmailEt.getText().toString(), userPwdEt.getText().toString(), userConfirmPwdEt.getText().toString()));
        findViewById(R.id.btn_verify).setOnClickListener(view -> clickVerify(userEmailEt.getText().toString(), verifyEt.getText().toString()));
    }

    private void clickRegister(String userName, String email, String pwd, String cpwd) {
        showLoadingDialog();
        mPresenter.register(userName, email, pwd, cpwd);
    }

    private void clickVerify(String email, String code) {
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
    }

    @Override
    public void onVerifySuccess() {
        ToastUtil.showShort("verify success");
        hideLoadingDialog();
        supportFinishAfterTransition();
    }
}
