package com.rsen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.angcyo.rsen.R;

/**
 * Created by robi on 2016-04-15 17:55.
 */
public class RoundImageView extends ImageView {
    private float roundRadius = 15f;
    private Drawable roundImage;

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        roundRadius = typedArray.getDimension(R.styleable.RoundImageView_roundRadius, roundRadius);
        roundImage = typedArray.getDrawable(R.styleable.RoundImageView_roundImage);
        typedArray.recycle();

        init();
    }

    public RoundImageView(Context context) {
        this(context, null);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?
                Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    private void init() {
        if (roundImage == null) {
            return;
        }

        RoundedBitmapDrawable shopDrawable = RoundedBitmapDrawableFactory.create(getResources(), drawableToBitmap(roundImage));

        //在v4 以前的版本中,使用此方法可以设置 图片为圆角
        shopDrawable.setCornerRadius(roundRadius);

        //在v4 新版本中,直接提供了方法设置圆角,代码其实和上面的是一样的
        shopDrawable.setCircular(true);

        //直接设置,就可以啦...
        setImageDrawable(shopDrawable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (roundImage == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int width, height;
            if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
                width = roundImage.getIntrinsicWidth();
            } else {
                width = MeasureSpec.getSize(widthMeasureSpec);
            }

            if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
                height = roundImage.getIntrinsicHeight();
            } else {
                height = MeasureSpec.getSize(heightMeasureSpec);
            }
            setMeasuredDimension(width, height);
        }
    }
}
