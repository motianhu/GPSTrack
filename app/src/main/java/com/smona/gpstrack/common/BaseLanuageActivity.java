package com.smona.gpstrack.common;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.util.CommonUtils;

import java.util.Locale;

public abstract class BaseLanuageActivity extends BaseActivity {

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

    public void showShort(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}
