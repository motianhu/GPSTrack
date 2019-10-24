package com.smona.image.loader.glide;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smona.image.loader.IImageLoader;
import com.smona.image.loader.glide.options.GlideCornersTransform;

import java.io.File;

public class GlideImageLoader implements IImageLoader {

    /**
     * 支持显示本地、网络
     */
    public void showImage(String uri, ImageView view, int drawableResId) {
        if (uri == null) return;
        GlideRequest<Drawable> glideRequest;
        File file = new File(uri);
        if (file.exists()) {
            glideRequest = GlideApp.with(view).load(file);
        } else {
            glideRequest = GlideApp.with(view).load(uri);
        }
        if (drawableResId == 0) {
            glideRequest.into(view);
        } else {
            glideRequest.placeholder(drawableResId).error(drawableResId).into(view);
        }
    }


    /**
     * 四角圆角；radius和ImageView大小一样就是圆
     *
     * @param uri
     * @param view
     * @param drawableResId
     */
    public void showCornerImage(String uri, ImageView view, int radius, int drawableResId) {
        if (uri == null) return;
        GlideRequest<Drawable> glideRequest;
        File file = new File(uri);
        if (file.exists()) {
            glideRequest = GlideApp.with(view).load(file);
        } else {
            glideRequest = GlideApp.with(view).load(uri).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        }
        if (drawableResId == 0) {
            glideRequest.circleCrop().transform(new GlideCornersTransform(radius).scaleType(view.getScaleType())).into(view);
        } else {
            glideRequest.placeholder(drawableResId).circleCrop().error(drawableResId)
                    .transform(new GlideCornersTransform(radius).scaleType(view.getScaleType())).into(view);
        }
    }


    /**
     * @param uri
     * @param view
     * @param radius        circle radius
     * @param rxFactor      not zero
     * @param ryFactor      not zero
     * @param drawableResId default drawable
     */
    public void showCornerImage(String uri, ImageView view, int radius, int rxFactor, int ryFactor, int drawableResId) {
        if (uri == null) return;
        GlideRequest<Drawable> glideRequest;
        File file = new File(uri);
        if (file.exists()) {
            glideRequest = GlideApp.with(view).load(file);
        } else {
            glideRequest = GlideApp.with(view).load(uri).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        }
        if (drawableResId == 0) {
            glideRequest.circleCrop()
                    .transform(new GlideCornersTransform(radius, rxFactor, ryFactor, GlideCornersTransform.TYPE_ALL).scaleType(view.getScaleType())).into(view);
        } else {
            glideRequest.placeholder(drawableResId).circleCrop().error(drawableResId)
                    .transform(new GlideCornersTransform(radius, rxFactor, ryFactor, GlideCornersTransform.TYPE_ALL).scaleType(view.getScaleType())).into(view);
        }
    }

    /**
     * 顶部圆角
     *
     * @param uri
     * @param view
     * @param radius
     * @param drawableResId
     */
    @Override
    public void showTopCornerImage(String uri, ImageView view, int radius, int drawableResId) {
        if (uri == null) return;
        GlideRequest<Drawable> glideRequest;
        File file = new File(uri);
        if (file.exists()) {
            glideRequest = GlideApp.with(view).load(file);
        } else {
            glideRequest = GlideApp.with(view).load(uri).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        }
        if (drawableResId == 0) {
            glideRequest.circleCrop()
                    .transform(new GlideCornersTransform(radius, GlideCornersTransform.TYPE_TOP).scaleType(view.getScaleType())).into(view);
        } else {
            glideRequest.placeholder(drawableResId).circleCrop().error(drawableResId)
                    .transform(new GlideCornersTransform(radius, GlideCornersTransform.TYPE_TOP).scaleType(view.getScaleType())).into(view);
        }
    }

    /**
     * 底部圆角
     *
     * @param uri
     * @param view
     * @param radius
     * @param drawableResId
     */
    @Override
    public void showBottomCornerImage(String uri, ImageView view, int radius, int drawableResId) {
        if (uri == null) return;
        GlideRequest<Drawable> glideRequest;
        File file = new File(uri);
        if (file.exists()) {
            glideRequest = GlideApp.with(view).load(file);
        } else {
            glideRequest = GlideApp.with(view).load(uri).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        }
        if (drawableResId == 0) {
            glideRequest.circleCrop()
                    .transform(new GlideCornersTransform(radius, GlideCornersTransform.TYPE_BOTTOM).scaleType(view.getScaleType())).into(view);
        } else {
            glideRequest.placeholder(drawableResId).circleCrop().error(drawableResId)
                    .transform(new GlideCornersTransform(radius, GlideCornersTransform.TYPE_BOTTOM).scaleType(view.getScaleType())).into(view);
        }
    }
}
