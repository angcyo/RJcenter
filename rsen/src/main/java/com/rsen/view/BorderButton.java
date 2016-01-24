package com.rsen.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import com.angcyo.rsen.R;

/**
 * Created by angcyo on 16-01-24-024.
 */
public class BorderButton extends Button {
    float mBorderWidth = 2;//dp 宽度
    float mBorderRound = 2;//dp 圆角角度
    @ColorInt
    int mBorderColor = Color.GRAY;//边框颜色
    @ColorInt
    int mBorderColorPress = mBorderColor;

    @ColorInt
    int mBorderBgColor = Color.TRANSPARENT;//背景颜色
    @ColorInt
    int mBorderBgColorPress = Color.parseColor("#20000000");

    public BorderButton(Context context) {
        this(context, null);
    }

    public static float dpToPx(Resources res, float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
        return px;
    }

    public BorderButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyleSmall);
    }

    public BorderButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBorderWidth = dpToPx(getResources(), mBorderWidth);
        mBorderRound = dpToPx(getResources(), mBorderRound);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BorderButton);
        mBorderWidth = typedArray.getDimension(R.styleable.BorderButton_borderBtWidth, mBorderWidth);
        mBorderRound = typedArray.getDimension(R.styleable.BorderButton_borderBtRound, mBorderRound);
        mBorderColor = typedArray.getColor(R.styleable.BorderButton_borderBtColor, mBorderColor);
        mBorderBgColor = typedArray.getColor(R.styleable.BorderButton_borderBtColorBg, mBorderBgColor);
        mBorderBgColorPress = typedArray.getColor(R.styleable.BorderButton_borderBtColorBgPress, mBorderBgColorPress);
        mBorderColorPress = typedArray.getColor(R.styleable.BorderButton_borderBtColorPress, mBorderColorPress);

        typedArray.recycle();
        initView();
    }

    private void initView() {
        setBgDrawable(this, generateBg());
    }

    @SuppressLint("NewApi")
    public static void setBgDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    private Drawable generateBg() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        float width = mBorderWidth;
        float round = mBorderRound;
        int pressColor = mBorderColorPress;
        int color = mBorderColor;
        int pressColorBG = mBorderBgColorPress;
        int colorBG = mBorderBgColor;

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
