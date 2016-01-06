package com.rsen.util;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by angcyo on 15-12-31 031 10:52 上午.
 */
public class ResUtil {
    /**
     * Px to dp float.
     *
     * @param res the res
     * @param px  the px
     * @return the float
     */
    public static float pxToDp(Resources res, float px) {
        float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, res.getDisplayMetrics());

        return dp;
    }

    /**
     * Generate text color color state list.
     *
     * @param pressColor   the press color
     * @param defaultColor the default color
     * @return the color state list
     */
    public static ColorStateList generateTextColor(int pressColor, int defaultColor) {
        ColorStateList stateList = new ColorStateList(new int[][]{{android.R.attr.state_pressed}, {}},
                new int[]{pressColor, defaultColor});
        return stateList;
    }

    /**
     * Generate bg drawable drawable.
     *
     * @param radii        the radii
     * @param borderWidth  the border width
     * @param pressColor   the press color
     * @param defaultColor the default color
     * @return the drawable
     */
    public static Drawable generateBgDrawable(Resources res, float radii, float borderWidth, int pressColor, int defaultColor) {

        radii = pxToDp(res, radii);
        borderWidth = pxToDp(res, borderWidth);

        //外环的圆角矩形
        float[] outRadii = new float[]{radii, radii, radii, radii, radii, radii, radii, radii};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个

        //与内环的距离
        RectF inset = new RectF(borderWidth, borderWidth, borderWidth, borderWidth);

        //按下状态
        Shape roundRectShape = new RoundRectShape(outRadii, inset, null);//圆角背景
        ShapeDrawable shopDrawablePress = new ShapeDrawable(roundRectShape);//圆角shape
        shopDrawablePress.getPaint().setColor(pressColor);//设置颜色

        //正常状态
        Shape roundRectShapeNormal = new RoundRectShape(outRadii, inset, null);
        ShapeDrawable shopDrawableNormal = new ShapeDrawable(roundRectShapeNormal);
        shopDrawableNormal.getPaint().setColor(defaultColor);

        StateListDrawable bgStateDrawable = new StateListDrawable();//状态shape
        bgStateDrawable.addState(new int[]{android.R.attr.state_pressed}, shopDrawablePress);//按下状态
        bgStateDrawable.addState(new int[]{}, shopDrawableNormal);//其他状态

        return bgStateDrawable;
    }

    public static Drawable generateBgDrawable(float rL1, float rL2, float rT1, float rT2,
                                              float rR1, float rR2, float rB1, float rB2,
                                              int pressColor, int defaultColor) {
        //外环的圆角矩形
        float[] outRadii = new float[]{rL1, rL2, rT1, rT2, rR1, rR2, rB1, rB2};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个

        //与内环的距离
        RectF inset = new RectF(0, 0, 0, 0);

        //按下状态
        Shape roundRectShape = new RoundRectShape(outRadii, inset, null);//圆角背景
        ShapeDrawable shopDrawablePress = new ShapeDrawable(roundRectShape);//圆角shape
        shopDrawablePress.getPaint().setColor(pressColor);//设置颜色

        //正常状态
        Shape roundRectShapeNormal = new RoundRectShape(outRadii, inset, null);
        ShapeDrawable shopDrawableNormal = new ShapeDrawable(roundRectShapeNormal);
        shopDrawableNormal.getPaint().setColor(defaultColor);

        StateListDrawable bgStateDrawable = new StateListDrawable();//状态shape
        bgStateDrawable.addState(new int[]{android.R.attr.state_pressed}, shopDrawablePress);//按下状态
        bgStateDrawable.addState(new int[]{}, shopDrawableNormal);//其他状态

        return bgStateDrawable;
    }

    public static Drawable generateBgDrawable(float radiiL, float radiiR, int pressColor, int defaultColor) {
        //外环的圆角矩形
        float[] outRadii = new float[]{radiiL, radiiL, radiiR, radiiR, radiiR, radiiR, radiiL, radiiL};//四个角的 圆角幅度,8个可以设置的值,每个角都有2个边 2*4=8个

        //与内环的距离
        RectF inset = new RectF(0, 0, 0, 0);

        //按下状态
        Shape roundRectShape = new RoundRectShape(outRadii, inset, null);//圆角背景
        ShapeDrawable shopDrawablePress = new ShapeDrawable(roundRectShape);//圆角shape
        shopDrawablePress.getPaint().setColor(pressColor);//设置颜色

        //正常状态
        Shape roundRectShapeNormal = new RoundRectShape(outRadii, inset, null);
        ShapeDrawable shopDrawableNormal = new ShapeDrawable(roundRectShapeNormal);
        shopDrawableNormal.getPaint().setColor(defaultColor);

        StateListDrawable bgStateDrawable = new StateListDrawable();//状态shape
        bgStateDrawable.addState(new int[]{android.R.attr.state_pressed}, shopDrawablePress);//按下状态
        bgStateDrawable.addState(new int[]{}, shopDrawableNormal);//其他状态

        return bgStateDrawable;
    }

    @SuppressLint("NewApi")
    public static void setBgDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
