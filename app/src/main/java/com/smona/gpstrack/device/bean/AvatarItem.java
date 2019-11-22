package com.smona.gpstrack.device.bean;

import android.text.TextUtils;
import android.widget.ImageView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.util.SPUtils;
import com.smona.image.loader.ImageLoaderDelegate;

import java.util.List;

public class AvatarItem {

    private static final int AVATAR_0 = R.drawable.avator_0;
    private static final int AVATAR_1 = R.drawable.avator_1;
    private static final int AVATAR_2 = R.drawable.avator_2;
    private static final int AVATAR_3 = R.drawable.avator_3;
    private static final int AVATAR_4 = R.drawable.avator_4;

    public static int getResId(String path) {
        if("avatar_0".equals(path)) {
            return AVATAR_0;
        } else if("avatar_1".equals(path)) {
            return AVATAR_1;
        } else if("avatar_2".equals(path)) {
            return AVATAR_2;
        } else if("avatar_3".equals(path)) {
            return AVATAR_3;
        } else if("avatar_4".equals(path)) {
            return AVATAR_4;
        } else {
            return AVATAR_0;
        }
    }

    private int resId;
    private String url;
    private boolean isSelcted;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isSelcted() {
        return isSelcted;
    }

    public void setSelcted(boolean selcted) {
        isSelcted = selcted;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public static void showDeviceIcon(String deviceNo, ImageView deviceIcon) {
        String path = (String) SPUtils.get(deviceNo, "avatar_0");
        if(TextUtils.isEmpty(path)) {
            deviceIcon.setImageResource(R.drawable.avator_0);
        } else if(path.startsWith("avatar")) {
            int resId = AvatarItem.getResId(path);
            deviceIcon.setImageResource(resId);
        } else {
            ImageLoaderDelegate.getInstance().showCornerImage(path, deviceIcon, deviceIcon.getContext().getResources().getDimensionPixelSize(R.dimen.dimen_10dp), R.drawable.avator_0);
        }
    }

    public static void saveDevicePic(String deviceNo, List<AvatarItem> iconList) {
        AvatarItem item;
        for (int i = 0; i < iconList.size(); i++) {
            item = iconList.get(i);
            if (item.isSelcted()) {
                saveIconPath(i,deviceNo, item);
                break;
            }
        }
    }

    private static void saveIconPath(int pos, String deivceId, AvatarItem item) {
        if (TextUtils.isEmpty(item.getUrl())) {
            SPUtils.put(deivceId, "avatar_" + pos);
        } else {
            SPUtils.put(deivceId, item.getUrl());
        }
    }
}
