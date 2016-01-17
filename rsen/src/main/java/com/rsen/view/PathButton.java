package com.rsen.view;

import android.content.Context;
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
    float mPathOffset = 2;//
    boolean isDown = false;
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
    int mDrawStep = 20;

    public PathButton(Context context) {
        this(context, null);
    }

    public PathButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public PathButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPathWidth = ResUtil.dpToPx(getResources(), mPathWidth);

        initView();
    }

    private void initView() {
        setWillNotDraw(false);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mPathWidth);
        mPaint.setStyle(Paint.Style.STROKE);//
        mPaint.setColor(mPathColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Drawable background = getBackground();
        int action = event.getAction();
        e(new Exception().getStackTrace()[0].getMethodName() + "  --  " + action);

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

        mPath.reset();
        mStartPoint = mCurPoint = getStartPoint(-1);
        mPath.moveTo(mStartPoint.x, mStartPoint.y);
        mPath.lineTo(mCurPoint.x, mCurPoint.y);

        if (background != null) {
            background.setState(new int[]{android.R.attr.state_pressed});
        }
    }

    private void onTouchUp(MotionEvent event, Drawable background) {
        isDown = false;

        if (background != null) {
            background.setState(new int[]{});
        }
//        this.performClick();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Point nextPoint = ;
//        mPath.lineTo(nextPoint.x, nextPoint.y);
        if (isDown) {
            canvas.drawPath(mPath, mPaint);
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

        //默认在上边的中间
        point.x = mViewWidth / 2;
        point.y = (int) (0 + mPathOffset);

        return point;
    }

    /**
     * 获取下一个path的点坐标
     */
    private Point getNextPoint(Point curPoint) {
        Point retPoint = new Point();
        Point startPoint = mStartPoint;
        if (startPoint.y == curPoint.y) {//在同一横向上
            retPoint.y = curPoint.y;
            if (startPoint.y > mViewHeight / 2) {//在下边
                curPoint.x - mDrawStep;//
            } else {//在上边

            }

        } else {//在同一纵向上
            retPoint.x = curPoint.x;
            if (startPoint.x > mViewWidth / 2) {//在右边

            } else {//在左边

            }
        }

        //默认在上边的中间
//        point.x = mViewWidth / 2;
//        point.y = (int) (0 + mPathOffset);

        return retPoint;
    }

    public void e(String log) {
        Log.e("angcyo", log);
    }
}
