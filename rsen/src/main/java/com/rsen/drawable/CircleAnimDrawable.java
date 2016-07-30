package com.rsen.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.util.Log;
import android.util.TypedValue;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robi on 2016-07-29 14:27.
 */
public class CircleAnimDrawable extends Drawable {

    public static final int POS_LEFT = 1;//放置在左边
    public static final int POS_CENTER = 2;//放置在中间
    public static final int POS_RIGHT = 3;//放置在右边
    private int mCircleRadius;//圆的半径
    private int mCircleRadiusOut;//外圆的半径
    private int mCircleRadiusMax;//最大允许的半径(主要用来绘制左边和右边的布局)

    private int mRadiusOffsetOut = 10;//外圆的偏移量(控制2个圆之间的距离)
    private int mRadiusOffset = 1;//多偏移的像素(让里面的圆有一种被切割的感觉)
    private int mRadiusDrawStep = 6;//每次绘制添加的半径(控制动画持续的时间,越大越快结束)
    @ColorInt
    private int mCircleColor;//圆的颜色@ColorInt
    private int mCircleColorOut;//外圆的颜色
    private Paint mPaint;
    private float mCx;
    private float mCy;
    private int curRadius;//控制绘制的当前半径

    private int mPosition = POS_CENTER;//布局存放在什么地方

    private Context mContext;

    public CircleAnimDrawable(Context context) {
        this(context, Color.parseColor("#1EC2B6"));
    }

    public CircleAnimDrawable(Context context, @ColorInt int circleColor) {
        mContext = context;
        mCircleColor = circleColor;
        mCircleColorOut = getColorWidthAlpha(mCircleColor, 100);//不透明度取值范围0-255 (越小越透明)
        init();
    }

    public CircleAnimDrawable(Context context, @ColorInt int circleColor, @Position int position) {
        this(context, circleColor);
        mPosition = position;
    }

    /**
     * 给指定的颜色,加上透明度
     */
    public static int getColorWidthAlpha(int color, int alpha) {
        return Color.argb(alpha,
                Color.red(color),
                Color.green(color),
                Color.blue(color));
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mRadiusOffset = dpToPx(mRadiusOffset);
        mRadiusOffsetOut = dpToPx(mRadiusOffsetOut);
        mRadiusDrawStep = dpToPx(mRadiusDrawStep);
    }

    @Override
    public void draw(Canvas canvas) {
        float maxRadius;
        boolean drawLeft = false;
        boolean drawRight = false;
        switch (mPosition) {
            case POS_LEFT:
                maxRadius = mCircleRadiusMax;
                drawLeft = true;
                break;
            case POS_RIGHT:
                drawRight = true;
                maxRadius = mCircleRadiusMax;
                break;
            case POS_CENTER:
            default:
                maxRadius = mCircleRadiusOut;
                break;
        }

        mPaint.setColor(mCircleColor);
        canvas.drawCircle(mCx, mCy, Math.min(curRadius, mCircleRadius), mPaint);
        mPaint.setColor(mCircleColorOut);
        canvas.drawCircle(mCx, mCy, Math.min(curRadius, mCircleRadiusOut), mPaint);

        if (curRadius >= mCircleRadiusOut) {
            if (drawLeft) {
                mPaint.setColor(mCircleColor);
                canvas.drawArc(getArcRecF(Math.min(curRadius, mCircleRadiusMax)), 90, 180, true, mPaint);
            } else if (drawRight) {
                mPaint.setColor(mCircleColor);
                canvas.drawArc(getArcRecF(Math.min(curRadius, mCircleRadiusMax)), -90, 180, true, mPaint);
            }
        }

        if (curRadius < maxRadius) {
            curRadius += mRadiusDrawStep;
            invalidateSelf();
        } else {
//            curRadius = getBeginDrawRadius();
        }
    }

    @Override
    public void setAlpha(int alpha) {
        Log.e("angcyo", "");

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        Log.e("angcyo", "");
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    protected boolean onStateChange(int[] state) {
        if (!convertToList(state).contains(android.R.attr.state_checked)) {
            resetBeginDrawRadius();
        }
        return super.onStateChange(state);
    }

    /**
     * 重置开始绘制的位置,用于控制动画开始
     */
    public void resetBeginDrawRadius() {
        curRadius = getBeginDrawRadius();
    }

    @Override
    public boolean setState(int[] stateSet) {
        return super.setState(stateSet);
    }

    /**
     * 设置布局放置的位置
     */
    public CircleAnimDrawable setPosition(@Position int position) {
        mPosition = position;
        return this;
    }

    private int getBeginDrawRadius() {
        return getBeginDrawRadius(getBounds());
    }

    private int getBeginDrawRadius(Rect bounds) {
        return Math.min(bounds.width(), bounds.height()) / 4;
    }

    private List<Integer> convertToList(int[] array) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i : array) {
            list.add(i);
        }
        return list;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mCx = bounds.width() / 2;
        mCy = bounds.height() / 2;
        curRadius = getBeginDrawRadius(bounds);
        mCircleRadius = Math.min(bounds.width(), bounds.height()) / 2 + mRadiusOffset;
        mCircleRadiusOut = mCircleRadius + mRadiusOffsetOut;

        switch (mPosition) {
            case POS_LEFT:
            case POS_RIGHT:
                mCircleRadiusMax = (int) Math.ceil(Math.sqrt(mCx * mCx + mCy * mCy));
                break;
            case POS_CENTER:
            default:
                mCircleRadiusMax = mCircleRadiusOut;
                break;
        }
    }

    private RectF getArcRecF(int radius) {
        final Rect bounds = getBounds();
        float cx = bounds.width() / 2;
        float cy = bounds.height() / 2;
        return new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }

    @IntDef({POS_LEFT, POS_CENTER, POS_RIGHT})
    @IntRange(from = 1)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Position {
    }
}
