package com.smona.gpstrack.changepwd.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.changepwd.model.ChangePwdModel;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.bean.resp.RespEmptyBean;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/20/19 11:43 AM
 */
public class ChangePwdPreseneter extends BasePresenter<ChangePwdPreseneter.IChangePwdView> {

    private ChangePwdModel mModel = new ChangePwdModel();

    public void changePwd() {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ParamConstant.LOCALE_EN);
        mModel.changePwd(urlBean, new OnResultListener<RespEmptyBean>() {
            @Override
            public void onSuccess(RespEmptyBean respEmptyBean) {
                if(mView != null) {
                    mView.onSuccess();
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if(mView != null) {
                    mView.onError("changePwd", stateCode, errorInfo);
                }
            }
        });
    }

    public interface IChangePwdView extends ICommonView {
        void onSuccess();
    }
}
