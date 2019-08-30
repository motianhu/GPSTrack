package com.smona.image.loader;

import android.widget.ImageView;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 3/6/19 5:30 PM
 */
public interface IImageLoader {
    /**
     * 直接加载图片，不做任何处理
     * @param uri
     * @param view
     * @param drawableResId
     */
    void showImage(String uri, ImageView view, int drawableResId);

    /**
     * 四角都是圆角
     * @param uri
     * @param view
     * @param radius
     * @param drawableResId
     */
    void showCornerImage(String uri, ImageView view, int radius, int drawableResId);

    /**
     * 根据因子设置圆角形状
     * @param uri
     * @param view
     * @param radius
     * @param rxFactor
     * @param ryFactor
     * @param drawableResId
     */
    void showCornerImage(String uri, ImageView view, int radius, int rxFactor, int ryFactor, int drawableResId);

    /**
     * 顶部圆角
     * @param uri
     * @param view
     * @param radius
     * @param drawableResId
     */
    void showTopCornerImage(String uri, ImageView view, int radius, int drawableResId);

    /**
     * 底部圆角
     * @param uri
     * @param view
     * @param radius
     * @param drawableResId
     */
    void showBottomCornerImage(String uri, ImageView view, int radius, int drawableResId);
}