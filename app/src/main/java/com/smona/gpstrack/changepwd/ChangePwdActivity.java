package com.smona.gpstrack.changepwd;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.gpstrack.R;
import com.smona.gpstrack.changepwd.presenter.ChangePwdPreseneter;
import com.smona.gpstrack.common.BaseLanuagePresenterActivity;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 11:43 AM
 */
@Route(path = ARouterPath.PATH_TO_SETTING_UPDATE_PWD)
public class ChangePwdActivity extends BaseLanuagePresenterActivity<ChangePwdPreseneter, ChangePwdPreseneter.IChangePwdView> implements ChangePwdPreseneter.IChangePwdView {

    private EditText sourceTv;
    private EditText pwdTv;
    private EditText confirmTv;

    @Override
    protected ChangePwdPreseneter initPresenter() {
        return new ChangePwdPreseneter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_changepwd;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        findViewById(R.id.back).setOnClickListener(view -> finish());
        TextView titleTv = findViewById(R.id.title);
        titleTv.setText(R.string.password);

        sourceTv = findViewById(R.id.source_pwd);
        CommonUtils.setMaxLenght(sourceTv, CommonUtils.MAX_PWD_LENGHT);
        CommonUtils.disableEditTextCopy(sourceTv);
        pwdTv = findViewById(R.id.new_pwd);
        CommonUtils.disableEditTextCopy(pwdTv);
        CommonUtils.setMaxLenght(pwdTv, CommonUtils.MAX_PWD_LENGHT);
        confirmTv = findViewById(R.id.confirm_password);
        CommonUtils.disableEditTextCopy(confirmTv);
        CommonUtils.setMaxLenght(confirmTv, CommonUtils.MAX_PWD_LENGHT);

        findViewById(R.id.confirm_update).setOnClickListener(v -> clickConfirm());
    }

    private void clickConfirm() {
        String sourcePwd = sourceTv.getText().toString();
        if (TextUtils.isEmpty(sourcePwd)) {
            showShort(R.string.empty_source_pwd);
            return;
        }
        if (sourcePwd.length() < 8) {
            showShort(R.string.no_than_s_pwd);
            return;
        }
        String newPwd = pwdTv.getText().toString();
        if (TextUtils.isEmpty(newPwd)) {
            showShort(R.string.empty_new_pwd);
            return;
        }
        if (newPwd.length() < 8 ) {
            showShort(R.string.no_than_n_pwd);
            return;
        }

        String confirmPwd = confirmTv.getText().toString();
        if (TextUtils.isEmpty(confirmPwd)) {
            showShort(R.string.empty_confirm_pwd);
            return;
        }
        if(confirmPwd.length() < 8) {
            showShort(R.string.no_than_c_pwd);
            return;
        }

        if (!newPwd.equals(confirmPwd)) {
            showShort(R.string.diff_new_confirm);
            return;
        }

        showLoadingDialog();
        mPresenter.changePwd(sourcePwd, newPwd);
    }

    @Override
    public void onSuccess() {
        hideLoadingDialog();
        finish();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        CommonUtils.showToastByFilter(errCode, errorInfo.getMessage());
    }
}
