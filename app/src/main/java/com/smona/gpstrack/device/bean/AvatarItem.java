package com.smona.gpstrack.device.bean;

import android.widget.ImageView;

import com.smona.gpstrack.R;
import com.smona.gpstrack.util.SPUtils;
import com.smona.image.loader.ImageLoaderDelegate;

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


    public static void showDeviceIcon(String deviceId, ImageView deviceIcon) {
        String path = (String) SPUtils.get(deviceId, "avatar_0");
        if(path.startsWith("avatar")) {
            int resId = AvatarItem.getResId(path);
            deviceIcon.setImageResource(resId);
        } else {
            ImageLoaderDelegate.getInstance().showImage(path, deviceIcon, 0);
        }
    }
}
