package com.rsen.view;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 大转盘
 * Created by angcyo on 15-12-18 018 15:09 下午.
 */
public class DialView extends View {
    int repeatCount = 0;
    float degreeStep = 20;//每次重绘,添加的角度
    long invalidateTime = 20;//每隔多长时间,重绘一次
    int dialNum = 10;//至少需要转多少圈;
    @ColorInt
    private int[] mColors = new int[]{Color.RED, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GREEN, Color.YELLOW};//转盘每个块区域对应的颜色
    private String[] mTexts = new String[]{"一等奖", "二12等奖", "三阿萨德等奖", "四adf等奖", "五12sdf等奖", "六1ff等奖"};//转盘每个块区域对应的文本
    private float mTextSize = 60f;
    private float[] mRatios = new float[]{1, 2, 3, 2, 1, 3};//转盘每个块区域对应的大小比例
    private TextPaint mTextPaint;//文本画笔
    //转换之后的角度
    private float[] mAngles;
    //可绘制区域
    private Rect mDialRect;
    //文本绘制偏移比例
    private float mTextOffset = 0.3f;
    private float mDialCurrentDegree = 0f;// 转盘当前的角度,
    private float mDialOffsetDegree = 0f;// 转盘偏移的角度,用于决定开始时角度
    private boolean mDialStart = false;//是否开始了
    private boolean mDialEnd = true;//是否结束了
    private Animation animation;
    private ValueAnimator valueAnimator;

    public DialView(Context context) {
        this(context, null);
    }

    public DialView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    /**
     * 测量文本范围
     */
    public static void getTextBounds(Paint paint, String text, Rect bounds) {
        Rect textRound = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRound);
        float width = paint.measureText(text);
        bounds.set(textRound.left, textRound.top, (int) (textRound.left + width), textRound.bottom);
    }

    /**
     * @see com.rsen.view.DialView#getTextBounds(Paint, String, Rect)
     */
    public static Rect getTextBounds(Paint paint, String text) {
        Rect textRound = new Rect();
        getTextBounds(paint, text, textRound);
        return textRound;
    }

    private void init() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
//        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
    }

    /**
     * 需要滚动到那个色块,不受当前是哪个色块的影响
     */
    public void startDial(int index) {
        if (index < 0 || index >= mRatios.length) {
            throw new IllegalArgumentException("index is nullity");
        }

        if (!mDialStart && mDialEnd) {
            animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);
//                    Log.e("tag", "" + interpolatedTime);
//                    mDialCurrentDegree += 20;//闪的很快
//                    postInvalidate();

                    mDialCurrentDegree += 60 * (1f - interpolatedTime);

//                    if (interpolatedTime < 0.3) {
//                        mDialCurrentDegree += 60;//闪的很快
//                    } else if (interpolatedTime < 0.6) {
//                        mDialCurrentDegree += 40;//闪的很快
//                    }else if (interpolatedTime < 0.8) {
//                        mDialCurrentDegree += 20;//闪的很快
//                    }else if (interpolatedTime < 0.9) {
//                        mDialCurrentDegree += 10;//闪的很快
//                    }else if (interpolatedTime < 1) {
//                        mDialCurrentDegree += 2;//闪的很快
//                    }
                    invalidate();
                }
            };
