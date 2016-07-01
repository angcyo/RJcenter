package com.rsen.github.filldrawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.angcyo.rsen.R;

/**
 * Created by Dimitry Ivanov on 26.10.2015.
 * https://github.com/angcyo/FillDrawable
 */
public class FillImageView extends ImageView {

    private FillDrawable mFillDrawable;

    public FillImageView(Context context) {
        this(context, null);
    }

    public FillImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FillImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {

        if (attributeSet != null) {

            final TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.FillImageView);
            try {

                final int normalColor   = typedArray.getColor(R.styleable.FillImageView_fiv_normalColor, 0);
                final int fillColor     = typedArray.getColor(R.styleable.FillImageView_fiv_fillColor, 0);
                final int from          = typedArray.getInteger(R.styleable.FillImageView_fiv_from, FillDrawable.FROM_LEFT);
                final Drawable drawable = typedArray.getDrawable(R.styleable.FillImageView_fiv_drawable);

                // useful to debugging
                final float percent = typedArray.getFloat(R.styleable.FillImageView_fiv_percent, 33.F);

                if (drawable != null) {

                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                    //noinspection ResourceType
                    final FillDrawable fillDrawable = new FillDrawable(from, drawable.mutate())
                            .setNormalColor(normalColor)
                            .setFillColor(fillColor);

                    if (isInEditMode()) {
                        setWillNotDraw(false);
                        fillDrawable.setFillPercent(percent);
                    }

                    setFillDrawable(fillDrawable);
                }

            } finally {
                typedArray.recycle();
            }
        }
    }

    public void setFillDrawable(FillDrawable fillDrawable) {
        mFillDrawable = fillDrawable;
        setImageDrawable(mFillDrawable);
    }

    public @Nullable
    FillDrawable getFillDrawable() {
        return mFillDrawable;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isInEditMode() && mFillDrawable != null) {
            mFillDrawable.draw(canvas);
            return;
        }
        super.onDraw(canvas);
    }
}
