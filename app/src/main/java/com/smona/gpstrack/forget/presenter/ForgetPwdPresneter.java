package com.smona.gpstrack.forget.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ConstParam;
import com.smona.gpstrack.common.IView;
import com.smona.gpstrack.common.bean.RespEmptyBean;
import com.smona.gpstrack.forget.bean.ForgetPwdBean;
import com.smona.gpstrack.forget.model.ForgetPwdModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/19/19 2:22 PM
 */
public class ForgetPwdPresneter extends BasePresenter<ForgetPwdPresneter.IForgetPwdView> {

    private ForgetPwdModel mModel = new ForgetPwdModel();

    public void sendEmail(String email) {
        ForgetPwdBean urlBean = new ForgetPwdBean();
        urlBean.setLocale(ConstParam.LOCALE_EN);
        urlBean.setEmail(email);
        mModel.sendEmail(urlBean, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if (mView != null) {
                    mView.onSuccess();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if (mView != null) {
                    mView.onError("sendEmail", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IForgetPwdView extends IView {
        void onSuccess();
    }
}