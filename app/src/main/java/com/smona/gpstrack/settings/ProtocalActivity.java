package com.smona.gpstrack.settings;

import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.settings.presenter.ProtocalPresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.http.wrapper.ErrorInfo;

@Route(path = ARouterPath.PATH_TO_SETTING_PROTOCAL)
public class ProtocalActivity extends BasePresenterActivity<ProtocalPresenter, ProtocalPresenter.IProtocalView> implements ProtocalPresenter.IProtocalView {


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
        textView.setText(R.string.view_protocal);
        findViewById(R.id.back).setOnClickListener(v -> finish());
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
