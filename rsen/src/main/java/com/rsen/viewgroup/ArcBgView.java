package com.rsen.viewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 弧形的 RelativeLayout
 * Created by angcyo on 16-01-31-031.
 */
public class ArcBgView extends RelativeLayout {
    private Paint mPaint;
    private int mBgColor;
    private RectF arcRectF;
    private float factor = 0.8f;//值越小,弧形越弯

    public ArcBgView(Context context) {
        this(context, null);
    }

    public ArcBgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            mBgColor = ((ColorDrawable) background).getColor();
        } else {
            mBgColor = Color.WHITE;
        }
        setBackgroundColor(0);
        initView();
    }

    private void initView() {
        setWillNotDraw(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(mBgColor);
        mPaint.setStrokeWidth(2f);
    }

    public void setBgColor(@ColorInt int color) {
        mBgColor = color;
        mPaint.setColor(mBgColor);
        invalidate();
    }

    public void setFactor(float factor) {
        this.factor = factor;
        initArcRectF(getWidth(), getHeight());
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(arcRectF, -180, 180, true, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initArcRectF(w, h);
    }

    private void initArcRectF(int w, int h) {
        float offset = h * factor;
        arcRectF = new RectF(-offset, 0, w + offset, 2 * h);
    }
}
