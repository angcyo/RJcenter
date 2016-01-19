package com.rsen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.angcyo.rsen.R;
import com.rsen.util.ResUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by angcyo on 16-01-17-017.
 */
public class CirclePathButton extends Button {

    /**
     * 用来绘制路径,最重要的成员
     */
    Path mPath;

    /**
     * 用来绘制对勾的路径,最重要的成员
     */
    Path mPathTick;

    /**
     * 画笔
     */
    Paint mPaint;

    /**
     * path 的颜色
     */
    @ColorInt
    int mPathColor = Color.BLUE;
    /**
     * 激活选择模式之后, 选择状态的颜色
     */
    @ColorInt
    int mPathSelectColor = Color.RED;

    /**
     * path 的宽度
     */
    float mPathWidth = 2;//dp
    int mViewWidth, mViewHeight;

    /**
     * path 偏移边框的距离
     */
    int mPathOffset = 4;//
    boolean isDown = false;
    boolean isUp = false;
    /**
     * 当前绘制到的角度
     */
    float mCurAngle;
    /**
     * 当前开始绘制的角度
     */
    float mStartAngle = -90;//默认12点钟方向
    /**
     * 每次绘制,移动的角度,越大,移动愉快
     */
    float mDrawStep = 10;
    float mCurDrawStep;
    /**
     * 多少毫秒绘制一次
     */
    int mDrawDelay = 40;
    int mCurDrawDelay;

    /**
     * 样式, 背景透明, 只显示圆圈
     */
    boolean mIsBorderStyle = true;

    /**
     * 样式, 背景透明,  显示圆圈+对勾, 需要 mIsBorderStyle = true 才生效
     */
    boolean mIsSelectStyle = true;

    /**
     * 边框的颜色
     */
    @ColorInt
    int mBorderColor = Color.BLUE;
    /**
     * 边框背景
     */
    Drawable bgDrawable;

    /**
     * 保存对勾图形的path 点
     */
    List<PointF> mTickLeftList;//左边线段
    List<PointF> mTickRightList;//右边线段
    /**
     * 当前绘制到了那个点, 对勾 的path
     */
    int mTickCurIndex = 0;
    /**
     * 对勾绘制的步长
     */
    int mTickStep = 8;

    /**
     * 是否到了一圈
     */
    boolean isOneCircle = false;

    /**
     * 当前是否是选中状态
     */
    boolean isSelected = false;

    /**
     * 开始绘制对勾
     */
    boolean isBeginSelecting = false;
    OnSelectChanged onSelectChanged;

    public CirclePathButton(Context context) {
        this(context, null);
    }

    public CirclePathButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public CirclePathButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePathButton);

        mPathWidth = typedArray.getDimension(R.styleable.CirclePathButton_circlePathWidth, ResUtil.dpToPx(getResources(), mPathWidth));
        mPathOffset = (int) typedArray.getDimension(R.styleable.CirclePathButton_circlePathOffset, mPathWidth / 2);
        mPathColor = typedArray.getColor(R.styleable.CirclePathButton_circlePathColor, mPathColor);
        mPathSelectColor = typedArray.getColor(R.styleable.CirclePathButton_circlePathSelectColor, mPathSelectColor);
        mDrawStep = typedArray.getFloat(R.styleable.CirclePathButton_circlePathDrawStep, mDrawStep);
        mDrawDelay = typedArray.getInteger(R.styleable.CirclePathButton_circlePathDrawDelay, mDrawDelay);

        mIsBorderStyle = typedArray.getBoolean(R.styleable.CirclePathButton_circlePathBorderStyle, mIsBorderStyle);
        mIsSelectStyle = typedArray.getBoolean(R.styleable.CirclePathButton_circlePathSelectStyle, mIsSelectStyle);
        mBorderColor = typedArray.getColor(R.styleable.CirclePathButton_circlePathBorderColor, mBorderColor);

        mStartAngle = typedArray.getFloat(R.styleable.CirclePathButton_circlePathStartAngle, mStartAngle);

        typedArray.recycle();
        initView();
    }

    private void initView() {
        setWillNotDraw(false);
        mPath = new Path();
        mPathTick = new Path();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mPathWidth);
        mPaint.setStyle(Paint.Style.STROKE);//
        mPaint.setColor(mPathColor);
        if (mIsBorderStyle) {
            bgDrawable = ResUtil.generateCircleBgDrawable(mPathWidth, mBorderColor);
            setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Drawable background = getBackground();
        int action = event.getAction();
//        e(new Exception().getStackTrace()[0].getMethodName() + "  --  " + action);

        if (action == MotionEvent.ACTION_DOWN) {
            onTouchDown(event, background);
        }

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            onTouchUp(event, background);
        }

        return true;
    }

    private void onTouchDown(MotionEvent event, Drawable background) {
        isDown = true;
        isUp = false;
        mCurDrawDelay = mDrawDelay;
        mCurDrawStep = mDrawStep;

//        if (mIsBorderStyle) {
//            setBackgroundColor(Color.TRANSPARENT);
//        }

        resetPath();
//        if (background != null) {
//            background.setState(new int[]{android.R.attr.state_pressed});
//        } else {
        invalidate();
//        }
    }

    private void resetPath() {
        mPath.reset();
        mCurDrawStep = mDrawStep;
        mCurAngle = mStartAngle;
        isOneCircle = false;
    }

    private void onTouchUp(MotionEvent event, Drawable background) {
        isDown = false;
        isUp = true;

        mCurDrawDelay = 0;//抬手之后, 快速画完
        mCurDrawStep = 45f;

        isSelected = !isSelected;

//        if (background != null) {
//            background.setState(new int[]{});
//        } else {
        invalidate();
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mPathColor);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        if (isDown || isUp) {
            mCurAngle = addNextPath(mCurAngle);
            canvas.drawPath(mPath, mPaint);

            if (isOneCircle) {//一圈
                if (isUp) {//手抬起
                    resetPath();
                    this.performClick();
                    isUp = false;
                    mPathTick.reset();
                    mTickCurIndex = 0;
                    isBeginSelecting = isSelected;

                    if (mIsSelectStyle) {
                        if (onSelectChanged != null && !isSelected) {
                            onSelectChanged.onSelectChanged(this, isSelected);
                        }
                    }
                }
            }

            postInvalidateDelayed(mCurDrawDelay);
        } else {
            if (mIsBorderStyle) {
                bgDrawable.draw(canvas);
            }
        }

        if (mIsSelectStyle) {
            if (isSelected) {
                mPaint.setStrokeCap(Paint.Cap.ROUND);
                mPaint.setColor(mPathSelectColor);
                if (isBeginSelecting) {
                    for (int i = 0; i < mTickStep; i++) {
                        if (isBeginSelecting) {
                            mTickCurIndex = addNextTickPath(mTickCurIndex);
                        }
                    }
                    canvas.drawPath(mPathTick, mPaint);
                    postInvalidateDelayed(0);
                } else {
                    mTickCurIndex = 0;
                    canvas.drawPath(mPathTick, mPaint);
                }
            } else {

            }
        }
