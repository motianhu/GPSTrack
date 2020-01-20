package com.smona.gpstrack.common;

import android.content.res.Configuration;
import android.os.Bundle;

import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.base.ui.mvp.BasePresenter;
import com.smona.base.ui.mvp.IView;
import com.smona.gpstrack.util.CommonUtils;

import java.util.Locale;

public abstract class BaseLanuagePresenterActivity <P extends BasePresenter<V>, V extends IView> extends BasePresenterActivity<P, V> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLanguage();
    }

    private void setLanguage() {
        String language = CommonUtils.getSysLanuage();
        Locale locale = Locale.ENGLISH;
        if(ParamConstant.LOCALE_ZH_CN.equals(language)) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else  if(ParamConstant.LOCALE_ZH_TW.equals(language)) {
            locale = Locale.TAIWAN;
        }
        setAppLanguage(locale);
    }
}
