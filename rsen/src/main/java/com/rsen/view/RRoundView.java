package com.rsen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.angcyo.rsen.R;

/**
 * Created by angcyo on 2016-04-16 16:42.
 */
public class RRoundView extends ImageView {
    private int roundRadius = 10;

    public RRoundView(Context context) {
        this(context, null);
    }

    public RRoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RRoundView);
        roundRadius = typedArray.getDimensionPixelSize(R.styleable.RRoundView_roundRadius, roundRadius);
        typedArray.recycle();

        roundPath = new Path();
        rectF = new RectF();
    }

    public void setRoundRadius(int roundRadius) {
        this.roundRadius = roundRadius;
        resetPath();
        postInvalidate();
    }

    private void resetPath() {
        roundPath.addRoundRect(rectF, roundRadius, roundRadius, Path.Direction.CW);
    }

    private Path roundPath;
    private RectF rectF;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        rectF.set(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        resetPath();
    }

    @Override
    public void draw(Canvas canvas) {
        if (roundRadius > 0f) {
            canvas.clipPath(roundPath);
        }
        super.draw(canvas);
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

    public static Bitmap roundBitmap(Bitmap bitmap, int radius) {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
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
