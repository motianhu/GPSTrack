package com.smona.base.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.smona.base.ui.R;

public class AppDialog extends Dialog {

    private AppDialog(Context context, @LayoutRes int layoutRes, int width, int height) {
        this(context, layoutRes, width, height, Gravity.CENTER, context.getResources().getDimensionPixelSize(R.dimen.dimen_50dp));
    }

    private AppDialog(Context context, @LayoutRes int layoutRes, int width, int height, int gravity, int y) {
        super(context, R.style.AppDialog2);
        // set content
        setContentView(layoutRes);

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            if (width > 0) {
                params.width = width;
            } else {
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            if (height > 0) {
                params.height = height;
            } else {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            params.gravity = gravity;
            params.y = y;

            window.setAttributes(params);
        }
    }

    public void showLoadingDialog() {
        setContentView(R.layout.dialog_loading2);
        startLoadingAnim();
        show();
    }


    /**
     * 等待dialog2
     */
    public static AppDialog loadingCreate(Context context, String text) {
        final AppDialog dialog = new AppDialog(context, R.layout.dialog_loading2, 0, 0);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.sv_loading_bg);
        if (!TextUtils.isEmpty(text)) {
            TextView tv = dialog.findViewById(R.id.tv_loading);
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
        return dialog;
    }

    private void startLoadingAnim() {
        ImageView ivRotate = findViewById(R.id.ivRotate);
        if (ivRotate != null) {
            Animation rotateAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate_anim);
            ivRotate.setAnimation(rotateAnimation);
            ivRotate.startAnimation(rotateAnimation);
        }
    }
}
