package com.smona.gpstrack.device;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.smona.base.ui.activity.BaseUiActivity;
import com.smona.gpstrack.R;
import com.smona.gpstrack.device.adapter.AvatarAdapter;
import com.smona.gpstrack.device.bean.AvatarItem;
import com.smona.gpstrack.util.ARouterPath;
import com.smona.gpstrack.util.ActivityUtils;
import com.smona.gpstrack.util.BitmapUtils;
import com.smona.logger.Logger;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouterPath.PATH_TO_DEVICE_PIC_MODIFY)
public class DeviceModifyPicActivity extends BaseUiActivity {

    private final static String TAG = DeviceAddActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private AvatarAdapter avatarAdapter;
    private List<AvatarItem> iconList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_pic_modify;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        initHeader();
        initViews();
    }

    private void initHeader() {
        TextView textView = findViewById(R.id.title);
        textView.setText(R.string.modify_device_pic);
        findViewById(R.id.back).setOnClickListener(v -> finish());
    }

    private void initViews() {
        recyclerView = findViewById(R.id.iconList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        iconList = new ArrayList<>();
        AvatarItem item;
        for (int i = 0; i < 5; i++) {
            item = new AvatarItem();
            item.setResId(R.drawable.avatar);
            iconList.add(item);
        }
        iconList.add(new AvatarItem());

        avatarAdapter = new AvatarAdapter(R.layout.adapter_item_avatar, this);
        avatarAdapter.setNewData(iconList);
        recyclerView.setAdapter(avatarAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityUtils.ACTION_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String imagePath = BitmapUtils.getRealPathFromURI(this, selectedImage);
            Logger.d(TAG, "onActivityResult imagePath: " + imagePath);
            iconList.get(iconList.size() - 1).setUrl(imagePath);
            avatarAdapter.notifyItemChanged(iconList.size() - 1);
        }
    }
}
