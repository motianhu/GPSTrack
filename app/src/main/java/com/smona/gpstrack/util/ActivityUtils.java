package com.smona.gpstrack.util;

import android.app.Activity;
import android.content.Intent;

/**
 * 去图库的工具类
 */
public class ActivityUtils {

    public static final int ACTION_GALLERY = 100;
    public static final int ACTION_SCAN = 101;

    public static void gotoGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, ActivityUtils.ACTION_GALLERY);
    }
}
