package com.smona.gpstrack.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.util.ARouterPath;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/16/19 2:57 PM
 */
@Route(path = ARouterPath.PATH_TO_ABOUT)
public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.aboutUs);
        findViewById(R.id.back).setOnClickListener(v-> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
