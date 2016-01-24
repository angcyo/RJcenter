package com.rsen.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.angcyo.rsen.R;
import com.rsen.util.ResUtil;

import java.util.ArrayList;

/**
 * Created by angcyo on 16-01-24-024.
 */
public class PathButton extends BasePathButton {

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
    Drawable bgDrawable;
    float mPathRound = 4;//dp 圆角

    public PathButton(Context context) {
        this(context, null);
    }

    public PathButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public PathButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPathRound = ResUtil.dpToPx(getResources(), mPathRound);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PathButton);

        mPathWidth = typedArray.getDimension(R.styleable.PathButton_pathWidth, mPathWidth);
        mPathColor = typedArray.getColor(R.styleable.PathButton_pathColor, mPathColor);
        mStartGravity = typedArray.getInteger(R.styleable.PathButton_pathStartGravity, mStartGravity);
        mPathRound = typedArray.getDimension(R.styleable.PathButton_pathRound, mPathRound);
        typedArray.recycle();

        initView();
    }

    @SuppressLint("NewApi")
    public static void setBgDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    protected void initView() {
        mPaint.setColor(mPathColor);
        mPaint.setStrokeWidth(mPathWidth);
        bgDrawable = generateBg();
        setBgDrawable(this, null);
    }

    @Override
    protected void fillList(RectF drawRect, ArrayList<PointF> pathList) {
        int gravity = mStartGravity;

        ArrayList<PointF> topList = new ArrayList<>();
        ArrayList<PointF> leftList = new ArrayList<>();
        ArrayList<PointF> rightList = new ArrayList<>();
        ArrayList<PointF> bottomList = new ArrayList<>();

        /*上边线*/
        PointF startPoint = new PointF(drawRect.left, drawRect.top);
        while (startPoint.x < drawRect.right) {
            topList.add(startPoint);
            startPoint = new PointF(startPoint.x + 1, startPoint.y);//
        }

        /*右边线*/
        startPoint = new PointF(drawRect.right, drawRect.top);
        while (startPoint.y < drawRect.bottom) {
            rightList.add(startPoint);
            startPoint = new PointF(startPoint.x, startPoint.y + 1);//
        }

        /*下边线*/
        startPoint = new PointF(drawRect.right, drawRect.bottom);
        while (startPoint.x > drawRect.left) {
            bottomList.add(startPoint);
            startPoint = new PointF(startPoint.x - 1, startPoint.y);//
        }

        /*左边线*/
        startPoint = new PointF(drawRect.left, drawRect.bottom);
        while (startPoint.y > drawRect.top) {
            leftList.add(startPoint);
            startPoint = new PointF(startPoint.x, startPoint.y - 1);//
        }


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
            case 1://左上
                pathList.addAll(topList);
                pathList.addAll(rightList);
                pathList.addAll(bottomList);
                pathList.addAll(leftList);
                break;
            case 2:
                //默认在上边的中间
                for (int i = topList.size() / 2; i < topList.size(); i++) {
                    pathList.add(topList.get(i));
                }
                pathList.addAll(rightList);
                pathList.addAll(bottomList);
                pathList.addAll(leftList);
                for (int i = 0; i < topList.size() / 2; i++) {
                    pathList.add(topList.get(i));
                }
                break;
            case 3://右上
                pathList.addAll(rightList);
                pathList.addAll(bottomList);
                pathList.addAll(leftList);
                pathList.addAll(topList);
                break;
            case 4:
                for (int i = rightList.size() / 2; i < rightList.size(); i++) {
                    pathList.add(rightList.get(i));
                }
                pathList.addAll(bottomList);
                pathList.addAll(leftList);
                pathList.addAll(topList);
                for (int i = 0; i < rightList.size() / 2; i++) {
                    pathList.add(rightList.get(i));
                }
                break;
            case 5://右下
                pathList.addAll(bottomList);
                pathList.addAll(leftList);
                pathList.addAll(topList);
                pathList.addAll(rightList);
                break;
            case 6:
                for (int i = rightList.size() / 2; i < rightList.size(); i++) {
                    pathList.add(rightList.get(i));
                }
                pathList.addAll(leftList);
                pathList.addAll(topList);
                pathList.addAll(rightList);
                for (int i = 0; i < bottomList.size() / 2; i++) {
                    pathList.add(bottomList.get(i));
                }
                break;
            case 7://左下
                pathList.addAll(leftList);
                pathList.addAll(topList);
                pathList.addAll(rightList);
                pathList.addAll(bottomList);
                break;
            case 8:
                for (int i = leftList.size() / 2; i < leftList.size(); i++) {
                    pathList.add(leftList.get(i));
                }
                pathList.addAll(topList);
                pathList.addAll(rightList);
                pathList.addAll(bottomList);
                for (int i = 0; i < leftList.size() / 2; i++) {
                    pathList.add(leftList.get(i));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDrawPathStart() {
        postInvalidate();
    }

    @Override
    protected void onDrawPathEnd(Canvas canvas) {
        this.performClick();//调用单击事件
        postInvalidate();
    }

    @Override
    protected void needDraw(Canvas canvas) {
        RectF drawRect = getDrawRectF();
        bgDrawable.setBounds((int) (drawRect.left - mPathWidth / 2), (int) (drawRect.top - mPathWidth / 2),
                (int) (drawRect.right + mPathWidth / 2), (int) (drawRect.bottom + mPathWidth / 2));
        bgDrawable.draw(canvas);
    }

    private Drawable generateBg() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        float width = mPathWidth;
        float round = mPathRound;
        int pressColor = mPathColor;
        int color = mPathColor;
        int pressColorBG = Color.TRANSPARENT;
        int colorBG = Color.TRANSPARENT;

        //按下状态,边框
        float[] outRadii = new float[]{round, round, round, round, round, round, round, round};
        RoundRectShape pressRectShape = new RoundRectShape(outRadii, new RectF(width, width, width, width), outRadii);
        ShapeDrawable pressShape = new ShapeDrawable(pressRectShape);
        pressShape.getPaint().setColor(pressColor);

        //按下背景
        RoundRectShape pressRectShapeBg = new RoundRectShape(outRadii, null, null);
        ShapeDrawable pressShapeBg = new ShapeDrawable(pressRectShapeBg);
        pressShapeBg.getPaint().setColor(pressColorBG);


        LayerDrawable pressDrawable = new LayerDrawable(new Drawable[]{pressShapeBg, pressShape});//先绘制背景色,再绘制边框

        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);

        //正常状态,边框
        RoundRectShape rectShape = new RoundRectShape(outRadii, new RectF(width, width, width, width), outRadii);
        ShapeDrawable shape = new ShapeDrawable(rectShape);
        shape.getPaint().setColor(color);
        //正常背景
        RoundRectShape rectShapeBg = new RoundRectShape(outRadii, null, null);
        ShapeDrawable shapeBg = new ShapeDrawable(rectShapeBg);
        shapeBg.getPaint().setColor(colorBG);


        LayerDrawable drawable = new LayerDrawable(new Drawable[]{shapeBg, shape});//先绘制背景色,再绘制边框
        stateListDrawable.addState(new int[]{}, drawable);

        return stateListDrawable;
    }
}
