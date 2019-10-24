package com.smona.gpstrack;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BaseActivity;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ActivityUtils;
import com.smona.zxing.common.zxing.view.QRCodeView;
import com.smona.zxing.common.zxing.view.ZXingView;

@Route(path = ARouterPath.PATH_TO_SCAN)
public class GpsScanActivity extends BaseActivity implements QRCodeView.Delegate {

    private ZXingView mZXingView;
    private View mFlashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.scan_add_device);
        findViewById(R.id.back).setOnClickListener(v -> finish());
        initViews();
    }

    private void initViews() {
        mZXingView = findViewById(R.id.zxing_view);
        mZXingView.setDelegate(this);

        mFlashView = findViewById(R.id.flash_light_iv);
        mFlashView.setSelected(false);
        mFlashView.setOnClickListener(v -> {
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Toast.makeText(this, R.string.qr_num_light_tip, Toast.LENGTH_SHORT).show();
            } else {
                toggleFlashLight();
            }
        });
    }


    @Override
    public boolean enableLightStatusBar() {
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopZxing();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tryOpenCamera();
    }

    private void stopZxing() {
        mFlashView.setSelected(false);
        if (mZXingView != null) {
            mZXingView.stopCamera();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyZxing();
    }

    private void destroyZxing() {
        if (mZXingView != null) {
            mZXingView.onDestroy();
        }
    }

    private void tryOpenCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //ContextCompat.checkSelfPermission() 方法
            // 指定context和某个权限 返回PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 若不为GRANTED(即为DENIED)则要申请权限了
                // 申请权限 第一个为context 第二个可以指定多个请求的权限
                // 第三个参数为请求码
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.CAMERA}, ActivityUtils.ACTION_SCAN);
            } else {
                //权限已经被授予，在这里直接写要执行的相应方法即可
                startZxing();
            }
        } else {
            // 低于6.0的手机直接访问
            startZxing();
        }
    }

    // 用户权限 申请 的回调方法
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ActivityUtils.ACTION_SCAN) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startZxing();
            } else {
                Toast.makeText(this, R.string.authorization_is_prohibited, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void startZxing() {
        if (mZXingView != null) {
            mZXingView.startCamera();
            mZXingView.startSpotAndShowRect();
        }
    }

    private void toggleFlashLight() {
        final boolean isOpened = mZXingView.changedFlashLight();
        mFlashView.setSelected(isOpened);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        mZXingView.stopSpot();

        Intent intent = new Intent();
        intent.putExtra(ARouterPath.PATH_TO_SCAN, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, R.string.qr_scanner_error_open, Toast.LENGTH_SHORT).show();
        finish();
    }
}
