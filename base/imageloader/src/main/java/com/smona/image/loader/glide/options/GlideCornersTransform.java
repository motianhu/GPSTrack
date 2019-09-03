package com.smona.image.loader.glide.options;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * description:
 *
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 5/10/19 3:06 PM
 */
public class GlideCornersTransform extends BitmapTransformation {

    private static final String ID = GlideCornersTransform.class.getName();

    // 圆角类型
    public static final int TYPE_ALL = 0;
    public static final int TYPE_TOP = 1;
    public static final int TYPE_BOTTOM = 2;

    private int rxFactor = 1;
    private int ryFactor = 1;

    private int radius = 0;
    private int diameter;
    private int mType;

    private ImageView.ScaleType mScaleType = ImageView.ScaleType.CENTER_CROP;

    public GlideCornersTransform(int radius) {
        this(radius, TYPE_ALL);
    }

    public GlideCornersTransform(int radius, int rxFactor, int ryFactor, int type) {
        super();
        this.radius = radius;
        this.diameter = radius * 2;
        this.rxFactor = rxFactor;
        this.ryFactor = ryFactor;
        mType = type;
    }

    public GlideCornersTransform(int radius, int type) {
        this(radius, 1, 1, type);
    }

    public GlideCornersTransform scaleType(ImageView.ScaleType mScaleType) {
        this.mScaleType = mScaleType;
        return this;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        if (source == null) {
            return null;
        }

        Bitmap result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        }

        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        float scale = 0;
        float dx = 0, dy = 0;
        float scaleWidth = (float) outWidth / sourceWidth;
        float scaleHeight = (float) outHeight / sourceHeight;

        // 先保证宽与控件一致
        if (scaleWidth * sourceHeight < outHeight) {
            if (scaleHeight * sourceWidth < outWidth) {
                scale += (outHeight - scaleWidth * sourceHeight)/outHeight;
                dx = (outWidth - sourceWidth * scale) * 0.5f;
            } else {
                scale = scaleHeight;
                dx = (outWidth - sourceWidth * scale) * 0.5f;
            }
        } else  {
            //缩放后高度足够填满控件
            scale = scaleWidth;
            dy = (outHeight - sourceHeight * scale) * 0.5f;
        }

        Matrix matrix = new Matrix();
        if (mScaleType == ImageView.ScaleType.CENTER_CROP) {
            //适配centerCrop
            matrix.setScale(scale, scale);
            matrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
        } else {
            //适配FitXY, TODO: 使用fitXY，图片一般会拉伸或压缩变形
            matrix.setScale(scaleWidth, scaleHeight);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        if (mType == TYPE_TOP) {
            // 顶部有圆角
            RectF roundRectF = new RectF(0, 0, outWidth, diameter);
            canvas.drawRoundRect(roundRectF, radius / rxFactor, radius / ryFactor, paint);

            RectF rectF = new RectF(0, radius, outWidth, outHeight);
            canvas.drawRect(rectF, paint);
        } else if (mType == TYPE_BOTTOM) {
            // 底部有圆角
            RectF roundRectF = new RectF(0, outHeight - diameter, outWidth, outHeight);
            canvas.drawRoundRect(roundRectF, radius / rxFactor, radius / ryFactor, paint);

            RectF rectF = new RectF(0, 0, outWidth, outHeight - radius);
            canvas.drawRect(rectF, paint);
        } else {
            // 默认四周都有圆角
            RectF rectF = new RectF(0f, 0f, outWidth, outHeight);
            canvas.drawRoundRect(rectF, radius / rxFactor, radius / ryFactor, paint);
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GlideCornersTransform) {
            return this == obj;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID.getBytes(CHARSET));
    }
}