package com.smona.gpstrack.forget;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.dialog.HintCommonDialog;
import com.smona.gpstrack.forget.presenter.ForgetPwdPresneter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/9/19 2:29 PM
 */

@Route(path = ARouterPath.PATH_TO_FORGETPWD)
public class ForgetPwdActivity extends BasePresenterActivity<ForgetPwdPresneter, ForgetPwdPresneter.IForgetPwdView> implements ForgetPwdPresneter.IForgetPwdView {

    private HintCommonDialog hintCommonDialog;

    @Override
    protected ForgetPwdPresneter initPresenter() {
        return new ForgetPwdPresneter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgetpwd;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        setStatusBar(R.color.color_64B8D7);
        findViewById(R.id.back).setOnClickListener(view -> finish());
        TextView titleTv = findViewById(R.id.title);
        titleTv.setText(R.string.forget_password);

        EditText editText = findViewById(R.id.et_input_email);
        findViewById(R.id.bt_send_email).setOnClickListener(view -> clickSend(editText.getText().toString()));

        hintCommonDialog = new HintCommonDialog(this);
    }

    private void clickSend(String email) {
        if (TextUtils.isEmpty(email)) {
            ToastUtil.showShort(R.string.empty_email);
            return;
        }
        if (!CommonUtils.isEmail(email)) {
            ToastUtil.showShort(R.string.invalid_email);
            return;
        }
        showLoadingDialog();
        mPresenter.sendEmail(email);
    }

    @Override
    public void onSuccess() {
        hideLoadingDialog();
        hintCommonDialog.setContent(getString(R.string.send_success));
        hintCommonDialog.setOnCommitListener((dialog, confirm) -> {
            dialog.dismiss();
            ARouterManager.getInstance().gotoActivity(ARouterPath.PATH_TO_LOGIN);
            supportFinishAfterTransition();
        });
        hintCommonDialog.show();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        ToastUtil.showShort(errorInfo.getMessage());
    }
}
