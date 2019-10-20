package com.smona.gpstrack.settings;

import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.settings.presenter.LanuagePresenter;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.http.wrapper.ErrorInfo;

@Route(path = ARouterPath.PATH_TO_SETTING_LANUAGE)
public class SettingLanuageActivity extends BasePresenterActivity<LanuagePresenter, LanuagePresenter.ILanuageView> implements LanuagePresenter.ILanuageView {

    private ImageView jiantiIv;
    private ImageView fantiTv;
    private ImageView englishTv;

    @Override
    protected LanuagePresenter initPresenter() {
        return new LanuagePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_lanuage;
    }

    @Override
    protected void initContentView() {
        super.initContentView();

        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.switchLanguage);
        findViewById(R.id.back).setOnClickListener(v-> finish());

        findViewById(R.id.jianti).setOnClickListener(v -> clickJianti());
        findViewById(R.id.fanti).setOnClickListener(v -> clickFanti());
        findViewById(R.id.english).setOnClickListener(v -> clickEnglish());

        jiantiIv = findViewById(R.id.selectJianti);
        fantiTv = findViewById(R.id.selectFanti);
        englishTv = findViewById(R.id.selectEnglish);
    }

    private void clickJianti() {
        mPresenter.switchLanuage();
    }

    private void clickFanti() {
        mPresenter.switchLanuage();
    }

    private void clickEnglish() {
        mPresenter.switchLanuage();

    }

    @Override
    public void onSwitchLanuage() {

    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {

    }
}
