package com.smona.base.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

import com.smona.base.ui.R;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String ACTION_BASE_ACTIVITY = "com.smona.activity.base.action";
    public static final String ACTION_BASE_ACTIVITY_EXIT_KEY = "action.exit";
    public static final int ACTION_BASE_ACTIVITY_EXIT_VALUE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lightStatusBar(enableLightStatusBar());
        setStatusBar(R.color.color_64B8D7);
        IntentFilter intentFilter = new IntentFilter(ACTION_BASE_ACTIVITY);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBaseReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBaseReceiver);
    }

    protected boolean enableLightStatusBar() {
        return true;
    }

    /**
     * 亮色调状态栏
     */
    private void lightStatusBar(boolean light) {
        if (light && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            int flags = decorView.getSystemUiVisibility();
            decorView.setSystemUiVisibility(flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private BroadcastReceiver mBaseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            processLocalBroadcast(intent);
        }
    };

    public void processLocalBroadcast(Intent data) {
        if (data != null) {
            int sign = data.getIntExtra(ACTION_BASE_ACTIVITY_EXIT_KEY, -1);
            if (sign == ACTION_BASE_ACTIVITY_EXIT_VALUE) {
                finish();
            }
        }
    }

    /**
     * 沉浸式效果
     */
    protected void immerse() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0);
        }
        View decorView = getWindow().getDecorView();
        int flags = decorView.getSystemUiVisibility();
        decorView.setSystemUiVisibility(flags | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    protected void setStatusBar(int resId) {
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(resId));
    }

    protected void setAppLanguage(Locale locale) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, metrics);
    }
}
