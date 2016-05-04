package com.rsen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.angcyo.rsen.R;


/**
 * Created by robi on 2016-05-04 19:49.
 */
public class DuduProgressBar extends View {
    RectF mRectF;
    Paint mPaint;
    float roundX = 15, roundY = 15;
    int minWidth = 200, minHeight = 20;
    private int backgroundColor = Color.GRAY;
    private int progressColor = Color.parseColor("#398DEE");
    private float progress = 0f;//进度条的比例,0-1

    public DuduProgressBar(Context context) {
        super(context);
        initView();
    }

    public DuduProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DuduProgressBar);
        backgroundColor = typedArray.getColor(R.styleable.DuduProgressBar_background_color, backgroundColor);
        progressColor = typedArray.getColor(R.styleable.DuduProgressBar_progress_color, progressColor);
        progress = typedArray.getFloat(R.styleable.DuduProgressBar_progress, progress);
        roundX = roundY = typedArray.getDimensionPixelOffset(R.styleable.DuduProgressBar_round, (int) roundX);
        typedArray.recycle();

        initView();
    }

    private void initView() {
        setWillNotDraw(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(backgroundColor);
        mRectF = new RectF();
    }

    private void initProgress() {
        if (progress == 0) {
            mPaint.setShader(null);
        } else {
            mPaint.setShader(new LinearGradient(0, 0, getMeasuredWidth(), 0,
                    new int[]{progressColor, backgroundColor},
                    new float[]{progress, progress + 0.001f}, Shader.TileMode.CLAMP));
        }
        mRectF.set(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
    }


    public void setProgress(float progress) {
        this.progress = progress;
        initProgress();
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width, height;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            width = minWidth;
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            height = minHeight;
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initProgress();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(mRectF, roundX, roundY, mPaint);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putFloat("p", progress);
        bundle.putParcelable("s", super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        progress = bundle.getFloat("p");
        super.onRestoreInstanceState(bundle.getParcelable("s"));
    }
}
