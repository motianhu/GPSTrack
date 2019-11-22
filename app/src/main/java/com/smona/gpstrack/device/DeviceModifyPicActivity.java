package com.smona.gpstrack.device;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BaseUiActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.adapter.AvatarAdapter;
import com.smona.gpstrack.device.bean.AvatarItem;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ActivityUtils;
import com.smona.gpstrack.util.BitmapUtils;
import com.smona.gpstrack.util.SPUtils;
import com.smona.logger.Logger;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.PATH_TO_DEVICE_PIC_MODIFY)
public class DeviceModifyPicActivity extends BaseUiActivity {

    private final static String TAG = DeviceAddActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private AvatarAdapter avatarAdapter;
    private List<AvatarItem> iconList;

    private String deviceNo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_pic_modify;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        initSerilized();
        initHeader();
        initViews();
    }

    private void initSerilized() {
        deviceNo = getIntent().getStringExtra(ARouterPath.PATH_TO_DEVICE_PIC_MODIFY);
        if (TextUtils.isEmpty(deviceNo)) {
            finish();
        }
    }

    private void initHeader() {
        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.modify_device_pic);
        findViewById(R.id.back).setOnClickListener(v -> finish());

        TextView saveTv = findViewById(R.id.rightTv);
        saveTv.setText(R.string.geo_save);
        saveTv.setVisibility(View.VISIBLE);
        saveTv.setOnClickListener(v -> saveModify());
    }

    private void initViews() {
        recyclerView = findViewById(R.id.iconList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        initAvator();

        avatarAdapter = new AvatarAdapter(R.layout.adapter_item_avatar, this);
        avatarAdapter.setNewData(iconList);
        recyclerView.setAdapter(avatarAdapter);
    }

    private void initAvator() {
        iconList = new ArrayList<>();
        AvatarItem item;
        int resId = -1;
        String path = (String) SPUtils.get(deviceNo, "");
        if (path.startsWith("avatar")) {
            resId = AvatarItem.getResId(path);
        }

        item = new AvatarItem();
        item.setResId(R.drawable.avator_0);
        item.setSelcted(resId == R.drawable.avator_0);
        iconList.add(item);

        item = new AvatarItem();
        item.setResId(R.drawable.avator_1);
        item.setSelcted(resId == R.drawable.avator_1);
        iconList.add(item);

        item = new AvatarItem();
        item.setResId(R.drawable.avator_2);
        item.setSelcted(resId == R.drawable.avator_2);
        iconList.add(item);

        item = new AvatarItem();
        item.setResId(R.drawable.avator_3);
        item.setSelcted(resId == R.drawable.avator_3);
        iconList.add(item);

        item = new AvatarItem();
        item.setResId(R.drawable.avator_4);
        item.setSelcted(resId == R.drawable.avator_4);
        iconList.add(item);


        item = new AvatarItem();
        item.setSelcted(resId == -1);
        item.setUrl(resId == -1 ? path:null);
        iconList.add(item);
    }

    private void saveModify() {
        AvatarItem.saveDevicePic(deviceNo, iconList);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
