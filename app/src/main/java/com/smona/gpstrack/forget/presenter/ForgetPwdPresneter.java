package com.smona.gpstrack.forget.presenter;

import android.text.TextUtils;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.common.param.ConfigInfo;
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

    public void sendEmail(String curSysLa, String email) {
        ForgetPwdBean urlBean = new ForgetPwdBean();
        ConfigInfo configInfo = ConfigCenter.getInstance().getConfigInfo();
        String language = configInfo != null ? configInfo.getLocale() : "";
        if (TextUtils.isEmpty(language)) {
            Integer value = ParamConstant.LANUAGEMAP.get(curSysLa);
            if (value == null || value == 0) {
                language = ParamConstant.LOCALE_EN;
            } else {
                language = curSysLa;
            }
        }
        urlBean.setLocale(language);
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

    public interface IForgetPwdView extends ICommonView {
        void onSuccess();
    }
}
