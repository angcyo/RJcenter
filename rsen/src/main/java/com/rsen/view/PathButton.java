package com.rsen.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

import com.angcyo.rsen.R;
import com.rsen.util.ResUtil;

/**
 * Created by angcyo on 16-01-17-017.
 */
public class PathButton extends Button {

    /**
     * 用来绘制路径,最重要的成员
     */
    Path mPath;
    /**
     * 画笔
     */
    Paint mPaint;

    /**
     * path 的颜色
     */
    @ColorInt
    int mPathColor = Color.WHITE;

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
     * 当前绘制到的点坐标
     */
    Point mCurPoint;
    /**
     * 当前开始绘制的点坐标
     */
    Point mStartPoint;
    /**
     * 每次绘制,移动的距离,越大,移动愉快
     */
    int mDrawStep = 10;
    int mCurDrawStep;
    /**
     * 多少毫秒绘制一次
     */
    int mDrawDelay = 40;
    int mCurDrawDelay;
    int mMovePathCount = 0x0000;//经过了多少个边, 如果大于0x1111,说明已经绘制了一圈;

    /**
     * <enum name="LEFT_TOP" value="1" />
     * <enum name="TOP" value="2" />
     * <enum name="RIGHT_TOP" value="3" />
     * <enum name="RIGHT" value="4" />
     * <enum name="RIGHT_BOTTOM" value="5" />
     * <enum name="BOTTOM" value="6" />
     * <enum name="LEFT_BOTTOM" value="7" />
     * <enum name="LEFT" value="8" />
     */
    int mStartGravity = 2;

    /**
     * 样式, 背景透明,只显示四条边框线
     */
    boolean mIsBorderStyle = false;
    /**
     * 边框的宽度
     */
    float mBorderWidth = 2;//dp
    /**
     * 边框的圆角
     */
    float mBorderRound = 2;//dp

    @ColorInt
    int mBorderColor = Color.BLUE;
    /**
     * 边框背景
     */
    Drawable bgDrawable;
    /**
     * 原始的背景
     */
    Drawable rawBackground;

    public PathButton(Context context) {
        this(context, null);
    }

