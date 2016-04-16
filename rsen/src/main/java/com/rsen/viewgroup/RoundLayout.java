package com.rsen.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.angcyo.rsen.R;

/**
 * Created by angcyo on 2016-04-16 23:29.
 * 圆角布局,任意View都能实现圆角
 */
public class RoundLayout extends RelativeLayout {
    private float roundLayoutRadius = 14f;
    private Path roundPath;
    private RectF rectF;

    public RoundLayout(Context context) {
        this(context, null);
    }

    public RoundLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundLayout);
        roundLayoutRadius = typedArray.getDimensionPixelSize(R.styleable.RoundLayout_roundLayoutRadius, (int) roundLayoutRadius);
        typedArray.recycle();

        init();
    }

    private void init() {
        setWillNotDraw(false);
        roundPath = new Path();
        rectF = new RectF();
    }

    private void setRoundPath() {
        roundPath.addRoundRect(rectF, roundLayoutRadius, roundLayoutRadius, Path.Direction.CW);
    }


    public void setRoundLayoutRadius(float roundLayoutRadius) {
        this.roundLayoutRadius = roundLayoutRadius;
        setRoundPath();
        postInvalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        rectF.set(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        setRoundPath();
    }

    @Override
    public void draw(Canvas canvas) {
        if (roundLayoutRadius > 0f) {
            canvas.clipPath(roundPath);
        }
        super.draw(canvas);
    }
}
