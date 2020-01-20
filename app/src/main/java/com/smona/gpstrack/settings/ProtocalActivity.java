package com.smona.gpstrack.settings;

import android.util.TypedValue;
import android.webkit.WebView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.BaseLanuagePresenterActivity;
import com.smona.gpstrack.common.ParamConstant;
import com.smona.gpstrack.common.param.ConfigCenter;
import com.smona.gpstrack.settings.presenter.ProtocalPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.http.wrapper.ErrorInfo;

/**
 * 协议页
 */
@Route(path = ARouterPath.PATH_TO_SETTING_PROTOCAL)
public class ProtocalActivity extends BaseLanuagePresenterActivity<ProtocalPresenter, ProtocalPresenter.IProtocalView> implements ProtocalPresenter.IProtocalView {

    private WebView webView;

    @Override
    protected ProtocalPresenter initPresenter() {
        return new ProtocalPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_protocal;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        TextView textView = findViewById(R.id.title);
        if(ParamConstant.LOCALE_EN.equalsIgnoreCase(ConfigCenter.getInstance().getConfigInfo().getLocale())) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        }
        textView.setText(R.string.view_protocal);
        findViewById(R.id.back).setOnClickListener(v -> finish());
        webView = findViewById(R.id.webview);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.requestTermCondition();
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        CommonUtils.showToastByFilter(errCode, errorInfo.getMessage());
    }

    @Override
    public void onSuccess(String json) {
        webView.loadData(json, "application/json",  "utf-8");
    }
}
