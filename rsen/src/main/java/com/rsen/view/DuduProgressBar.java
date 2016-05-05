package com.rsen.view;

import android.animation.ValueAnimator;
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
import android.view.animation.DecelerateInterpolator;

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
    private boolean mAnim = true;

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
        mRectF.set(getPaddingLeft(), getPaddingTop(), getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
    }


    /**
     * 设置进度值
     *
     * @param progress 取值范围为 0-1f
     */
    public void setProgress(float progress) {
        if (progress > 1) {
            progress = 1;
        }
        if (progress < 0) {
            progress = 0;
        }

        updateProgress(progress);

        if (mAnim) {
            animToProgress(progress);
        } else {
            postInvalidate();
        }
    }

    private void updateProgress(float progress) {
        this.progress = progress;
        initProgress();
    }

    public void setAnim(boolean anim) {
        this.mAnim = anim;
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

    private void animToProgress(float progress) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, progress);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            updateProgress((Float) animation.getAnimatedValue());
            postInvalidate();
        });
        animator.start();
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
