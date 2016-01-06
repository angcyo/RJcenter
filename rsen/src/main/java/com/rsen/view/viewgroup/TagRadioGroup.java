package com.rsen.view.viewgroup;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rsen.util.ResUtil;

/**
 * 标签单选按钮组
 * Created by angcyo on 2016-01-06.
 */
public class TagRadioGroup extends RadioGroup {
    private int childWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int childHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int childTextColor = Color.parseColor("#969696");
    private int childPressTextColor = Color.WHITE;
    private int paddingLeft = 15;//dp
    private int paddingTop = 10;
    private int paddingRight = 15;
    private int paddingBottom = 10;

    private float borderWidth = 4f;//线框的宽度

    private float round = 6f;//dp 单位

    public TagRadioGroup(Context context) {
        this(context, null);
    }

    public TagRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public static ColorStateList generateTextColor(int pressColor, int defaultColor) {
        ColorStateList stateList = new ColorStateList(new int[][]{{android.R.attr.state_pressed}, {android.R.attr.state_checked}, {}},
                new int[]{pressColor, pressColor, defaultColor});
        return stateList;
    }

    public static Drawable generateBgDrawable(float radiiL, float radiiR, RectF insetOffset, int pressColor) {
        //外环的圆角矩形
        float[] outRadii = new float[]{radiiL, radiiL, radiiR, radiiR, radiiR, radiiR, radiiL, radiiL};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个

        //与内环的距离
        RectF inset = new RectF(0, 0, 0, 0);

        //按下状态
        Shape roundRectShape = new RoundRectShape(outRadii, inset, null);//圆角背景
        ShapeDrawable shopDrawablePress = new ShapeDrawable(roundRectShape);//圆角shape
        shopDrawablePress.getPaint().setColor(pressColor);//设置颜色

        //正常状态
        Shape roundRectShapeNormal = new RoundRectShape(outRadii, insetOffset, null);
        ShapeDrawable shopDrawableNormal = new ShapeDrawable(roundRectShapeNormal);
        shopDrawableNormal.getPaint().setColor(pressColor);

        StateListDrawable bgStateDrawable = new StateListDrawable();//状态shape
        bgStateDrawable.addState(new int[]{android.R.attr.state_pressed}, shopDrawablePress);//按下状态
        bgStateDrawable.addState(new int[]{android.R.attr.state_checked}, shopDrawablePress);//按下状态
        bgStateDrawable.addState(new int[]{}, shopDrawableNormal);//其他状态

        return bgStateDrawable;
    }

    private void init() {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initChildButton();
    }

    private void initChildButton() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton childView = (RadioButton) view;
                childView.setButtonDrawable(new ColorDrawable());//去掉系统默认的圆圈
                childView.setTextColor(textColor());
                childView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

                if (i == 0) {
                    initFirstButton(childView);
                } else if (i == count - 1) {
                    initLastButton(childView);
                } else {
                    initMidButton(childView);
                }
            }
        }
    }

    private Drawable bgDrawable(float roundL, float roundR, RectF insetOffset) {
        return generateBgDrawable(roundL, roundR, insetOffset, childTextColor);
    }

    private ColorStateList textColor() {
        return generateTextColor(childPressTextColor, childTextColor);
    }

    private void initMidButton(RadioButton childView) {
        ResUtil.setBgDrawable(childView, bgDrawable(0, 0, new RectF(borderWidth / 2f, borderWidth, borderWidth / 2, borderWidth)));
    }

    private void initLastButton(RadioButton childView) {
        ResUtil.setBgDrawable(childView, bgDrawable(0, round, new RectF(borderWidth / 2f, borderWidth, borderWidth, borderWidth)));
    }

    private void initFirstButton(RadioButton childView) {
        ResUtil.setBgDrawable(childView, bgDrawable(round, 0, new RectF(borderWidth, borderWidth, borderWidth / 2, borderWidth)));
    }
}