package com.rsen.viewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by angcyo on 16-01-31-031.
 */
public class ArcBgView extends RelativeLayout {
    private Paint mPaint;
    private int mBgColor;

    public ArcBgView(Context context) {
        this(context, null);
    }

    public ArcBgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBgColor = Color.YELLOW;

        initView();
    }

    private void initView() {
        setWillNotDraw(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(mBgColor);
        mPaint.setStrokeWidth(2f);

//        ArcShape arcShape = new ArcShape(-180, 90);
//        ShapeDrawable shapeDrawable = new ShapeDrawable(arcShape);
//        shapeDrawable.getPaint().setColor(Color.RED);
//        shapeDrawable.getPaint().setStrokeWidth(2f);
//        shapeDrawable.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
//        ResUtil.setBgDrawable(this, shapeDrawable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawRect(new Rect(0, 0, 100, 100), mPaint);
//        canvas.drawArc(new RectF(0, 0, getWidth(), getHeight()), 180, 90, true, mPaint);
        //不能直接从-180度-->0度
        //绘制左半部
        float offset = 0;
        RectF leftRect = new RectF(-offset, 0, getWidth(), getHeight());
        RectF rightRect = new RectF(0, 0, getWidth() + offset, getHeight());
//        canvas.drawArc(leftRect, -180, 90, true, mPaint);
        canvas.drawArc(rightRect, -180, 180, true, mPaint);

    }
}
