package com.smona.image.loader;

import android.widget.ImageView;

import com.smona.image.loader.glide.GlideImageLoader;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 3/6/19 5:30 PM
 */
public class ImageLoaderDelegate implements IImageLoader {

    private IImageLoader mImageLoader;

    private ImageLoaderDelegate() {
        mImageLoader = new GlideImageLoader();
    }

    private static class ImageLoaderHolder {
        private static IImageLoader sImageLoader = new ImageLoaderDelegate();
    }

    public static IImageLoader getInstance() {
        return ImageLoaderHolder.sImageLoader;
    }

    @Override
    public void showImage(String uri, ImageView view, int drawableResId) {
        mImageLoader.showImage(uri, view, drawableResId);
    }

    @Override
    public void showCornerImage(String uri, ImageView view, int radius,  int drawableResId) {
        mImageLoader.showCornerImage(uri, view, radius, drawableResId);
    }

    @Override
    public void showCornerImage(String uri, ImageView view, int radius, int rxFactor, int ryFactor, int drawableResId) {
        mImageLoader.showCornerImage(uri, view, radius, rxFactor, ryFactor, drawableResId);
    }

    @Override
    public void showTopCornerImage(String uri, ImageView view, int radius, int drawableResId) {
        mImageLoader.showTopCornerImage(uri, view, radius, drawableResId);
    }

    @Override
    public void showBottomCornerImage(String uri, ImageView view, int radius, int drawableResId) {
        mImageLoader.showBottomCornerImage(uri, view, radius, drawableResId);
    }


}
