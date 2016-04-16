package com.rsen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.angcyo.rsen.R;

/**
 * Created by angcyo on 2016-04-16 16:42.
 */
public class RRoundView extends ImageView {
    private Drawable roundDrawable;
    private Bitmap roundBitmap;
    private int roundRadius = 10;
    private Paint paint;
    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private RectF rectF;

    public RRoundView(Context context) {
        this(context, null);
    }

    public RRoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RRoundView);
//        roundDrawable = typedArray.getDrawable(R.styleable.RRoundView_roundImage);
        roundDrawable = getDrawable();
        roundRadius = typedArray.getDimensionPixelSize(R.styleable.RRoundView_roundRadius, roundRadius);
        typedArray.recycle();
        init();
    }

    private void init() {
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(1f);
        rectF = new RectF();

        roundBitmap = drawableToBitmap(roundDrawable);
    }

    public void setRoundBitmap(Bitmap bitmap) {
        if (roundBitmap != null) {
            roundBitmap.recycle();
        }
        roundBitmap = bitmap;
        postInvalidate();
    }

    public void setRoundDrawable(Drawable drawable) {
        if (roundBitmap != null) {
            roundBitmap.recycle();
        }
        roundBitmap = drawableToBitmap(drawable);
        postInvalidate();
    }

    public void setRoundRadius(int roundRadius) {
        this.roundRadius = roundRadius;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (roundDrawable == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int width, height;
            width = roundDrawable.getIntrinsicWidth();
            height = roundDrawable.getIntrinsicHeight();
            if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.AT_MOST) {
                width = MeasureSpec.getSize(widthMeasureSpec);
            }
            if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.AT_MOST) {
                height = MeasureSpec.getSize(heightMeasureSpec);
            }
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        rectF.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (roundBitmap == null) {
            super.onDraw(canvas);
        } else {
            canvas.drawRoundRect(rectF, roundRadius, roundRadius, paint);
            paint.setXfermode(xfermode);
            canvas.drawBitmap(scaleBitmap(roundBitmap), 0, 0, paint);
        }
    }

    private Bitmap scaleBitmap(Bitmap bitmap) {
        Bitmap result = bitmap;
        Matrix matrix;
        float bmpW = bitmap.getWidth();
        float bmpH = bitmap.getHeight();
        float viewW = getMeasuredWidth();
        float viewH = getMeasuredHeight();
        ScaleType scaleType = getScaleType();
        if (scaleType == ScaleType.FIT_XY) {
            matrix = new Matrix();
            matrix.setScale(viewW / bmpW, viewH / bmpH);
            result = Bitmap.createBitmap(bitmap, 0, 0, (int) bmpW, (int) bmpH, matrix, true);
        } else if (scaleType == ScaleType.CENTER) {
            result = Bitmap.createBitmap(bitmap, (int) ((bmpW - viewW) / 2), (int) ((bmpH - viewH) / 2), (int) viewW, (int) viewH);
        }
        return result;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
