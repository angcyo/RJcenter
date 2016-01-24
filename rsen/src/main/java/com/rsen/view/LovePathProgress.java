package com.rsen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import com.angcyo.rsen.R;
import com.rsen.util.ResUtil;

import java.util.ArrayList;

/**
 * Created by angcyo on 16-01-24-024.
 */
public class LovePathProgress extends View {

    int mDrawDelay = 30;//多少毫秒绘制一次
    int mDrawPause = 1000;//绘制一次之后,暂停多少毫秒
    Path mPath;//最主要的path
    Paint mPaint;//最主要的画笔
    ArrayList<PointF> mList;//保存所有path中的点
    int mCurIndex = 0;//当前绘制到了那个点;
    boolean isOneCircle = false;//是否绘制一周了
    float mPathWidth = 2;//dp path的宽度

    int mViewWidth, mViewHeight;//视图的宽高

    float mMinSize = 100;//dp 最小的大小

    int mDrawStep = 10;//每次绘制多少个点

    @ColorInt
    int mPathColor = Color.RED;//path 的颜色

    public LovePathProgress(Context context) {
        this(context, null);
    }

    public LovePathProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LovePathProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mMinSize = ResUtil.dpToPx(getResources(), mMinSize);
        mPathWidth = ResUtil.dpToPx(getResources(), mPathWidth);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LovePathProgress);
        mPathColor = typedArray.getColor(R.styleable.LovePathProgress_lovePathColor, mPathColor);
        mPathWidth = typedArray.getDimension(R.styleable.LovePathProgress_lovePathWidth, mPathWidth);
        mDrawDelay = typedArray.getInt(R.styleable.LovePathProgress_lovePathDrawDelay, mDrawDelay);
        mDrawStep = typedArray.getInt(R.styleable.LovePathProgress_lovePathDrawStep, mDrawStep);
        mDrawPause = typedArray.getInt(R.styleable.LovePathProgress_lovePathPauseTime, mDrawPause);
        mMinSize = typedArray.getDimension(R.styleable.LovePathProgress_lovePathMinSize, mMinSize);
        typedArray.recycle();
        initView();
    }

    private void initView() {
        setWillNotDraw(false);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mPathWidth);
        mPaint.setColor(mPathColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize, widthMode, heightSize, heightMode;
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        widthMode = MeasureSpec.getMode(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            widthSize = (int) mMinSize;
            heightSize = (int) mMinSize;
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;

        fillList();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
        for (int i = 0; i < mDrawStep; i++) {
            PointF pointF = mList.get(mCurIndex);

            if (mCurIndex == 0) {
                mPath.reset();
                mPath.moveTo(pointF.x, pointF.y);
                isOneCircle = false;
            }

            mCurIndex++;
            mPath.lineTo(pointF.x, pointF.y);

            if (mCurIndex >= mList.size()) {
                mCurIndex = 0;
                isOneCircle = true;
                mPath.close();
                canvas.drawPath(mPath, mPaint);
                postInvalidateDelayed(mDrawPause);//暂停时间
                return;
            }
        }

        postInvalidateDelayed(mDrawDelay);
    }

    /**
     * 填充list
     */
    private void fillList() {
        mList = new ArrayList<>();
        RectF drawRect = getDrawRectF();

        //从爱心的低点开始计算
        PointF startPoint = new PointF();
        startPoint.x = drawRect.centerX();
        startPoint.y = drawRect.bottom;
        mList.add(startPoint);//开始点

        //爱心 左边斜线,45度斜角计算
        PointF nextPoint = new PointF();
        nextPoint.x = startPoint.x - 1;
        nextPoint.y = startPoint.y - 1;
        while (nextPoint.x >= drawRect.left && nextPoint.y >= drawRect.centerY()) {
            mList.add(nextPoint);
            nextPoint = new PointF(nextPoint.x, nextPoint.y);
            --nextPoint.x;
            --nextPoint.y;
        }

        float lastY = nextPoint.y;//最后点的y坐标

        //爱心, 左边上半圆
        float radius = drawRect.width() / 4;//爱心2个半圆的半径
        PointF centerPoint = new PointF();//圆心坐标
        centerPoint.x = radius;
        centerPoint.y = 0;

        for (int i = 180; i >= 0; i--) {//上半圆
            nextPoint = new PointF();
            nextPoint.x = drawRect.left + (float) (centerPoint.x + radius * Math.cos(i * Math.PI / 180f));
            nextPoint.y = lastY - (float) (centerPoint.y + radius * Math.sin(i * Math.PI / 180f));
            mList.add(nextPoint);
        }

        //爱心, 右边上半圆
        centerPoint = new PointF();//圆心坐标
        centerPoint.x = 3 * radius;
        centerPoint.y = 0;

        for (int i = 180; i >= 0; i--) {//上半圆
            nextPoint = new PointF();
            nextPoint.x = drawRect.left + (float) (centerPoint.x + radius * Math.cos(i * Math.PI / 180f));
            nextPoint.y = lastY - (float) (centerPoint.y + radius * Math.sin(i * Math.PI / 180f));
            mList.add(nextPoint);
        }

        //爱心 右边边斜线,45度斜角计算
        startPoint = new PointF();
        startPoint.x = nextPoint.x;
        startPoint.y = nextPoint.y;

        nextPoint = new PointF();
        nextPoint.x = startPoint.x - 1;
        nextPoint.y = startPoint.y + 1;
        while (nextPoint.x >= drawRect.centerX() && nextPoint.y <= drawRect.bottom) {
            mList.add(nextPoint);
            nextPoint = new PointF(nextPoint.x, nextPoint.y);
            --nextPoint.x;
            ++nextPoint.y;
        }

        //结束...
    }

    /**
     * 去除padding 的 可绘制区域
     */
    private RectF getDrawRectF() {
        RectF rectF = new RectF();
        rectF.set(getPaddingLeft(), getPaddingTop(), mViewWidth - getPaddingRight(), mViewHeight - getPaddingBottom());
        return rectF;
    }
}
