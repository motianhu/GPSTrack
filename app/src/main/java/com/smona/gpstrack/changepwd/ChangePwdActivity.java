package com.smona.gpstrack.changepwd;

import android.text.TextUtils;
import android.widget.TextView;

import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.changepwd.presenter.ChangePwdPreseneter;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 11:43 AM
 */
public class ChangePwdActivity extends BasePresenterActivity<ChangePwdPreseneter, ChangePwdPreseneter.IChangePwdView> implements ChangePwdPreseneter.IChangePwdView {

    private TextView sourceTv;
    private TextView pwdTv;
    private TextView confirmTv;

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
        titleTv.setText(R.string.update_pwd);

        sourceTv = findViewById(R.id.source_pwd);
        pwdTv = findViewById(R.id.new_pwd);
        confirmTv = findViewById(R.id.confirm_password);

        findViewById(R.id.confirm_update).setOnClickListener(v -> clickConfirm());
    }

    private void clickConfirm() {
        String sourcePwd = sourceTv.getText().toString();
        if (TextUtils.isEmpty(sourcePwd)) {
            ToastUtil.showShort(R.string.empty_source_pwd);
            return;
        }
        String newPwd = pwdTv.getText().toString();
        if (TextUtils.isEmpty(newPwd)) {
            ToastUtil.showShort(R.string.empty_new_pwd);
            return;
        }
        String confirmPwd = confirmTv.getText().toString();
        if (TextUtils.isEmpty(confirmPwd)) {
            ToastUtil.showShort(R.string.empty_confirm_pwd);
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            ToastUtil.showShort(R.string.diff_new_confirm);
            return;
        }
        showLoadingDialog();
        mPresenter.changePwd(sourcePwd, newPwd);
    }

    @Override
    public void onSuccess() {
        hideLoadingDialog();
        ToastUtil.showShort(R.string.success_update_pwd);
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        ToastUtil.showShort(errorInfo.getMessage());
    }
}
