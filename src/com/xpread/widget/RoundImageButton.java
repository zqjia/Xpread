
package com.xpread.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.xpread.R;

public class RoundImageButton extends View {
    private int defaultColor = 0;
    private int defaultTextSize = 14;
    private int mBorderColor;
    private int mBackgroundColor;
    private int mBorderWidth;
    private Drawable mImageDrawable;
    private int mImageWidth;
    private int mImageHeight;
    private String mText;
    private int mTextSize;
    private int mTextColor;
    private int mPadding;
    private int defaultWidth = 0;
    private int defaultHeight = 0;
    private int mOffsetX;
    private int radius;
    
    private Paint mPaint;
    private Context mContext;

    public RoundImageButton(Context context) {
        this(context, null);
    }

    public RoundImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setCustomAttributes(attrs);
    }

    private void setCustomAttributes(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.roundedimagebutton);
        mBorderColor = ta.getColor(R.styleable.roundedimagebutton_border_color, defaultColor);
        mBackgroundColor = ta.getColor(R.styleable.roundedimagebutton_background_color,
                defaultColor);
        mBorderWidth = ta.getDimensionPixelSize(R.styleable.roundedimagebutton_border_width, 0);
        mImageDrawable = ta.getDrawable(R.styleable.roundedimagebutton_image_resource);
        mImageWidth = ta.getDimensionPixelSize(R.styleable.roundedimagebutton_image_width, 0);
        mImageHeight = ta.getDimensionPixelSize(R.styleable.roundedimagebutton_image_height, 0);
        mOffsetX = ta.getDimensionPixelSize(R.styleable.roundedimagebutton_image_offset_x, 0);

        mText = ta.getString(R.styleable.roundedimagebutton_text);
        mTextColor = ta.getColor(R.styleable.roundedimagebutton_text_color, defaultColor);
        mTextSize = ta.getDimensionPixelSize(R.styleable.roundedimagebutton_text_size,
                defaultTextSize);

        mPadding = ta.getDimensionPixelSize(R.styleable.roundedimagebutton_padding_between, 0);

        ta.recycle();
        
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        measure(0, 0);

        if (defaultWidth == 0) {
            defaultWidth = getWidth();
        }
        if (defaultHeight == 0) {
            defaultHeight = getHeight();
        }

        radius = (defaultWidth > defaultHeight ? defaultHeight : defaultWidth) / 2 - mBorderWidth;

        drawCircleBorder(canvas, radius + mBorderWidth / 2);

        drawImage(canvas);

        drawText(canvas);

    }

    private void drawCircleBorder(Canvas canvas, int radius) {
        mPaint.setColor(mBackgroundColor);
        canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, mPaint);

        mPaint.setAntiAlias(true);
        //如果该项设置为true，则图像在动画进行中会滤掉对Bitmap图像的优化操作，加快显示  
        //速度，本设置项依赖于dither和xfermode的设置  
        mPaint.setFilterBitmap(true);
        //设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        mPaint.setDither(true);
        mPaint.setColor(mBorderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        //当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的粗细度
        mPaint.setStrokeWidth(mBorderWidth);

        canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, mPaint);
        this.mPaint.reset();
    }

    private void drawImage(Canvas canvas) {
        Bitmap image = ((BitmapDrawable)mImageDrawable).getBitmap();
        if (mImageWidth == 0) {
            mImageWidth = image.getWidth();
        }
        if (mImageHeight == 0) {
            mImageHeight = image.getHeight();
        }
        int left = (defaultWidth - mImageWidth) / 2 + mOffsetX;

        int top = (defaultHeight - mImageHeight - mPadding - mTextSize) / 2;
        if (mText == null) {
            top = (defaultHeight - mImageHeight) / 2;
        }
        canvas.drawBitmap(image, left, top, null);
        image = null;
    }

    private void drawText(Canvas canvas) {
        if (mText == null) {
            return;
        }
        this.mPaint.setColor(mTextColor);
        this.mPaint.setTextSize(mTextSize);
        this.mPaint.setAntiAlias(true);

        FontMetrics fm = this.mPaint.getFontMetrics();

        int x = (int)((defaultWidth - this.mPaint.measureText(mText)) / 2);
        
        //defaultHeight/2, 就是整个view的一半，然后padding和image的一半，就是image关于padding的对称点，加上一半的ascent，就是text的
        int y = (int)((defaultHeight + mImageHeight + mPadding + fm.ascent) / 2 + fm.descent - fm.ascent);
        
        canvas.drawText(mText, x, y, this.mPaint);
        this.mPaint.reset();
    }
    
    public void refreshButton(int bgColor, int borderColor, Drawable imageSource, String text, int textColor) {
        this.mBackgroundColor = bgColor;
        this.mBorderColor = borderColor;
        this.mImageDrawable = imageSource;
        this.mText = text;
        this.mTextColor = textColor;
        
        invalidate();
    }
    
    public String getText() {
        return this.mText;
    }
}
