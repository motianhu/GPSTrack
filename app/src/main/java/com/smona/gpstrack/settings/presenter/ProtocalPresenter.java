package com.smona.gpstrack.settings.presenter;

import com.smona.base.ui.mvp.BasePresenter;
import com.smona.gpstrack.common.ICommonView;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.bean.req.UrlBean;
import com.smona.gpstrack.settings.model.ProtocalModel;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.http.wrapper.OnResultListener;

public class ProtocalPresenter extends BasePresenter<ProtocalPresenter.IProtocalView> {

    private ProtocalModel model = new ProtocalModel();

    public void requestTermCondition() {
        UrlBean urlBean = new UrlBean();
        urlBean.setLocale(ParamConstant.LOCALE_EN);
        model.requestTermCondition(urlBean, new OnResultListener<String>() {
            @Override
            public void onSuccess(String s) {
                if(mView != null) {
                    mView.onSuccess(s);
                }
            }

            @Override
            public void onError(int stateCode, ErrorInfo errorInfo) {
                if(mView != null) {
                    mView.onError("", stateCode, errorInfo);
                }
            }
        });
    }


    public interface IProtocalView extends ICommonView {
        void onSuccess(String json);
    }
}
