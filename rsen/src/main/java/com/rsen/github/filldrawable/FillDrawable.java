package com.rsen.github.filldrawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Dimitry Ivanov on 06.08.2015.
 * https://github.com/angcyo/FillDrawable
 */
public class FillDrawable extends Drawable {

    public static final int FROM_LEFT   = 1; // 1 << 0
    public static final int FROM_TOP    = 1 << 1;
    public static final int FROM_RIGHT  = 1 << 2;
    public static final int FROM_BOTTOM = 1 << 3;

    @IntDef({FROM_LEFT, FROM_TOP, FROM_RIGHT, FROM_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface From {}

    private final Drawable mDrawable;
    private final CanvasClipper mCanvasClipper;
    private float mFillPercent;

    private ColorFilter mNormalColorFilter;
    private ColorFilter mFillColorFilter;

    public FillDrawable(
            @From int directionFrom,
            Drawable drawable
    ) {
        mCanvasClipper = CanvasClipper.create(directionFrom);
        mDrawable = drawable;
    }

    public FillDrawable setNormalColor(@ColorInt int normalColor) {
        return setNormalColor(normalColor, PorterDuff.Mode.MULTIPLY);
    }

    // note, that drawable won't call `invalidateSelf`, should be called manually
    public FillDrawable setNormalColor(@ColorInt int normalColor, PorterDuff.Mode mode) {
        mNormalColorFilter = new PorterDuffColorFilter(normalColor, mode);
        return this;
    }

    public FillDrawable setFillColor(@ColorInt int fillColor) {
        return setFillColor(fillColor, PorterDuff.Mode.MULTIPLY);
    }

    public FillDrawable setFillColor(@ColorInt int fillColor, PorterDuff.Mode mode) {
        mFillColorFilter = new PorterDuffColorFilter(fillColor, mode);
        return this;
    }

    public void setFillPercent(@FloatRange(from = .0F, to = 100.F) float fillPercent) {
        mFillPercent = fillPercent;
        invalidateSelf();
    }

    public float getFillPercent() {
        return mFillPercent;
    }

    @Override
    public void onBoundsChange(Rect bounds) {
        mCanvasClipper.setBounds(bounds);
        super.onBoundsChange(bounds);
    }

    @Override
    public void draw(Canvas canvas) {

        mCanvasClipper.prepare(mFillPercent);

        canvas.save();
        mCanvasClipper.clipNormal(canvas);
        mDrawable.setColorFilter(mNormalColorFilter);
        mDrawable.draw(canvas);
        canvas.restore();

        canvas.save();
        mCanvasClipper.clipProgress(canvas);
        mDrawable.setColorFilter(mFillColorFilter);
        mDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getIntrinsicWidth() {
        return mDrawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mDrawable.getIntrinsicHeight();
    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