//        canvas.drawRect(0, 0, mViewWidth, mViewHeight, mPaint);//测试边框
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width, height, min;
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        min = Math.min(width, height);

        //强制为正方形
        setMeasuredDimension(min, min);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        mPathTick.reset();

        if (bgDrawable != null) {
            bgDrawable.setBounds(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
        }

        mTickLeftList = new ArrayList<>();
        mTickRightList = new ArrayList<>();

        //对勾的path计算
        RectF rectF = getDrawRectF();
        rectF.centerX();
        rectF.centerY();
        float radius = rectF.height() / 2;//半径
        int padding = getPaddingBottom();//注意 只是用xml中的padding属性
        PointF startP = new PointF(padding + radius, padding + radius + radius / 2);
//        PointF endP = new PointF(padding + radius, padding + radius + radius / 2);
        PointF nextP = new PointF(startP.x, startP.y);

        int count = 0;
        /*左边的线段*/
        do {
            mTickLeftList.add(new PointF(nextP.x, nextP.y));
            nextP.x -= 1;
            nextP.y -= 1;
            count++;
        } while (nextP.y >= padding + radius);

        Collections.reverse(mTickLeftList);

        /*右边的线段*/
        nextP.x = startP.x;
        nextP.y = startP.y;

        mTickRightList.add(new PointF(nextP.x, nextP.y + 1));//连接点的地方,填充数据

        for (int i = 0; i < (count + count / 2); i++) {
            mTickRightList.add(new PointF(nextP.x, nextP.y));
            nextP.x += 1;
            nextP.y -= 1;
        }
    }

    /**
     * 获取下一个path的点坐标
     */
    private float addNextPath(float curAngle) {
        float retAngle;
        float step = mCurDrawStep;

        if (isOneCircle) {
            resetPath();
            curAngle = mStartAngle;
        }

        if (curAngle - mStartAngle + step >= 360) {
            isOneCircle = true;
            retAngle = mStartAngle + 360;
        } else {
            retAngle = curAngle + step;
        }
        mPath.addArc(getDrawRectF(), mStartAngle, retAngle - mStartAngle);

        return retAngle;
    }

    /**
     * 获取下一个对勾path的点坐标
     */
    private int addNextTickPath(int curIndex) {
        int retIndex = curIndex;
        if (curIndex == 0) {
            PointF first = mTickLeftList.get(0);
            mPathTick.reset();
            mPathTick.moveTo(first.x - getTickOffset(), first.y);
        }

        int leftSize = mTickLeftList.size();
        int rightSize = mTickRightList.size();

        if (curIndex < leftSize) {
            lineTickPath(mTickLeftList.get(curIndex));
        } else if (curIndex < (rightSize + leftSize)) {
            lineTickPath(mTickRightList.get(curIndex - leftSize));
        } else {
            isBeginSelecting = false;
            if (onSelectChanged != null) {
                onSelectChanged.onSelectChanged(this, true);
            }
        }
        retIndex++;
        return retIndex;
    }

    private void lineTickPath(PointF pointF) {
        mPathTick.lineTo(pointF.x - getTickOffset(), pointF.y);
        mPathTick.moveTo(pointF.x - getTickOffset(), pointF.y);
    }

    private int getTickOffset() {
        int offset = (int) (getDrawRectF().width() / 12);//
        return offset;
    }

    /**
     * 去除padding 的 可绘制区域
     */
    private RectF getDrawRectF() {
        RectF rectF = new RectF();
        int padding = getPaddingTop();//注意 只是用xml中的padding属性
        rectF.set(padding, padding, mViewWidth - padding, mViewHeight - padding);
        return rectF;
    }

    public void e(String log) {
        Log.e("angcyo", log);
    }

    public void setOnSelectChanged(OnSelectChanged onSelectChanged) {
        this.onSelectChanged = onSelectChanged;
    }

    public interface OnSelectChanged {
        void onSelectChanged(View view, boolean isSelect);
    }
}
