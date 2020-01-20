package com.smona.gpstrack.device;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BasePresenterActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.common.BaseLanuagePresenterActivity;
import com.smona.gpstrack.device.adapter.AvatarAdapter;
import com.smona.gpstrack.device.bean.AvatarItem;
import com.smona.gpstrack.device.presenter.DeviceAddPresenter;
import com.smona.gpstrack.util.ARouterManager;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ActivityUtils;
import com.smona.gpstrack.util.BitmapUtils;
import com.smona.gpstrack.util.CommonUtils;
import com.smona.gpstrack.util.ToastUtil;
import com.smona.http.wrapper.ErrorInfo;
import com.smona.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备添加
 */
@Route(path = ARouterPath.PATH_TO_ADD_DEVICE)
public class DeviceAddActivity extends BaseLanuagePresenterActivity<DeviceAddPresenter, DeviceAddPresenter.IDeviceAddView> implements DeviceAddPresenter.IDeviceAddView {

    private final static String TAG = DeviceAddActivity.class.getSimpleName();

    private EditText deviceNameEt;
    private EditText deviceNoEt;
    private EditText deviceOrderNoEt;

    private RecyclerView recyclerView;
    private AvatarAdapter avatarAdapter;
    private List<AvatarItem> iconList;

    private ImageView selectedIv;

    @Override
    protected DeviceAddPresenter initPresenter() {
        return new DeviceAddPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_add;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        initHeader();
        initViews();
    }

    private void initHeader() {
        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.add_device);
        findViewById(R.id.back).setOnClickListener(v -> finish());

        ImageView rightIv = findViewById(R.id.rightIv);
        rightIv.setVisibility(View.VISIBLE);
        rightIv.setImageResource(R.drawable.scan);
        rightIv.setOnClickListener(v -> clickScan());
    }

    private void initViews() {
        selectedIv = findViewById(R.id.selectedIv);
        selectedIv.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.iconList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        initAvator();

        avatarAdapter = new AvatarAdapter(R.layout.adapter_item_avatar, this);
        avatarAdapter.setNewData(iconList);
        recyclerView.setAdapter(avatarAdapter);

        deviceNoEt = findViewById(R.id.device_id);
        deviceNameEt = findViewById(R.id.device_name);
        CommonUtils.setMaxLenght(deviceNameEt, CommonUtils.MAX_NAME_LENGHT);
        deviceOrderNoEt = findViewById(R.id.device_order_no);
        findViewById(R.id.device_add).setOnClickListener(v -> clickAddDevice());
    }

    private void initAvator() {
        iconList = new ArrayList<>();
        AvatarItem item;

        item = new AvatarItem();
        item.setResId(R.drawable.avator_0);
        item.setSelcted(true);
        iconList.add(item);

        item = new AvatarItem();
        item.setResId(R.drawable.avator_1);
        iconList.add(item);

        item = new AvatarItem();
        item.setResId(R.drawable.avator_2);
        iconList.add(item);

        item = new AvatarItem();
        item.setResId(R.drawable.avator_3);
        iconList.add(item);

        item = new AvatarItem();
        item.setResId(R.drawable.avator_4);
        iconList.add(item);

        iconList.add(new AvatarItem());
    }


    @Override
    protected void initData() {
        super.initData();
    }

    private void clickAddDevice() {
        String deviceId = deviceNoEt.getText().toString();
        String deviceName = deviceNameEt.getText().toString();
        String deviceOrderNo = deviceOrderNoEt.getText().toString();

        if (TextUtils.isEmpty(deviceId)) {
            ToastUtil.showShort(R.string.toast_device_id_empty);
            return;
        } else if (TextUtils.isEmpty(deviceName)) {
            ToastUtil.showShort(R.string.toast_device_name_empty);
            return;
        } else if (TextUtils.isEmpty(deviceOrderNo)) {
            ToastUtil.showShort(R.string.toast_device_orderno_empty);
            return;
        }
        showLoadingDialog();
        mPresenter.addDevice(deviceId, deviceName, deviceOrderNo);
    }

    private void clickScan() {
        ARouterManager.getInstance().gotoActivityForResult(ARouterPath.PATH_TO_SCAN, this, ActivityUtils.ACTION_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityUtils.ACTION_SCAN && resultCode == RESULT_OK) {
            String deviceid = data.getStringExtra(ARouterPath.PATH_TO_SCAN);
            deviceNoEt.setText(deviceid);
            deviceNoEt.setEnabled(false);
        } else {
            if (requestCode == ActivityUtils.ACTION_GALLERY && resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String imagePath = BitmapUtils.getRealPathFromURI(this, selectedImage);
                Logger.d(TAG, "onActivityResult imagePath: " + imagePath);
                for(AvatarItem item: iconList) {
                    item.setSelcted(false);
                }
                iconList.get(iconList.size() - 1).setUrl(imagePath);
                iconList.get(iconList.size() - 1).setSelcted(true);
                avatarAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(String api, int errCode, ErrorInfo errorInfo) {
        hideLoadingDialog();
        CommonUtils.showToastByFilter(errCode, errorInfo.getMessage());
    }

    @Override
    public void onSuccess() {
        hideLoadingDialog();
        AvatarItem.saveDevicePic(deviceNoEt.getText().toString(), iconList);
        finish();
    }
}
