package com.smona.gpstrack.settings;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.BaseLanuageActivity;
import com.smona.gpstrack.util.ARouterPath;

/**
 * description:
 * 关于页面
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/16/19 2:57 PM
 */
@Route(path = ARouterPath.PATH_TO_ABOUT)
public class AboutActivity extends BaseLanuageActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.aboutUs);
        findViewById(R.id.back).setOnClickListener(v -> finish());
        TextView appversion = findViewById(R.id.versionTv);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String pack = getString(R.string.version) + packageInfo.versionName + "(" + packageInfo.versionCode + ")";
            appversion.setText(pack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView emailTv = findViewById(R.id.contactEmail);
        String email = getString(R.string.cnotact_email) + "team@pingo.com.hk";
        emailTv.setText(email);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