//            animation.setRepeatCount(Animation.INFINITE);
            animation.setDuration(6000);
            startAnimation(animation);
        }
    }

    public void rotateNumber(int num, long longTime, final Runnable endAction) {
        if (num < 0) {
            throw new IllegalArgumentException("num must greater than 0");
        }

        //匀速旋转指定的圈数
        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                Log.e("tag", "" + interpolatedTime);
                mDialCurrentDegree = 360f * interpolatedTime;
                invalidate();
            }
        };
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (endAction != null) {
                    endAction.run();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim.setRepeatCount(num - 1);//圈数就是循环次数
        anim.setDuration(longTime);//每圈的时间
        startAnimation(anim);
    }

    /**
     * 旋转指定圈数,每圈旋转需要的时间
     *
     * @param num      旋转的圈数
     * @param longTime 每圈的时间 毫秒单位
     */
    public void rotateNumber(int num, long longTime) {
        if (num < 0) {
            throw new IllegalArgumentException("num must greater than 0");
        }

        if (valueAnimator != null) {
//            valueAnimator.setRepeatCount(repeatCount + 3);
            valueAnimator.setDuration(1000);
            return;
        }

        valueAnimator = ValueAnimator.ofObject(new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                return fraction * (endValue - startValue);
            }
        }, 0f, 360f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDialCurrentDegree = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                repeatCount = 0;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                valueAnimator = null;
                rotateNumber(1, 10000, null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                repeatCount = 0;
                valueAnimator = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
//                if (repeatCount == 2) {
//                    valueAnimator.setInterpolator(new DecelerateInterpolator());
//                    valueAnimator.setRepeatCount(1);
//
//                }else{
//                    repeatCount++;
//                }
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(longTime);
        valueAnimator.setRepeatCount(10);
//        valueAnimator.setRepeatCount(num - 1);
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(widthSize, heightSize);

        //固定大小
        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(size, size);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void start(int index) {//从0开始的索引

    }

    /***
     * 获取目标需要旋转的角度
     */
    private float getTargetDegree(int index) {

        float targetDegree = 0f;
        float offsetAngle = 360 * dialNum;

        float startAngle = 0, curAngle = 0;
        for (int i = 0; i < mAngles.length; i++) {
            curAngle = mAngles[i];
            if (index == i) {
                break;
            }
            startAngle += curAngle;
        }

        targetDegree = (float) (offsetAngle + startAngle + ((1 - Math.random()) * 0.8 * curAngle));

        return targetDegree;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        testDraw(canvas);

//        mTextPaint.setColor(mColors[0]);
//        Rect mDialRect = new Rect();
//        getmDialRect(mDialRect);
//        canvas.drawRect(mDialRect, mTextPaint);
        //测量一些值
        mAngles = rationsToAngle();
        mDialRect = getmDialRect();

        //开始时有一个角度
        tranToCenter(canvas, mDialRect);
        canvas.rotate(mDialOffsetDegree + mDialCurrentDegree);

        drawDialArea(canvas);
        drawDialText(canvas);

        if (mDialStart) {
            mDialCurrentDegree += degreeStep;//闪的很快
            postInvalidateDelayed(invalidateTime);//转的很快
        }
    }

    private void drawDialText(Canvas canvas) {
        //绘制色块上对应的文本
        canvas.save();
//        tranToCenter(canvas, mDialRect);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);//居中绘制文本

        int radius = mDialRect.width() / 2;//半径
        float textCenterX = radius / 2 + radius / 2 * mTextOffset;//文本偏移之后的横向中心坐标

        float angle = 0;//需要旋转的角度
        for (int i = 0; i < mTexts.length; i++) {
            angle = mAngles[i] / 2;
            canvas.rotate(angle);
            String text = mTexts[i];
            Rect textBound = getTextBounds(mTextPaint, text);
            canvas.drawText(text, textCenterX, textBound.height() / 2, mTextPaint);
            canvas.rotate(angle);
        }
        canvas.restore();
    }

    private void drawDialArea(Canvas canvas) {
        //绘制色块区域
        canvas.save();
//        tranToCenter(canvas, mDialRect);

        RectF rectF = new RectF(-mDialRect.width() / 2, -mDialRect.height() / 2, mDialRect.width() / 2, mDialRect.height() / 2);//扇形绘制区域
        float startAngle = 0;
        for (int i = 0; i < mAngles.length; i++) {
            float endAngle = mAngles[i];
            mTextPaint.setColor(mColors[i]);
            canvas.drawArc(rectF, startAngle, endAngle, true, mTextPaint);
            startAngle += endAngle;
        }

        canvas.restore();
    }

    private Rect tranToCenter(Canvas canvas) {
        Rect dialRect = getmDialRect();
        tranToCenter(canvas, dialRect);
        return dialRect;
    }

    private void tranToCenter(Canvas canvas, Rect dialRect) {
        //讲绘图坐标移至view的中心点
        canvas.translate(dialRect.centerX(), dialRect.centerY());
    }

    private float[] rationsToAngle() {
        //讲色块比例,转换成 角度
        float sum = 0;
        float avg = 0;
        for (float ratio : mRatios) {
            sum += ratio;
        }
        avg = Math.round(360f / sum);

        int len = mRatios.length;
        float[] angles = new float[len];
        for (int i = 0; i < len; i++) {
            angles[i] = mRatios[i] * avg;
        }

        return angles;
    }

    private void testDraw(Canvas canvas) {
        Rect textRound = new Rect();
        getTextBounds(mTextPaint, mTexts[0], textRound);

        mTextPaint.setColor(mColors[0]);
        canvas.drawRect(new Rect(100, 100 - textRound.height(), 100 + textRound.width(), 100), mTextPaint);

        canvas.translate(100, 100);
        canvas.save();
        canvas.rotate(-45f);
        mTextPaint.setColor(mColors[4]);
        canvas.drawText(mTexts[0], 100, 100, mTextPaint);

        mTextPaint.setColor(mColors[1]);
        canvas.drawCircle(100, 100, 1, mTextPaint);

        mTextPaint.setColor(mColors[2]);
        canvas.drawRect(new Rect(10, 10, textRound.width(), textRound.height()), mTextPaint);

        canvas.restore();
//        mTextPaint.setColor(Color.BLACK);
        canvas.drawRect(new Rect(0, 0, 800, 800), mTextPaint);
        mTextPaint.setColor(Color.BLACK);

        canvas.drawArc(new RectF(-400, -400, 400, 400), 0, 30, true, mTextPaint);
    }

    private Rect getmDialRect() {
        //获取转盘可绘制rect
        int width, height;
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        return new Rect(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
    }


}
