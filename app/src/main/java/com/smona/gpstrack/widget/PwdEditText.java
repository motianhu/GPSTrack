package com.smona.gpstrack.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.smona.gpstrack.R;

/**
 * description:
 * 密码框View
 * @author motianhu
 * @email motianhu@qq.com
 * created on: 9/11/19 1:44 PM
 */
public class PwdEditText extends android.support.v7.widget.AppCompatEditText {
    private int height = 40;//view的高度

    private int count = 6;//矩形数目

    private Paint borderPaint;//边框画笔

    private Paint fillPaint;//填充画笔


    private Paint focusBorderPaint;//焦点边框画笔

    private Paint focusFillPaint;//焦点填充画笔

    private Paint paintText;//文字画笔

    private Paint paintCircle;//圆形画笔

    private int startX;//开始坐标

    private int lineWidth = 1;//边框宽度
    private int lineColor = Color.WHITE; //边框颜色
    private int stokesColor = Color.WHITE;//填充颜色
    private int focusStokeColor = Color.WHITE;//焦点填充颜色
    private int focusLineColor = Color.WHITE;//焦点边框颜色

    private int textColor = Color.BLACK;//文字的颜色
    private int textSize = 64;//文字的大小

    private int position = 0;//当前输入的位置

    private boolean isDrawLine = false;//是否绘制边框，true绘制，false不绘制
    private int spaceWidth = 0;//边框宽度

    private boolean isDrawCircle = false;//是绘制圆还是文字；true绘制圆，false绘制文字
    private int circleRadius = 10;//如果不显示文字则绘制圆，此为圆半径
    private int circleColor = Color.WHITE;//如果不显示文字则绘制圆，此为圆填充色

    private OnFinishListener onFinishListener;

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public PwdEditText(Context context) {
        this(context, null);
    }

    public PwdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PwdEditText);
        if (typedArray != null) {
            height = typedArray.getDimensionPixelSize(R.styleable.PwdEditText_height, height);
            count = typedArray.getInt(R.styleable.PwdEditText_count, count);
            lineWidth = typedArray.getDimensionPixelSize(R.styleable.PwdEditText_lineWidth, lineWidth);
            lineColor = typedArray.getColor(R.styleable.PwdEditText_lineColor, lineColor);
            focusLineColor = typedArray.getColor(R.styleable.PwdEditText_focusLineColor, focusLineColor);
            focusStokeColor = typedArray.getColor(R.styleable.PwdEditText_focusStokeColor, focusStokeColor);
            stokesColor = typedArray.getColor(R.styleable.PwdEditText_stokesColor, stokesColor);
            textColor = typedArray.getColor(R.styleable.PwdEditText_textColor, textColor);
            spaceWidth = typedArray.getDimensionPixelSize(R.styleable.PwdEditText_spaceWidth, spaceWidth);
            textSize = typedArray.getDimensionPixelSize(R.styleable.PwdEditText_textSize, textSize);
            isDrawCircle = typedArray.getBoolean(R.styleable.PwdEditText_isDrawCircle, isDrawCircle);
            isDrawLine = typedArray.getBoolean(R.styleable.PwdEditText_isDrawLine, isDrawLine);
            circleRadius = typedArray.getDimensionPixelSize(R.styleable.PwdEditText_circleRadius, circleRadius);
            circleColor = typedArray.getColor(R.styleable.PwdEditText_circleColor, circleColor);
            typedArray.recycle();
        }

        setBackgroundColor(Color.TRANSPARENT);
        setCursorVisible(false);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(count)});
        init();
    }

    private void init() {
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStrokeWidth(lineWidth);
        borderPaint.setColor(lineColor);
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(stokesColor);

        focusBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        focusBorderPaint.setStrokeWidth(lineWidth);
        focusBorderPaint.setColor(focusLineColor);
        focusBorderPaint.setAntiAlias(true);
        focusBorderPaint.setStyle(Paint.Style.STROKE);

        focusFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        focusFillPaint.setAntiAlias(true);
        focusFillPaint.setStyle(Paint.Style.FILL);
        focusFillPaint.setColor(focusStokeColor);

        if (!isDrawCircle) {
            paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setTextAlign(Paint.Align.CENTER);
            paintText.setAntiAlias(true);
            paintText.setTextSize(textSize);
            paintText.setColor(textColor);
        } else {
            paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintCircle.setAntiAlias(true);
            paintCircle.setStrokeWidth(2);
            paintCircle.setStyle(Paint.Style.FILL);
            paintCircle.setColor(circleColor);
        }

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (count * height > w)
            throw new IllegalArgumentException("View must be less than the width of the screen!");
        startX = (w - (count * height) - (count - 1) * spaceWidth) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawRectBorder(canvas);
        drawRectFocused(canvas, position);
        if (!isDrawCircle) {
            drawText(canvas);
        } else {
            drawCircle(canvas);
        }
    }

    /**
     * 绘制圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        char[] chars = getText().toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            drawRectFocused(canvas, i);
            canvas.drawCircle(startX + i * height + i * spaceWidth + height / 2, height / 2, circleRadius, paintCircle);
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        char[] chars = getText().toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            drawRectFocused(canvas, i);
            Paint.FontMetrics fontMetrics = paintText.getFontMetrics();
            int baseLineY = (int) (height / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2);
            canvas.drawText(String.valueOf(chars[i]), startX + i * height + i * spaceWidth + height / 2, baseLineY, paintText);
        }
    }


    /**
     * 绘制默认状态
     *
     * @param canvas
     */
    private void drawRectBorder(Canvas canvas) {
        for (int i = 0; i < count; i++) {
            if (isDrawLine)
                drawRoundRect(canvas, startX + i * height + i * spaceWidth,
                        1, startX + i * height + i * spaceWidth + height,
                        height, borderPaint);

            drawRoundRect(canvas, startX + i * height + i * spaceWidth + lineWidth,
                    lineWidth + 1, startX + i * height + i * spaceWidth + height - lineWidth,
                    height - lineWidth, fillPaint);
        }
    }

    /**
     * 绘制输入状态
     *
     * @param canvas
     * @param position
     */
    private void drawRectFocused(Canvas canvas, int position) {
        if (position > count - 1) {
            return;
        }
        if (isDrawLine)
            drawRoundRect(canvas, startX + position * height + position * spaceWidth,
                    1, startX + position * height + position * spaceWidth + height,
                    height, focusBorderPaint);

        drawRoundRect(canvas, startX + position * height + position * spaceWidth + lineWidth,
                lineWidth + 1, startX + position * height + position * spaceWidth + height - lineWidth,
                height - lineWidth, focusFillPaint);
    }

    private void drawRoundRect(Canvas canvas, float left, float top, float right, float bottom, Paint paint) {
        canvas.drawRoundRect(left, top, right, bottom, 20, 20, paint);
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        position = start + lengthAfter;
        if (!TextUtils.isEmpty(text) && text.toString().length() == count)
            if (onFinishListener != null) {
                onFinishListener.onFinish(text.toString());
            }
        invalidate();

    }

    public interface OnFinishListener {
        void onFinish(String msg);
    }
}