    public PathButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public PathButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PathButton);

        mPathWidth = typedArray.getDimension(R.styleable.PathButton_pathWidth, ResUtil.dpToPx(getResources(), mPathWidth));
        mPathOffset = (int) typedArray.getDimension(R.styleable.PathButton_pathOffset, mPathWidth / 2);
        mPathColor = typedArray.getColor(R.styleable.PathButton_pathColor, mPathColor);
        mDrawStep = typedArray.getInteger(R.styleable.PathButton_pathDrawStep, mDrawStep);
        mDrawDelay = typedArray.getInteger(R.styleable.PathButton_pathDrawDelay, mDrawDelay);
        mStartGravity = typedArray.getInteger(R.styleable.PathButton_pathStartGravity, mStartGravity);

        mIsBorderStyle = typedArray.getBoolean(R.styleable.PathButton_pathBorderStyle, mIsBorderStyle);
        mBorderWidth = typedArray.getDimension(R.styleable.PathButton_pathBorderWidth, ResUtil.dpToPx(getResources(), mBorderWidth));
        mBorderRound = typedArray.getDimension(R.styleable.PathButton_pathBorderRound, ResUtil.dpToPx(getResources(), mBorderRound));
        mBorderColor = typedArray.getColor(R.styleable.PathButton_pathBorderColor, mBorderColor);

        typedArray.recycle();
        initView();
    }

    private void initView() {
        setWillNotDraw(false);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mPathWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);//
        mPaint.setColor(mPathColor);
        if (mIsBorderStyle) {
            bgDrawable = ResUtil.generateBgDrawable(mBorderRound, mBorderWidth, mBorderColor);
            ResUtil.setBgDrawable(this, bgDrawable);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
//        e(new Exception().getStackTrace()[0].getMethodName() + "  --  " + action);

        if (action == MotionEvent.ACTION_DOWN) {
            rawBackground = getBackground();
            onTouchDown(event, rawBackground);
        }

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            onTouchUp(event, rawBackground);
        }

        return true;
    }

    private void onTouchDown(MotionEvent event, Drawable background) {
        isDown = true;
        isUp = false;
        mCurDrawDelay = mDrawDelay;
        mCurDrawStep = mDrawStep;

        if (mIsBorderStyle) {
            setBackgroundColor(Color.TRANSPARENT);
        }

        resetPath(getStartPoint(mStartGravity));
        if (background != null) {
            background.setState(new int[]{android.R.attr.state_pressed});
        } else {
            invalidate();
        }
    }

    private void resetPath(Point startPoint) {
        mPath.reset();
        mStartPoint = mCurPoint = startPoint;
        mPath.moveTo(mStartPoint.x, mStartPoint.y);
        mPath.lineTo(mCurPoint.x, mCurPoint.y);
        mMovePathCount = 0x0000;
    }

    private void onTouchUp(MotionEvent event, Drawable background) {
        isDown = false;
        isUp = true;

        mCurDrawDelay = 10;//抬手之后, 快速画完
//        mCurDrawStep = (mViewHeight + mViewWidth) / 5;
//        mCurDrawStep = Math.min(mViewHeight, mViewWidth) * 3 / 4;

//        mCurDrawDelay = 1000 / (mViewWidth + mViewHeight) / mCurDrawStep;

        mCurDrawStep = Math.min(mViewHeight, mViewWidth) / mCurDrawDelay;


        if (background != null) {
            background.setState(new int[]{});
            ResUtil.setBgDrawable(this, background);
        } else {
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDown || isUp) {
            mCurPoint = addNextPath(mCurPoint);
            canvas.drawPath(mPath, mPaint);

            if (mCurPoint == mStartPoint) {//起点
                resetPath(mStartPoint);
                if (isUp) {
                    this.performClick();
                    isUp = false;
                    if (mIsBorderStyle) {
                        ResUtil.setBgDrawable(this, bgDrawable);
                    }
                }
            }
            postInvalidateDelayed(mCurDrawDelay);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    /**
     * 获取开始的绘制path的点坐标
     */
    private Point getStartPoint(int gravity) {
        Point point = new Point();

        /**
         * <enum name="LEFT_TOP" value="1" />
         * <enum name="TOP" value="2" />
         * <enum name="RIGHT_TOP" value="3" />
         * <enum name="RIGHT" value="4" />
         * <enum name="RIGHT_BOTTOM" value="5" />
         * <enum name="BOTTOM" value="6" />
         * <enum name="LEFT_BOTTOM" value="7" />
         * <enum name="LEFT" value="8" />
         */
        switch (gravity) {
            case 1:
                point.x = mPathOffset;
                point.y = mPathOffset;
                break;
            case 2:
                //默认在上边的中间
                point.x = mViewWidth / 2;
                point.y = mPathOffset;
                break;
            case 3:
                point.x = mViewWidth - mPathOffset;
                point.y = mPathOffset;
                break;
            case 4:
                point.x = mViewWidth - mPathOffset;
                point.y = mViewHeight / 2;
                break;
            case 5:
                point.x = mViewWidth - mPathOffset;
                point.y = mViewHeight - mPathOffset;
                break;
            case 6:
                point.x = mViewWidth / 2;
                point.y = mViewHeight - mPathOffset;
                break;
            case 7:
                point.x = mPathOffset;
                point.y = mViewHeight - mPathOffset;
                break;
            case 8:
                point.x = mPathOffset;
                point.y = mViewHeight / 2;
                break;
            default:
                break;
        }
        return point;
    }

    /**
     * 获取下一个path的点坐标
     */
    private Point addNextPath(Point curPoint) {
//        e("x-->" + curPoint.x + " y-->" + curPoint.y);
//        e("x-->" + curPoint.x + " y-->" + curPoint.y);

        Point retPoint = new Point();
        Point startPoint = mStartPoint;
        int width = mViewWidth;
        int height = mViewHeight;
        int offset = mPathOffset;
        int step = mCurDrawStep;

        if (curPoint.y == offset && (curPoint.x > offset && curPoint.x < width - offset)) {//上边
            mMovePathCount |= 0x0010;
            retPoint.y = curPoint.y;
            float sub = curPoint.x + step + offset - width;//超出边框的部分
            if (sub > 0) {//超出边框
                retPoint.x = width - offset;
                retPoint.y = (int) (offset + sub);
                mPath.lineTo(retPoint.x + mPathWidth / 2, curPoint.y);//移动到右上角
                mPath.moveTo(retPoint.x, curPoint.y);//
            } else {
                retPoint.x = curPoint.x + step;//横坐标
            }
        } else if (curPoint.y == height - offset && (curPoint.x > offset && curPoint.x < width - offset)) {//下边
            mMovePathCount |= 0x0001;
            retPoint.y = curPoint.y;
            float sub = curPoint.x - step;//超出边框的部分
            if (sub >= offset) {//未超出边框
                retPoint.x = (int) sub;//横坐标
            } else {
                retPoint.x = offset;//横坐标
                sub = offset - sub;//超出的距离
                mPath.lineTo(offset - mPathWidth / 2, curPoint.y);
                mPath.moveTo(offset, curPoint.y);//移动到左下角
                retPoint.y = (int) (curPoint.y - Math.abs(sub));
            }
        } else if (curPoint.x == offset && (curPoint.y > offset && curPoint.y < height - offset)) {//左边
            mMovePathCount |= 0x1000;
            retPoint.x = curPoint.x;
            float sub = curPoint.y - step - offset;
            if (sub > 0) {//未超出
                retPoint.y = curPoint.y - step;
            } else {
                mPath.lineTo(curPoint.x, offset - mPathWidth / 2);//左上角
                mPath.moveTo(curPoint.x, offset);//
                retPoint.x = (int) (offset + Math.abs(sub));
                retPoint.y = offset;
            }
        } else if (curPoint.x == width - offset && (curPoint.y > offset && curPoint.y < height - offset)) {//右边
            mMovePathCount |= 0x0100;
            retPoint.x = curPoint.x;
            float sub = curPoint.y + step + offset - height;
            if (sub > 0) {//超出
                mPath.lineTo(curPoint.x, height - offset + mPathWidth / 2);//右下角
                mPath.moveTo(curPoint.x, height - offset);//
                retPoint.x = (int) (width - offset - sub);
                retPoint.y = height - offset;
            } else {
                retPoint.y = curPoint.y + step;
            }
        }

        mPath.lineTo(retPoint.x, retPoint.y);

        //是否一圈到了
        if (mMovePathCount >= 0x1111) {
            boolean result = false;
            switch (mStartGravity) {
                case 1://左上
                case 2://上
                case 3://右上
                case 4://右
                    if (retPoint.x >= startPoint.x && retPoint.y >= startPoint.y) {
                        result = true;
                    }
                    break;
                case 5://右下
                case 6://下
                case 7://左下
                case 8://左
                    if (retPoint.x <= startPoint.x && retPoint.y <= startPoint.y) {
                        result = true;
                    }
                    break;
                default:
                    break;
            }

            if (result) {
                //回到起点
                return startPoint;
            }
        }
        return retPoint;
    }

    public void e(String log) {
        Log.e("angcyo", log);
    }
}
